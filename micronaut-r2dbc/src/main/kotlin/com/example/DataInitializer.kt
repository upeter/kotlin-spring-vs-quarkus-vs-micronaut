package com.example

import io.micronaut.context.annotation.Requires
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

@Singleton
@Requires(notEnv = ["mock"])
class DataInitializer(private val fruitRepository: FruitRepositoryMicronautCo) {

    @EventListener//does not support `suspend`
    fun onStartUp(e: ServerStartupEvent) {
        log.info("starting data initialization at StartUpEvent: $e")

        runBlocking {
            val deleteAll = fruitRepository.deleteAll()
            log.info("deleted posts: $deleteAll")

            val data = listOf(
                Fruit(name = "Banana"),
                Fruit(name = "Cherry")
            )
            data.forEach { log.debug("saving: $it") }
            fruitRepository.saveAll(data)
                .onEach { log.debug("saved post: $it") }
                .onCompletion { log.debug("completed.") }
                .flowOn(Dispatchers.IO)
                .launchIn(this);
        }

        log.info("data initialization is done...")
    }

    companion object DataInitializer {
        private val log = LoggerFactory.getLogger(DataInitializer::class.java)
    }

}
