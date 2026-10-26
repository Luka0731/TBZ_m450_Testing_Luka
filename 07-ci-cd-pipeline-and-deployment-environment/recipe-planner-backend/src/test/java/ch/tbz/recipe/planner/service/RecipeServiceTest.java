package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
@DisplayName("Zusatz – Service-Tests RecipeService (Mockito)")
class RecipeServiceTest {
    @Mock private RecipeEntityMapper recipeEntityMapper;
    @Mock private RecipeRepository recipeRepository;
    @InjectMocks private RecipeService recipeService;
    private Recipe lasagne;
    private Recipe spaghetti;
    private RecipeEntity lasagneEntity;
    private RecipeEntity spaghettiEntity;

    @BeforeEach
    void setUp() {
        UUID lasagneId = UUID.randomUUID();
        UUID spaghettiId = UUID.randomUUID();
        lasagne = new Recipe(lasagneId, "Lasagne al Forno", "Mit Bechamelsauce", "lasagne.jpg", List.of());
        spaghetti = new Recipe(spaghettiId, "Spaghetti Bolognese", "Der Klassiker", "spaghetti.jpg", List.of());
        lasagneEntity = new RecipeEntity(lasagneId, "Lasagne al Forno", "Mit Bechamelsauce", "lasagne.jpg", List.of());
        spaghettiEntity = new RecipeEntity(spaghettiId, "Spaghetti Bolognese", "Der Klassiker", "spaghetti.jpg", List.of());
    }



    @Test
    @DisplayName("getRecipes mappt alle Entities aus dem Repository")
    void getRecipesReturnsAllRecipes() {
        when(recipeRepository.findAll()).thenReturn(List.of(lasagneEntity, spaghettiEntity));
        when(recipeEntityMapper.entityToDomain(lasagneEntity)).thenReturn(lasagne);
        when(recipeEntityMapper.entityToDomain(spaghettiEntity)).thenReturn(spaghetti);

        List<Recipe> result = recipeService.getRecipes();

        assertEquals(List.of(lasagne, spaghetti), result);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getRecipes liefert eine leere Liste, wenn das Repository leer ist")
    void getRecipesReturnsEmptyList() {
        when(recipeRepository.findAll()).thenReturn(List.of());

        List<Recipe> result = recipeService.getRecipes();

        assertTrue(result.isEmpty());
        verifyNoInteractions(recipeEntityMapper);
    }

    @Test
    @DisplayName("getRecipeById liefert das gemappte Rezept")
    void getRecipeByIdReturnsRecipe() {
        when(recipeRepository.findById(lasagne.getId())).thenReturn(Optional.of(lasagneEntity));
        when(recipeEntityMapper.entityToDomain(lasagneEntity)).thenReturn(lasagne);

        Recipe result = recipeService.getRecipeById(lasagne.getId());

        assertEquals(lasagne, result);
        verify(recipeRepository, times(1)).findById(lasagne.getId());
    }

    @Test
    @DisplayName("getRecipeById mit unbekannter ID liefert null")
    void getRecipeByIdNotFound() {
        UUID unknownId = UUID.randomUUID();
        when(recipeRepository.findById(unknownId)).thenReturn(Optional.empty());

        Recipe result = recipeService.getRecipeById(unknownId);

        assertNull(result);
        // der Mapper bekommt null und gibt null zurück
        verify(recipeEntityMapper, times(1)).entityToDomain(null);
    }

    @Test
    @DisplayName("addRecipe mappt das Rezept, speichert es und gibt das gespeicherte Rezept zurück")
    void addRecipeSavesRecipe() {
        Recipe newRecipe = new Recipe(null, "Fried Rice", "Asiatischer Klassiker", "rice.jpg", List.of());
        RecipeEntity newEntity = new RecipeEntity(null, "Fried Rice", "Asiatischer Klassiker", "rice.jpg", List.of());
        RecipeEntity savedEntity = new RecipeEntity(UUID.randomUUID(), "Fried Rice", "Asiatischer Klassiker", "rice.jpg", List.of());
        Recipe savedRecipe = new Recipe(savedEntity.getId(), "Fried Rice", "Asiatischer Klassiker", "rice.jpg", List.of());
        when(recipeEntityMapper.domainToEntity(newRecipe)).thenReturn(newEntity);
        when(recipeRepository.save(newEntity)).thenReturn(savedEntity);
        when(recipeEntityMapper.entityToDomain(savedEntity)).thenReturn(savedRecipe);

        Recipe result = recipeService.addRecipe(newRecipe);

        assertEquals(savedRecipe, result);
        verify(recipeRepository, times(1)).save(newEntity);
    }
}