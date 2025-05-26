package com.example.demo

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class FruitRepositorySpringbootJpaTest @Autowired constructor (val fruitRepository: FruitRepositorySpringBootJpa) {


    @BeforeEach
    fun setup() {
        fruitRepository.deleteAll()
    }

    @Test
    fun `should crud fruit`(): Unit  {
        val data = Fruit(name = "Strawberry")
        val saved = fruitRepository.save(data)
        log.info("saved fruit: $saved")
        saved.id.shouldNotBeNull()

        fruitRepository.findByIdOrNull(saved.id!!)?.name shouldBe "Strawberry"

        fruitRepository.findAllByName("Strawberry").size shouldBe 1

        fruitRepository.deleteAll()

        fruitRepository.findByIdOrNull(saved.id!!).shouldBeNull()



    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(FruitRepositorySpringbootJpaTest::class.java)
    }
}

