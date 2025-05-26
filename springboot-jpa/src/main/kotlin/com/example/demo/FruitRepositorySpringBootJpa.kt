package com.example.demo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Entity
data class Fruit(
    @Id
    @GeneratedValue
    val id: Long? = null,
    @Column
    val name: String,
)

@Repository
interface FruitRepositorySpringBootJpa : CrudRepository<Fruit, Long> {
    fun findAllByName(name: String): List<Fruit>
}

