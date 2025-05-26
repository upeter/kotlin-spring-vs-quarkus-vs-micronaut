package com.example.demo

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class FruitControllerSpringbootCoTest @Autowired constructor(
    val webTestClient: WebTestClient
) {


    @Test
    fun `should crud entity`(): Unit {
        val created = webTestClient.post().uri("/fruits").bodyValue(Fruit(null, "Cherry")).exchange().run {
            expectStatus().isOk
            expectBody<Fruit>().returnResult().responseBody.apply {
                this?.id.shouldNotBeNull()
            }
        }!!

    }
}














//val updated = webTestClient.put().uri("/fruits/${created.id}").bodyValue(created.copy(name =  "Apple")).exchange().run {
//    expectStatus().isOk
//    expectBody<Fruit>().returnResult().responseBody.apply {
//        this?.id.shouldNotBeNull()
//        this?.name shouldBe "Apple"
//    }
//}
