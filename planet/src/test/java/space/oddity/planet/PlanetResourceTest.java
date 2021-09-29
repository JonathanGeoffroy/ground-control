package space.oddity.planet;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import space.oddity.planet.dto.CreatePlanetDTO;
import space.oddity.planet.entities.Planet;

import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PlanetResourceTest {
  @BeforeAll
  static void resetBefore() {
    empty();
  }

  @AfterEach
  void resetAfter() {
    empty();
  }
  @Transactional
  static void empty() {
    Planet.deleteAll();
  }

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

  @Test
  void planetCreation() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(CreatePlanetDTO.builder().gravity(9.807).name("Earth").build())
        .post("/planet/v1")
        .then()
        .assertThat()
        .body("id", notNullValue())
        .body("name", equalTo("Earth"))
        .body("gravity", equalTo(9.807F))
        .body("radius", nullValue())
        .body("weight", nullValue());
  }

  @Test
  void planetCreationAllFields() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(
            CreatePlanetDTO.builder()
                .gravity(9.807)
                .name("Earth")
                .weight(5.972e24)
                .radius(6371.)
                .build())
        .post("/planet/v1")
        .then()
        .assertThat()
        .statusCode(201)
        .header("Location", startsWith("http://localhost:8081/planet/v1/"))
        .body("id", notNullValue())
        .body("name", equalTo("Earth"))
        // .body("gravity", equalTo(9.807F))
        .body("radius", equalTo(6371.F))
        .body("weight", equalTo(5.972e24F));
  }

  @Test
  void planetCreationEmptyBody() {
    given().contentType(ContentType.JSON).when().body("").post("planet/v1").then().statusCode(400);
  }

  @Test
  void planetCreationWithoutName() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(CreatePlanetDTO.builder().gravity(9.8).radius(1.2).weight(2.2).build())
        .post("planet/v1")
        .then()
        .statusCode(400);
  }
}
