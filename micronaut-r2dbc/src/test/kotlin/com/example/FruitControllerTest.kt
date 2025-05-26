package com.example
import io.kotest.common.runBlocking
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.kotlin.http.retrieveList
import io.micronaut.kotlin.http.retrieveObject
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*


@MicronautTest(environments = [Environment.TEST], startApplication = true, transactional = false)
class FruitControllerTest {

    lateinit var blockingClient: BlockingHttpClient

    @Inject
    lateinit var fruitRepository: FruitRepositoryMicronautCo


    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @BeforeEach
    fun setup():Unit = runBlocking{
        blockingClient = client.toBlocking()
        fruitRepository.deleteAll()
    }

    @Test
    fun `should crud and entity`() {
        val created = blockingClient.retrieveObject<Fruit>(HttpRequest.POST("/fruits", Fruit(null, "Peach")))
        created.id.shouldNotBeNull()
        blockingClient.retrieveObject<Fruit>(HttpRequest.GET("/fruits/${created.id}")) shouldBe created
        blockingClient.retrieveList<Fruit>(HttpRequest.GET("/fruits")).size shouldBe 1

        val updated = blockingClient.retrieveObject<Fruit>(HttpRequest.PUT("/fruits/${created.id}", created.copy(name =  "Apple")))
        updated.id shouldBe created.id
        blockingClient.retrieveObject<Fruit>(HttpRequest.GET("/fruits/${updated.id}")) shouldBe updated
        blockingClient.retrieveList<Fruit>(HttpRequest.GET("/fruits")).size shouldBe 1

    }



    @Test
    fun testFindNonExistingFruitReturns404() {
        val thrown = Assertions.assertThrows(
            HttpClientResponseException::class.java
        ) {
            blockingClient.retrieveObject<Fruit>(HttpRequest.GET("/fruits/99"))
        }
        Assertions.assertNotNull(thrown.response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }
}