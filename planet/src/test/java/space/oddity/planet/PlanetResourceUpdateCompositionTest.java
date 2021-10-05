package space.oddity.planet;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import space.oddity.planet.dto.CreatePlanetDTO;
import space.oddity.planet.entities.Element;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PlanetResourceUpdateCompositionTest extends AbstractRunner {
  @Test
  void planetUpdateEmptyComposition() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of())
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .body("composition.size()", is(0))
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(Map.of(Element.K, 3333, Element.BK, 1234))
        .patch("/planet/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("name", equalTo("Earth"))
        .body("gravity", is(9.807F))
        .body("composition.size()", is(2))
        .body("composition.K", is(3333))
        .body("composition.BK", is(1234));
  }

  @Test
  void planetUpdateWithComposition() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of(Element.H, 999999999L, Element.BK, 88888L))
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .body("composition.size()", is(2))
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(Map.of(Element.K, 3333, Element.BK, 1234))
        .patch("/planet/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("name", equalTo("Earth"))
        .body("gravity", is(9.807F))
        .body("composition.size()", is(3))
        .body("composition.H", is(999999999))
        .body("composition.K", is(3333))
        .body("composition.BK", is(1234));
  }

  @Test
  void planetUpdateUnknownCompositionIssue() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of())
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .body("composition.size()", is(0))
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(Map.of("NOTEXIST", 3333, Element.BK, -1))
        .patch("/planet/v1/" + id)
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  void planetUpdateNegativeCompositionIssue() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .name("Earth")
                    .composition(Map.of())
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .body("composition.size()", is(0))
            .and()
            .extract()
            .jsonPath()
            .getString("id");

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(Map.of(Element.K, 3333, Element.BK, -1))
        .patch("/planet/v1/" + id)
        .then()
        .assertThat()
        .statusCode(400)
        .body("error", is("Asked modification would result in negative number of element(s)"));
  }
}
