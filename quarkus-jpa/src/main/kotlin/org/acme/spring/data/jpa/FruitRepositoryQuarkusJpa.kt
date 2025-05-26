package org.acme.spring.data.jpa

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.*

@Entity
data class Fruit(@Id
                 @GeneratedValue
                 val id: Long? = null,
                 @Column
                 @field:JsonProperty("name")
                 var name: String,
                 )

interface FruitRepositoryQuarkusJpa : CrudRepository<Fruit, Long> {
    fun findAllByName(name: String): List<Fruit>
}








/**
 * Wrong extension copied from Spring...
 */
fun <T, ID> CrudRepository<T, ID>.findByIdOrNull(id: ID): T? = findById(id).orElse(null)