package space.oddity.planet;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import space.oddity.planet.dto.CreatePlanetDTO;
import space.oddity.planet.dto.command.ElementCommandDTO;
import space.oddity.planet.entities.Element;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PlanetCommandResourceTest extends AbstractRunner {
  private static Stream<Arguments> provideInputError() {
    return Stream.of(
        Arguments.of("[{\"element\": \"UN\", \"value\":3, \"command\": \"ADD\"}]"),
        Arguments.of("[{\"value\":3, \"command\": \"ADD\"}]"),
        Arguments.of("[{\"element\": \"AL\", \"value\":3, \"command\": \"BIM\"}]"),
        Arguments.of("[{\"element\": \"AL\", \"value\":3}]"),
        Arguments.of("[{\"element\": \"AL\", \"value\":3}]"),
        Arguments.of("[{\"element\": \"AL\", \"command\": \"ADD\"}]"));
  }

  @Test
  void planetNotFound() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(List.of(new ElementCommandDTO(ElementCommandDTO.CommandType.ADD, Element.AC, 3L)))
        .put("/planet/command/v1")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  void emptyWorldAddElement() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(CreatePlanetDTO.builder().gravity(9.807).name("Earth").build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(List.of(new ElementCommandDTO(ElementCommandDTO.CommandType.ADD, Element.AC, 3L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("composition.size()", is(1))
        .body("composition.AC", is(3));
  }

  @Test
  void addNewElement() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of(Element.BK, 1L))
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(List.of(new ElementCommandDTO(ElementCommandDTO.CommandType.ADD, Element.AC, 3L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("composition.size()", is(2))
        .body("composition.AC", is(3))
        .body("composition.BK", is(1));
  }

  @Test
  void addExistingElement() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of(Element.AL, 1L))
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(List.of(new ElementCommandDTO(ElementCommandDTO.CommandType.ADD, Element.AL, 3L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("composition.size()", is(1))
        .body("composition.AL", is(4));
  }

  @Test
  void subNewElement() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of(Element.AL, 1L))
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(List.of(new ElementCommandDTO(ElementCommandDTO.CommandType.SUB, Element.K, 3L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(400)
        .body("error", is("K element is not present and can't be subtracted"));

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("planet/v1/" + id)
        .then()
        .body("composition.AL", is(1))
        .body("composition.K", nullValue());
  }

  @Test
  void subTooFewElement() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of(Element.AL, 7L))
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(List.of(new ElementCommandDTO(ElementCommandDTO.CommandType.SUB, Element.AL, 8L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(400)
        .body("error", is("Asked modification would result in negative number of element(s)"));

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("planet/v1/" + id)
        .then()
        .body("composition.AL", is(7));
  }

  @Test
  void subElement() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of(Element.AL, 21L, Element.BK, 9L))
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(List.of(new ElementCommandDTO(ElementCommandDTO.CommandType.SUB, Element.AL, 8L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("composition.size()", is(2))
        .body("composition.AL", is(13))
        .body("composition.BK", is(9));
  }

  @Test
  void addAndSubElementOk() {
    String id = createPlanet();

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(
            List.of(
                new ElementCommandDTO(ElementCommandDTO.CommandType.SUB, Element.AL, 8L),
                new ElementCommandDTO(ElementCommandDTO.CommandType.ADD, Element.AL, 7L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("composition.size()", is(2))
        .body("composition.AL", is(0))
        .body("composition.BK", is(9));
  }

  @Test
  void addAndSubElementIssue() {
    String id = createPlanet();

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(
            List.of(
                new ElementCommandDTO(ElementCommandDTO.CommandType.ADD, Element.AL, 7L),
                new ElementCommandDTO(ElementCommandDTO.CommandType.SUB, Element.AL, 8L),
                new ElementCommandDTO(ElementCommandDTO.CommandType.SUB, Element.AL, 1L)))
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(400)
        .body("error", is("Asked modification would result in negative number of element(s)"));

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("planet/v1/" + id)
        .then()
        .body("composition.size()", is(2))
        .body("composition.AL", is(1))
        .body("composition.BK", is(9));
  }

  @ParameterizedTest
  @MethodSource("provideInputError")
  void inputError(String body) {
    String id = createPlanet();

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(body)
        .put("/planet/command/v1/" + id)
        .then()
        .assertThat()
        .statusCode(400);
  }

  private String createPlanet() {
    return given()
        .contentType(ContentType.JSON)
        .when()
        .body(
            CreatePlanetDTO.builder()
                .gravity(9.807)
                .name("Earth")
                .composition(Map.of(Element.AL, 1L, Element.BK, 9L))
                .build())
        .post("/planet/v1")
        .then()
        .assertThat()
        .statusCode(201)
        .body("id", notNullValue())
        .and()
        .extract()
        .jsonPath()
        .getString("id");
  }
}
