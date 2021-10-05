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
class PlanetResourceDetailsTest extends AbstractRunner {
  @Test
  void planetDetails() {
    String id =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(
                CreatePlanetDTO.builder()
                    .gravity(9.807)
                    .radius(6371.)
                    .weight(5.972e24)
                    .name("Earth")
                    .composition(Map.of(Element.H, 1234L, Element.ZN, 1111L))
                    .build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .statusCode(201)
            .body("id", notNullValue())
            .and()
            .extract()
            .body()
            .jsonPath()
            .getString("id");

    given()
        .when()
        .get("/planet/v1/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .body("id", notNullValue())
        .body("name", equalTo("Earth"))
        .body("gravity", equalTo(9.807F))
        .body("radius", equalTo(6371.F))
        .body("weight", equalTo(5.972e24F))
        .body("composition.size()", is(2))
        .body("composition.H", is(1234))
        .body("composition.ZN", is(1111));
  }

  @Test
  void planetDetailsNotFound() {
    given().when().get("/planet/v1/not-found").then().assertThat().statusCode(404);
  }
}
