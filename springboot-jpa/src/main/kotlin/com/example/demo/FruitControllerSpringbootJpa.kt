package com.example.demo

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RestController
class FruitControllerSpringbootJpa(val fruitRepository: FruitRepositorySpringBootJpa) {

    @GetMapping("/fruits")
    @ResponseBody
    fun all(): Iterable<Fruit> =
        fruitRepository.findAll()

    @GetMapping("/fruits/{id}")
    @ResponseBody
    fun getById(@PathVariable id: Long): Fruit =
        fruitRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")

    @PostMapping("/fruits", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    @Transactional
    fun create(@RequestBody fruit: Fruit): Fruit =
        fruitRepository.save(fruit)


    @PutMapping("/fruits/{id}")
    @ResponseBody
    @Transactional
    fun changeFruit(@PathVariable  id: Long, @RequestBody fruit:Fruit): Fruit =
        fruitRepository.findByIdOrNull(id)?.copy(name = fruit.name)?.let(fruitRepository::save)
                ?:throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")


}













//fruitRepository.findByIdOrNull(id)?.copy(name = fruit.name)?.let(fruitRepository::save)
//?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
