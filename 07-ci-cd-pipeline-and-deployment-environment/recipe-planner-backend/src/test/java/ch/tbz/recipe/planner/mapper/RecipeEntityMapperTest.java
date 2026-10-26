package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * SoftAssertions sammeln alle fehlgeschlagenen Assertions und melden sie erst bei {@code softly.assertAll()}.
 * So sieht man bei einem Mapper auf einen Blick, welche Felder alle falsch gemappt werden,
 * statt nach dem ersten Fehler abzubrechen.
**/
@DisplayName("Aufgabe 1 – Mapper-Tests RecipeEntityMapper (SoftAssertions)")
class RecipeEntityMapperTest {
    private final RecipeEntityMapper mapper = Mappers.getMapper(RecipeEntityMapper.class);
    private Ingredient tomato;
    private Ingredient pasta;
    private IngredientEntity tomatoEntity;
    private IngredientEntity pastaEntity;
    private Recipe spaghetti;
    private RecipeEntity spaghettiEntity;



    // |----- setup -----|

    @BeforeEach
    void setUp() {
        UUID tomatoId = UUID.randomUUID();
        UUID pastaId = UUID.randomUUID();
        UUID spaghettiId = UUID.randomUUID();
        tomato = new Ingredient(tomatoId, "Tomato", "The big ones", Unit.PIECE, 5);
        pasta = new Ingredient(pastaId, "Spaghetti", "al dente", Unit.GRAMM, 500);
        tomatoEntity = new IngredientEntity(tomatoId, "Tomato", "The big ones", Unit.PIECE, 5);
        pastaEntity = new IngredientEntity(pastaId, "Spaghetti", "al dente", Unit.GRAMM, 500);
        spaghetti = new Recipe(spaghettiId, "Spaghetti Bolognese", "Der Klassiker",
                "https://example.com/spaghetti.jpg", List.of(tomato, pasta));
        spaghettiEntity = new RecipeEntity(spaghettiId, "Spaghetti Bolognese", "Der Klassiker",
                "https://example.com/spaghetti.jpg", new ArrayList<>(List.of(tomatoEntity, pastaEntity)));
    }



    // |----- entityToDomain -----|

    @Test
    @DisplayName("entityToDomain übernimmt alle Felder der Entity inkl. Zutaten")
    void entityToDomainMapsAllFields() {
        Recipe result = mapper.entityToDomain(spaghettiEntity);

        // ohne Objekt macht das Weiterprüfen keinen Sinn, deshalb hier eine normale (harte) Assertion
        assertThat(result).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getId()).as("id").isEqualTo(spaghettiEntity.getId());
        softly.assertThat(result.getName()).as("name").isEqualTo("Spaghetti Bolognese");
        softly.assertThat(result.getDescription()).as("description").isEqualTo("Der Klassiker");
        softly.assertThat(result.getImageUrl()).as("imageUrl").isEqualTo("https://example.com/spaghetti.jpg");
        softly.assertThat(result.getIngredients()).as("ingredients").containsExactly(tomato, pasta);
        softly.assertAll();
    }

    @Test
    @DisplayName("entityToDomain übernimmt auch leere Felder (null)")
    void entityToDomainKeepsEmptyFields() {
        Recipe result = mapper.entityToDomain(new RecipeEntity());

        assertThat(result).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getId()).as("id").isNull();
        softly.assertThat(result.getName()).as("name").isNull();
        softly.assertThat(result.getDescription()).as("description").isNull();
        softly.assertThat(result.getImageUrl()).as("imageUrl").isNull();
        softly.assertThat(result.getIngredients()).as("ingredients").isNull();
        softly.assertAll();
    }

    @Test
    @DisplayName("entityToDomain mit null liefert null")
    void entityToDomainWithNullReturnsNull() {
        assertThat(mapper.entityToDomain(null)).isNull();
    }



    // |----- domainToEntity -----|

    @Test
    @DisplayName("domainToEntity übernimmt alle Felder der Domain-Klasse inkl. Zutaten")
    void domainToEntityMapsAllFields() {
        RecipeEntity result = mapper.domainToEntity(spaghetti);

        assertThat(result).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getId()).as("id").isEqualTo(spaghetti.getId());
        softly.assertThat(result.getName()).as("name").isEqualTo("Spaghetti Bolognese");
        softly.assertThat(result.getDescription()).as("description").isEqualTo("Der Klassiker");
        softly.assertThat(result.getImageUrl()).as("imageUrl").isEqualTo("https://example.com/spaghetti.jpg");
        softly.assertThat(result.getIngredients()).as("ingredients").containsExactly(tomatoEntity, pastaEntity);
        softly.assertAll();
    }

    @Test
    @DisplayName("domainToEntity mappt auch die Felder jeder einzelnen Zutat")
    void domainToEntityMapsIngredientFields() {
        RecipeEntity result = mapper.domainToEntity(spaghetti);

        assertThat(result.getIngredients()).hasSize(2);
        IngredientEntity ingredient = result.getIngredients().get(1);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(ingredient.getId()).as("ingredient.id").isEqualTo(pasta.getId());
        softly.assertThat(ingredient.getName()).as("ingredient.name").isEqualTo("Spaghetti");
        softly.assertThat(ingredient.getComment()).as("ingredient.comment").isEqualTo("al dente");
        softly.assertThat(ingredient.getUnit()).as("ingredient.unit").isEqualTo(Unit.GRAMM);
        softly.assertThat(ingredient.getAmount()).as("ingredient.amount").isEqualTo(500);
        softly.assertAll();
    }

    @Test
    @DisplayName("domainToEntity mit null liefert null")
    void domainToEntityWithNullReturnsNull() {
        assertThat(mapper.domainToEntity(null)).isNull();
    }

    @Test
    @DisplayName("Rezept ohne Zutaten: leere Liste bleibt leer, null bleibt null")
    void recipeWithoutIngredients() {
        Recipe emptyIngredients = new Recipe(UUID.randomUUID(), "Wasser", "", "", List.of());
        Recipe nullIngredients = new Recipe(UUID.randomUUID(), "Luft", "", "", null);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(mapper.domainToEntity(emptyIngredients).getIngredients()).as("leere Liste").isEmpty();
        softly.assertThat(mapper.domainToEntity(nullIngredients).getIngredients()).as("null").isNull();
        softly.assertAll();
    }

    @Test
    @DisplayName("Domain -> Entity -> Domain ergibt wieder ein gleiches, aber neues Objekt")
    void domainToEntityAndBackKeepsAllValues() {
        Recipe result = mapper.entityToDomain(mapper.domainToEntity(spaghetti));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).as("gleiche Werte").isEqualTo(spaghetti);
        softly.assertThat(result).as("neues Objekt").isNotSameAs(spaghetti);
        softly.assertThat(result.getIngredients()).as("neue Liste").isNotSameAs(spaghetti.getIngredients());
        softly.assertAll();
    }
}