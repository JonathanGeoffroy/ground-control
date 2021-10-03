package space.oddity.planet;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.oddity.planet.dto.CreatePlanetDTO;
import space.oddity.planet.entities.Element;
import space.oddity.planet.entities.Planet;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;

@QuarkusTest
class PlanetResourceTest {
  @Inject Random random;

  @Transactional
  static void empty() {
    for (var p : Planet.findAll().list()) {
      p.delete();
    }
  }

  @BeforeEach
  void mock() {
    random = Mockito.mock(Random.class);
    QuarkusMock.installMockForType(random, Random.class);
  }

  @AfterEach
  void resetAfter() {
    empty();
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
    Mockito.when(random.nextInt(Element.values().length))
        .thenReturn(3, Element.HE.ordinal(), Element.S.ordinal(), Element.ZN.ordinal());
    Mockito.when(random.nextLong()).thenReturn(1L, 2L, 3L);

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
        .body("weight", nullValue())
        .body("composition.size()", is(3))
        .body("composition.HE", is(1))
        .body("composition.S", is(2))
        .body("composition.ZN", is(3));
  }

  @Test
  void planetCreationRandomComposition() {
    Mockito.when(random.nextInt(Element.values().length))
        .thenReturn(3, Element.S.ordinal(), Element.S.ordinal(), Element.S.ordinal());
    Mockito.when(random.nextLong()).thenReturn(1L, 2L, 3L);

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(CreatePlanetDTO.builder().gravity(9.807).name("Earth").build())
        .post("/planet/v1")
        .then()
        .assertThat()
        .body("id", notNullValue())
        .body("composition.size()", is(1))
        .body("composition.S", is(1));
  }

  @Test
  void planetCreationProvidedCompositionEmpty() {

    given()
        .contentType(ContentType.JSON)
        .when()
        .body(CreatePlanetDTO.builder().gravity(9.807).name("Earth").composition(Map.of()).build())
        .post("/planet/v1")
        .then()
        .assertThat()
        .body("id", notNullValue())
        .body("composition.size()", is(0));

    Mockito.verify(random, Mockito.never()).nextInt(anyInt());
    Mockito.verify(random, Mockito.never()).nextLong();
  }

  @Test
  void planetCreationProvidedComposition() {

    given()
            .contentType(ContentType.JSON)
            .when()
            .body(CreatePlanetDTO.builder().gravity(9.807).name("Earth").composition(Map.of(Element.H, 1234L)).build())
            .post("/planet/v1")
            .then()
            .assertThat()
            .body("id", notNullValue())
            .body("composition.size()", is(1))
            .body("composition.H", is(1234));

    Mockito.verify(random, Mockito.never()).nextInt(anyInt());
    Mockito.verify(random, Mockito.never()).nextLong();
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
        .body("gravity", equalTo(9.807F))
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
