package org.acme.spring.data.jpa

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.*

@Path("/fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
class FruitControllerQuarkusJpa(private val fruitRepository: FruitRepositoryQuarkusJpa) {
    @GET
    @Produces("application/json")
    fun findAll(): Iterable<Fruit> {
        return fruitRepository.findAll()
    }

    @DELETE
    @Path("{id}")
    fun delete(id: Long) {
        fruitRepository.deleteById(id)
    }

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    fun findByName(name: String): List<Fruit> {
        return fruitRepository.findAllByName(name)
    }


    @POST
    @Path("/")
    @Transactional
    fun create(fruit: Fruit): Fruit {
        return fruitRepository.save(fruit)
    }

    @PUT
    @Path("/{id}")
    @Transactional
    fun change(id: Long, fruit: Fruit): Fruit {
        return fruitRepository.findByIdOrNull(id)?.copy(name = fruit.name)?.let(fruitRepository::save)
                ?: throw WebApplicationException("Fruit with id of $id does not exist.", 404)
    }


}













//@PUT
//@Path("/{id}")
//@Transactional
//fun change(id: Long, fruit: Fruit): Fruit {
//    val fruit = fruitRepository.findByIdOrNull(id)?.apply { this.name = fruit.name }
//        ?:  throw WebApplicationException("Fruit with id of $id does not exist.", 404) 
//    return fruitRepository.save(fruit)
//}