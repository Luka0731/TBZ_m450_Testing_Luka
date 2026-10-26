package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
@DisplayName("Aufgabe 1 – Controller-Tests RecipeController (MockMvc)")
class RecipeControllerTest {
    private static final String FRONTEND_ORIGIN = "http://localhost:3000";
    @Mock private RecipeService recipeService;
    @Mock private RecipeEntityMapper recipeEntityMapper;
    @InjectMocks private RecipeController recipeController;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    private Ingredient tomato;
    private Recipe lasagne;
    private Recipe spaghetti;



    // |----- setup -----|

    @BeforeEach
    void setUp() {
        // MockMvc ohne Spring Context: nur der Controller wird geladen, Service und Mapper sind Mocks
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

        tomato = new Ingredient(UUID.randomUUID(), "Tomato", "The big ones", Unit.PIECE, 5);
        Ingredient pasta = new Ingredient(UUID.randomUUID(), "Spaghetti", "al dente", Unit.GRAMM, 500);
        lasagne = new Recipe(UUID.randomUUID(), "Lasagne al Forno", "Mit Bechamelsauce",
                "https://example.com/lasagne.jpg", List.of(tomato));
        spaghetti = new Recipe(UUID.randomUUID(), "Spaghetti Bolognese", "Der Klassiker",
                "https://example.com/spaghetti.jpg", List.of(tomato, pasta));
    }



    // |----- GET /api/recipes -----|

    @Test
    @DisplayName("GET /api/recipes liefert 200 und alle Rezepte als JSON")
    void getRecipesReturns200() throws Exception {
        when(recipeService.getRecipes()).thenReturn(List.of(lasagne, spaghetti));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(lasagne.getId().toString()))
                .andExpect(jsonPath("$[0].name").value("Lasagne al Forno"))
                .andExpect(jsonPath("$[1].name").value("Spaghetti Bolognese"))
                .andExpect(jsonPath("$[1].ingredients.length()").value(2))
                .andExpect(jsonPath("$[1].ingredients[1].unit").value("GRAMM"))
                .andExpect(jsonPath("$[1].ingredients[1].amount").value(500));

        verify(recipeService, times(1)).getRecipes();
    }

    @Test
    @DisplayName("GET /api/recipes liefert 200 und eine leere Liste, wenn es keine Rezepte gibt")
    void getRecipesReturnsEmptyList() throws Exception {
        when(recipeService.getRecipes()).thenReturn(List.of());

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(recipeService, times(1)).getRecipes();
    }

    @Test
    @DisplayName("DELETE /api/recipes ist nicht erlaubt und liefert 405")
    void deleteRecipesReturns405() throws Exception {
        mockMvc.perform(delete("/api/recipes"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(recipeService);
    }



    // |----- GET /api/recipes/recipe/{recipeId} -----|

    @Test
    @DisplayName("GET /api/recipes/recipe/{id} liefert 200 und das passende Rezept")
    void getRecipeReturns200() throws Exception {
        when(recipeService.getRecipeById(lasagne.getId())).thenReturn(lasagne);

        mockMvc.perform(get("/api/recipes/recipe/{recipeId}", lasagne.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(lasagne.getId().toString()))
                .andExpect(jsonPath("$.name").value("Lasagne al Forno"))
                .andExpect(jsonPath("$.description").value("Mit Bechamelsauce"))
                .andExpect(jsonPath("$.imageUrl").value("https://example.com/lasagne.jpg"))
                .andExpect(jsonPath("$.ingredients[0].id").value(tomato.getId().toString()))
                .andExpect(jsonPath("$.ingredients[0].name").value("Tomato"))
                .andExpect(jsonPath("$.ingredients[0].comment").value("The big ones"))
                .andExpect(jsonPath("$.ingredients[0].unit").value("PIECE"))
                .andExpect(jsonPath("$.ingredients[0].amount").value(5));

        verify(recipeService, times(1)).getRecipeById(lasagne.getId());
    }

    @Test
    @DisplayName("BEFUND: GET mit unbekannter ID liefert 200 mit leerem Body statt 404")
    void getRecipeWithUnknownIdReturns200WithoutBody() throws Exception {
        UUID unknownId = UUID.randomUUID();
        when(recipeService.getRecipeById(unknownId)).thenReturn(null);

        mockMvc.perform(get("/api/recipes/recipe/{recipeId}", unknownId))
                // Ist-Verhalten: der Service gibt null zurück und der Controller verpackt das in ein 200.
                // Soll-Verhalten wäre: 404 Not Found (z.B. mit ResponseEntity.notFound().build()).
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(recipeService, times(1)).getRecipeById(unknownId);
    }

    @Test
    @DisplayName("GET mit ungültiger UUID liefert 400 und ruft den Service nicht auf")
    void getRecipeWithInvalidIdReturns400() throws Exception {
        mockMvc.perform(get("/api/recipes/recipe/{recipeId}", "keine-uuid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(recipeService);
    }



    // |----- POST /api/recipes -----|

    @Test
    @DisplayName("POST /api/recipes gibt das Rezept dem Service weiter und liefert das gespeicherte Rezept zurück")
    void addRecipeReturns200() throws Exception {
        Recipe newRecipe = new Recipe(null, "Fried Rice", "Asiatischer Klassiker",
                "https://example.com/rice.jpg", List.of(tomato));
        Recipe savedRecipe = new Recipe(UUID.randomUUID(), "Fried Rice", "Asiatischer Klassiker",
                "https://example.com/rice.jpg", List.of(tomato));
        when(recipeService.addRecipe(newRecipe)).thenReturn(savedRecipe);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRecipe.getId().toString()))
                .andExpect(jsonPath("$.name").value("Fried Rice"))
                .andExpect(jsonPath("$.ingredients[0].name").value("Tomato"));

        // der JSON-Body wird korrekt in ein Recipe umgewandelt (equals von Lombok vergleicht alle Felder)
        verify(recipeService, times(1)).addRecipe(newRecipe);
    }

    @Test
    @DisplayName("POST ohne Body liefert 400 und ruft den Service nicht auf")
    void addRecipeWithoutBodyReturns400() throws Exception {
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(recipeService);
    }

    @Test
    @DisplayName("POST mit ungültigem JSON liefert 400 und ruft den Service nicht auf")
    void addRecipeWithInvalidJsonReturns400() throws Exception {
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": "))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(recipeService);
    }

    @Test
    @DisplayName("POST mit falschem Content-Type liefert 415 und ruft den Service nicht auf")
    void addRecipeWithWrongContentTypeReturns415() throws Exception {
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Lasagne al Forno"))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(recipeService);
    }



    // |----- CORS -----|

    @Test
    @DisplayName("Das Frontend auf localhost:3000 darf auf die API zugreifen (CORS)")
    void corsAllowsFrontendOrigin() throws Exception {
        when(recipeService.getRecipes()).thenReturn(List.of());

        mockMvc.perform(get("/api/recipes")
                        .header(HttpHeaders.ORIGIN, FRONTEND_ORIGIN))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONTEND_ORIGIN));
    }

    @Test
    @DisplayName("Eine fremde Origin wird von CORS mit 403 abgelehnt")
    void corsRejectsUnknownOrigin() throws Exception {
        mockMvc.perform(get("/api/recipes")
                        .header(HttpHeaders.ORIGIN, "http://example.com"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(recipeService);
    }
}