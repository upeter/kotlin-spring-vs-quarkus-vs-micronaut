package com.example

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.notFound
import io.micronaut.http.HttpResponse.ok
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import org.springframework.data.repository.findByIdOrNull
import javax.transaction.Transactional
import javax.validation.Valid


@Controller("/fruits")
@Validated
class FruitControllerMicronautJpa {

    @Inject
    lateinit var fruitRepository: FruitRepositoryMicronautJpa

    @Get(uri = "/", produces = [MediaType.APPLICATION_JSON])
    fun all(): HttpResponse<Iterable<Fruit>> =
        fruitRepository.findAll().let(::ok)

    @Get(uri = "/{id}", produces = [MediaType.APPLICATION_JSON])
    fun getById(@PathVariable id: Long): HttpResponse<Fruit> =
        fruitRepository.findByIdOrNull(id)?.let(::ok) ?: notFound()

    @Post(uri = "/", produces = [MediaType.APPLICATION_JSON], consumes = [MediaType.APPLICATION_JSON])
    @Transactional
    fun create(@Body @Valid fruit: Fruit): HttpResponse<Fruit> =
        fruitRepository.save(fruit).let(::ok)

    @Put(uri = "/{id}", produces = [MediaType.APPLICATION_JSON], consumes = [MediaType.APPLICATION_JSON])
    @Transactional
    fun update(@PathVariable id:Long, @Body @Valid fruit: Fruit): HttpResponse<Fruit> =
        fruitRepository.findByIdOrNull(id)?.apply{this.name = fruit.name}?.let{ok(fruitRepository.save(it))}
            ?: notFound()


}





















//@Put(uri = "/{id}", produces = [MediaType.APPLICATION_JSON], consumes = [MediaType.APPLICATION_JSON])
//@Transactional
//fun update(@PathVariable id:Long, @Body @Valid fruit: Fruit): HttpResponse<Fruit> =
//    fruitRepository.findByIdOrNull(id)?.apply{this.name = fruit.name}?.let{ok(fruitRepository.save(it))} ?: notFound()
