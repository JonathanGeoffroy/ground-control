package space.oddity.planet;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import space.oddity.planet.dto.CreatePlanetDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PlanetResourcePaginationTest extends AbstractRunner {
  @Test
  void emptyWorld() {
    given().when().get("/planet/v1").then().statusCode(200).body("", hasSize(0));
  }

  @Test
  void pagination() {
    for (double i = 0.; i < 10.; i++) {
      CreatePlanetDTO create =
          CreatePlanetDTO.builder()
              .name(String.format("planet-%s", (int) i))
              .gravity(i)
              .radius(2d * i)
              .weight(3d * i)
              .build();

      given()
          .contentType(ContentType.JSON)
          .when()
          .body(create)
          .post("/planet/v1")
          .then()
          .assertThat()
          .statusCode(201);
    }

    given()
        .contentType(ContentType.JSON)
        .when()
        .queryParam("size", 3)
        .get("/planet/v1")
        .then()
        .assertThat()
        .statusCode(200)
        .body("", hasSize(3))
        .body("[0].name", equalTo("planet-9"))
        .body("[1].name", equalTo("planet-8"))
        .body("[2].name", equalTo("planet-7"));

    given()
        .contentType(ContentType.JSON)
        .when()
        .queryParam("size", 5)
        .queryParam("index", 1)
        .get("/planet/v1")
        .then()
        .assertThat()
        .statusCode(200)
        .body("", hasSize(5))
        .body("[0].name", equalTo("planet-4"))
        .body("[1].name", equalTo("planet-3"))
        .body("[2].name", equalTo("planet-2"))
        .body("[3].name", equalTo("planet-1"))
        .body("[4].name", equalTo("planet-0"));
  }
}
