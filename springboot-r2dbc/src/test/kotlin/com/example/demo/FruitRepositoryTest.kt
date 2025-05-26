package com.example.demo

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest
@ExtendWith(SpringExtension::class)
class FruitRepositoryTest @Autowired constructor(
        val fruitRepository: FruitRepositorySpringbootCo
) {

    @Test
    fun `should crud new fruit`() = runBlocking {
        //create
        val newFruit = fruitRepository.save(Fruit(null, "Banana"))
        var foundFruit = fruitRepository.findById(newFruit.id!!)
        foundFruit shouldBe newFruit

        //update
        val updatedFruit = newFruit.copy(name = "Dancing Banana")
        fruitRepository.save(updatedFruit)
        foundFruit = fruitRepository.findById(newFruit.id!!)
        foundFruit shouldBe updatedFruit

        //delete
        fruitRepository.delete(updatedFruit)
        fruitRepository.findById(newFruit.id!!) shouldBe null
    }


    companion object {
    }
}