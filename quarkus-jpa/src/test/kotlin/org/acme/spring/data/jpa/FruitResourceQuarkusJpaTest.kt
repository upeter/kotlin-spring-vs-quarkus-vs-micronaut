package org.acme.spring.data.jpa

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.IsNot.not
import org.junit.jupiter.api.Test

@QuarkusTest
open class FruitResourceQuarkusJpaTest {

    //Not supported by integration test when building native image
//    @Inject
//    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should list all fruits`() {
        given()
            .`when`()["/fruits"]
            .then()
            .statusCode(200)
            .body(
                containsString("Cherry"),
                containsString("Apple"),
                containsString("Banana")
            )

        given()
            .`when`().delete("/fruits/1")
            .then()
            .statusCode(204)

        given()
            .`when`()["/fruits"]
            .then()
            .statusCode(200)
            .body(
                not(containsString("Cherry")),
                containsString("Apple"),
                containsString("Banana")
            )

        given()
            .accept("application/json")
            .contentType("application/json")
            .body(Fruit(null, "Orange").asJson())
            .`when`().post("/fruits")
            .then()
            .statusCode(200)
            .body(containsString("Orange"))
            .body("id", notNullValue())
            .extract().body().jsonPath().getString("id")

        given()
            .`when`()["/fruits"]
            .then()
            .statusCode(200)
            .body(
                not(containsString("Cherry")),
                containsString("Apple"),
                containsString("Orange")
            )
    }

    @Test
    fun `should change name`() {
        given()
            .`when`()["/fruits/name/BlaBla"]
            .then()
            .statusCode(200)
            .body("size()", `is`(0))

        given()
            .`when`()["/fruits/name/Strawberry"]
            .then()
            .statusCode(200)
            .body(
                containsString("Strawberry")
            )

        given()
            .`when`()["/fruits/name/Avocado"]
            .then()
            .statusCode(200)
            .body("size()", `is`(1))
            .body(containsString("Avocado"))

        given().accept("application/json")
            .contentType("application/json")
            .body(Fruit(4, "Avocad0").asJson())
            .`when`().put("/fruits/4")
            .then()
            .statusCode(200)
            .body(
                containsString("Avocad0")
            )
        given()
            .`when`()["/fruits/name/Avocad0"]
            .then()
            .statusCode(200)
            .body("size()", `is`(1))
            .body(
                containsString("Avocad0")
            )
            .extract().body().asPrettyString().also(::println)

    }

    fun <T : Any> T.asJson() = objectMapper.writeValueAsString(this)

    companion object {
        val objectMapper = ObjectMapper().apply {
//            val x = KotlinModule()
//            registerModule(KotlinModule())
        }
    }

}


@QuarkusIntegrationTest
class FruitResourceQuarkusJpaIT : FruitResourceQuarkusJpaTest()


