package com.example.demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FruitControllerSpringBootJpaTest @Autowired constructor(
    val fruitRepository: FruitRepositorySpringBootJpa,
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @BeforeEach
    fun setup(): Unit {
        fruitRepository.deleteAll()
    }

    @Test
    fun `should crud entity`() {
        val created = mockMvc.perform(
            post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fruit(null, "Orange").toJson())
        )
            .andExpect(status().isOk)
            .andReturn().fromBody<Fruit>()
        created.id.shouldNotBeNull()

        mockMvc.get("/fruits/${created.id}").andReturn().fromBody<Fruit>() shouldBe created
        mockMvc.get("/fruits").andReturn().fromBody<List<Fruit>>().size shouldBe 1

        val updated = mockMvc.perform(
            put("/fruits/${created.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fruit(created.id, "Apple").toJson())
        )
            .andExpect(status().isOk)
            .andReturn().fromBody<Fruit>()
        created.id shouldBe updated.id
        updated.name shouldBe "Apple"



    }

    @Test
    fun `should get not found for non existing entity`() {
        mockMvc.get("/fruits/99").andExpect {
            status { isNotFound() }
        }
    }


    private inline fun <reified T> String.fromJson() = objectMapper.readValue<T>(this)
    private inline fun <reified T> T.toJson(): String = objectMapper.writeValueAsString(this)
    private inline fun <reified T> MvcResult.fromBody() = this.response.contentAsString.fromJson<T>()

}




//val updated = mockMvc.perform(
//    put("/fruits/${created.id}")
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(created.copy(name = "Apple").toJson())
//)
//    .andExpect(status().isOk)
//    .andReturn().fromBody<Fruit>()
//updated.id shouldBe created.id
//updated.name shouldBe "Apple"