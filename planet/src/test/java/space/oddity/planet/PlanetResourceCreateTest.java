package space.oddity.planet;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.oddity.planet.dto.CreatePlanetDTO;
import space.oddity.planet.entities.Element;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;

@QuarkusTest
class PlanetResourceCreateTest extends AbstractRunner {

  @Test
  void planetCreationEmptyBody() {
    given().contentType(ContentType.JSON).when().body("").post("planet/v1").then().statusCode(400);
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
        .body(
            CreatePlanetDTO.builder()
                .gravity(9.807)
                .name("Earth")
                .composition(Map.of(Element.H, 1234L))
                .build())
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
  void planetCreationWithoutName() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(CreatePlanetDTO.builder().gravity(9.8).radius(1.2).weight(2.2).build())
        .post("planet/v1")
        .then()
        .statusCode(400);
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
  void planetCreationUnknownCompositionIssue() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .body("{\"name\": \"Earth\", \"composition\": {\"UNKNOWN\": 3}}")
        .post("/planet/v1")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  void planetCreationNegativeCompositionIssue() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(
            CreatePlanetDTO.builder()
                .gravity(9.807)
                .name("Earth")
                .composition(Map.of(Element.K, 3333L, Element.BK, -1L))
                .build())
        .post("/planet/v1")
        .then()
        .assertThat()
        .statusCode(400)
        .body("error", is("Asked modification would result in negative number of element(s)"));
  }
}
