package com.example

import io.micronaut.data.annotation.Repository
import io.micronaut.transaction.annotation.TransactionalAdvice
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import org.springframework.data.repository.CrudRepository

@Entity
data class Fruit(
    @field:Id
    @field:GeneratedValue
    val id: Long? = null,
    @field:Column
    var name: String,
)

@Repository
@TransactionalAdvice
interface FruitRepositoryMicronautJpa : CrudRepository<Fruit, Long> {
    fun findAllByName(name: String): List<Fruit>
}
