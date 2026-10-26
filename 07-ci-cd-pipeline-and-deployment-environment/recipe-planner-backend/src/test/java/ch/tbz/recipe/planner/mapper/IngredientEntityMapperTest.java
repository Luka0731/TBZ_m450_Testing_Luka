package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * SoftAssertions sammeln alle fehlgeschlagenen Assertions und melden sie erst bei {@code softly.assertAll()}.
 * So sieht man bei einem Mapper auf einen Blick, welche Felder alle falsch gemappt werden,
 * statt nach dem ersten Fehler abzubrechen.
**/
@DisplayName("Aufgabe 1 – Mapper-Tests IngredientEntityMapper (SoftAssertions)")
class IngredientEntityMapperTest {
    private final IngredientEntityMapper mapper = Mappers.getMapper(IngredientEntityMapper.class);
    private Ingredient tomato;
    private Ingredient pasta;
    private IngredientEntity tomatoEntity;
    private IngredientEntity pastaEntity;



    // |----- setup -----|

    @BeforeEach
    void setUp() {
        UUID tomatoId = UUID.randomUUID();
        UUID pastaId = UUID.randomUUID();
        tomato = new Ingredient(tomatoId, "Tomato", "The big ones", Unit.PIECE, 5);
        pasta = new Ingredient(pastaId, "Spaghetti", "al dente", Unit.GRAMM, 500);
        tomatoEntity = new IngredientEntity(tomatoId, "Tomato", "The big ones", Unit.PIECE, 5);
        pastaEntity = new IngredientEntity(pastaId, "Spaghetti", "al dente", Unit.GRAMM, 500);
    }



    // |----- entityToDomain -----|

    @Test
    @DisplayName("entityToDomain übernimmt alle Felder der Entity")
    void entityToDomainMapsAllFields() {
        Ingredient result = mapper.entityToDomain(pastaEntity);

        // ohne Objekt macht das Weiterprüfen keinen Sinn, deshalb hier eine normale (harte) Assertion
        assertThat(result).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getId()).as("id").isEqualTo(pastaEntity.getId());
        softly.assertThat(result.getName()).as("name").isEqualTo("Spaghetti");
        softly.assertThat(result.getComment()).as("comment").isEqualTo("al dente");
        softly.assertThat(result.getUnit()).as("unit").isEqualTo(Unit.GRAMM);
        softly.assertThat(result.getAmount()).as("amount").isEqualTo(500);
        softly.assertAll();
    }

    @Test
    @DisplayName("entityToDomain übernimmt auch leere Felder (null bzw. 0)")
    void entityToDomainKeepsEmptyFields() {
        Ingredient result = mapper.entityToDomain(new IngredientEntity());

        assertThat(result).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getId()).as("id").isNull();
        softly.assertThat(result.getName()).as("name").isNull();
        softly.assertThat(result.getComment()).as("comment").isNull();
        softly.assertThat(result.getUnit()).as("unit").isNull();
        softly.assertThat(result.getAmount()).as("amount").isZero();
        softly.assertAll();
    }

    @Test
    @DisplayName("entityToDomain mit null liefert null")
    void entityToDomainWithNullReturnsNull() {
        assertThat(mapper.entityToDomain(null)).isNull();
    }



    // |----- domainToEntity -----|

    @Test
    @DisplayName("domainToEntity übernimmt alle Felder der Domain-Klasse")
    void domainToEntityMapsAllFields() {
        IngredientEntity result = mapper.domainToEntity(tomato);

        assertThat(result).isNotNull();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getId()).as("id").isEqualTo(tomato.getId());
        softly.assertThat(result.getName()).as("name").isEqualTo("Tomato");
        softly.assertThat(result.getComment()).as("comment").isEqualTo("The big ones");
        softly.assertThat(result.getUnit()).as("unit").isEqualTo(Unit.PIECE);
        softly.assertThat(result.getAmount()).as("amount").isEqualTo(5);
        softly.assertAll();
    }

    @Test
    @DisplayName("domainToEntity mit null liefert null")
    void domainToEntityWithNullReturnsNull() {
        assertThat(mapper.domainToEntity(null)).isNull();
    }

    @Test
    @DisplayName("Domain -> Entity -> Domain ergibt wieder ein gleiches, aber neues Objekt")
    void domainToEntityAndBackKeepsAllValues() {
        Ingredient result = mapper.entityToDomain(mapper.domainToEntity(tomato));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).as("gleiche Werte").isEqualTo(tomato);
        softly.assertThat(result).as("neues Objekt").isNotSameAs(tomato);
        softly.assertAll();
    }

    @ParameterizedTest(name = "Unit {0}")
    @EnumSource(Unit.class)
    @DisplayName("jede Unit wird in beide Richtungen richtig gemappt")
    void unitIsMappedInBothDirections(Unit unit) {
        tomato.setUnit(unit);
        tomatoEntity.setUnit(unit);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(mapper.domainToEntity(tomato).getUnit()).as("domainToEntity").isEqualTo(unit);
        softly.assertThat(mapper.entityToDomain(tomatoEntity).getUnit()).as("entityToDomain").isEqualTo(unit);
        softly.assertAll();
    }



    // |----- Listen -----|

    @Test
    @DisplayName("entitiesToDomains mappt jedes Element und behält die Reihenfolge")
    void entitiesToDomainsMapsAllElements() {
        List<Ingredient> result = mapper.entitiesToDomains(List.of(tomatoEntity, pastaEntity));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).as("Anzahl").hasSize(2);
        softly.assertThat(result).as("Elemente und Reihenfolge").containsExactly(tomato, pasta);
        softly.assertAll();
    }

    @Test
    @DisplayName("domainsToEntities mappt jedes Element und behält die Reihenfolge")
    void domainsToEntitiesMapsAllElements() {
        List<IngredientEntity> result = mapper.domainsToEntities(List.of(pasta, tomato));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).as("Anzahl").hasSize(2);
        softly.assertThat(result).as("Elemente und Reihenfolge").containsExactly(pastaEntity, tomatoEntity);
        softly.assertAll();
    }

    @Test
    @DisplayName("leere Listen bleiben leer, null bleibt null")
    void listsWithEmptyOrNullInput() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(mapper.entitiesToDomains(List.of())).as("entitiesToDomains leer").isEmpty();
        softly.assertThat(mapper.domainsToEntities(List.of())).as("domainsToEntities leer").isEmpty();
        softly.assertThat(mapper.entitiesToDomains(null)).as("entitiesToDomains null").isNull();
        softly.assertThat(mapper.domainsToEntities(null)).as("domainsToEntities null").isNull();
        softly.assertAll();
    }
}