package com.example.demo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class FruitControllerSpringbootCo(
    private val fruitRepository: FruitRepositorySpringbootCo
) {


    @GetMapping("/fruits")
    @ResponseBody
    suspend fun all(): Flow<Fruit> =
            fruitRepository.findAll()


    @GetMapping("/fruits/{id}")
    @ResponseBody
    suspend fun byId(@PathVariable("id") id: Long = 0) =
            fruitRepository.findById(id) ?: throw ResponseStatusException(NOT_FOUND, "entity not found")


    @PostMapping("/fruits", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    @Transactional
    suspend fun create(@RequestBody fruit: Fruit) =
        fruitRepository.save(fruit)


    @PutMapping("/fruits/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    @Transactional
    suspend fun update(@PathVariable id:Long, @RequestBody fruit: Fruit) =
        fruitRepository.findById(id)?.let { fruitRepository.save(it.copy(name = fruit.name)) } ?:
        throw ResponseStatusException(NOT_FOUND, "entity not found")

}
