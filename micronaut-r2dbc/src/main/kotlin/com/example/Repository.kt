package com.example

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import io.micronaut.data.repository.reactive.ReactorCrudRepository
import io.micronaut.data.repository.reactive.RxJavaCrudRepository
import io.micronaut.serde.annotation.Serdeable

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface FruitRepositoryMicronautCo : CoroutineCrudRepository<Fruit, Long>, CoroutineJpaSpecificationExecutor<Fruit>

@Introspected
@Serdeable
@MappedEntity()
data class Fruit(
    @field:GeneratedValue
    @field:Id
    val id: Long? = null,
    val name: String)