package com.example

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.*
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
//import io.micronaut.transaction.annotation.Transactional
import io.micronaut.validation.Validated
import io.reactivex.rxjava3.core.Single
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import reactor.core.publisher.Mono
import java.util.*

@Controller
@Validated
open class FruitControllerMicronautCo(private val fruitRepository: FruitRepositoryMicronautCo) {

    @Get(uri = "/fruits", produces = [MediaType.APPLICATION_JSON])
    open fun all(): HttpResponse<Flow<Fruit>> = ok(fruitRepository.findAll())

    @Get(uri = "/fruits/{id}", produces = [MediaType.APPLICATION_JSON])
    suspend open fun byId(@PathVariable id: Long): HttpResponse<Fruit> {
        return fruitRepository.findById(id)?.let(::ok) ?:  notFound()
    }

    @Post(uri = "/fruits", produces = [MediaType.APPLICATION_JSON], consumes = [MediaType.APPLICATION_JSON])
    @Transactional
    suspend open fun create(@Body @Valid fruit: Fruit): HttpResponse<Fruit> {
        return fruitRepository.save(fruit).let(::ok)
    }

    @Put(uri = "/fruits/{id}", produces = [MediaType.APPLICATION_JSON], consumes = [MediaType.APPLICATION_JSON])
    @Transactional
    suspend fun update(@PathVariable id: Long, @Body @Valid fruit: Fruit): MutableHttpResponse<Fruit> =
        fruitRepository.findById(id)?.let { entity ->
            ok(fruitRepository.update(entity.copy(name = fruit.name)))
        } ?: notFound()


}