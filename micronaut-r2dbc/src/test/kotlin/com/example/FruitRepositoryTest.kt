package com.example

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micronaut.context.env.Environment
import io.micronaut.data.r2dbc.operations.R2dbcOperations
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.r2dbc.spi.Connection
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@MicronautTest(environments = [Environment.TEST], startApplication = false, transactional = false)
class FruitRepositoryTest() {

    @Inject
    lateinit var fruitRepository: FruitRepositoryMicronautCo

    @Inject
    lateinit var template: R2dbcOperations

    @Test
    fun `persist fruit`(): Unit = runBlocking {
        val data = Fruit(name = "Strawberry")
        val saved = fruitRepository.save(data)
        log.debug("saved fruit: $saved")
        saved.id shouldNotBe null

        val found = fruitRepository.findById(saved.id!!)
        log.debug("found fruit: $found")
        found!!.name shouldBe "Strawberry"


        val upated = fruitRepository.update(saved.copy(name = "Apple"))
        log.debug("found fruit: $upated")
        upated.id  shouldBe saved.id
    }


    companion object {
        private val log: Logger = LoggerFactory.getLogger(FruitRepositoryTest::class.java)
    }

//    @BeforeEach
//    fun beforeEach():Unit = runBlocking{
//        //fruitRepository.deleteAll()
//        log.debug("call beforeEach...")
//        val sql = "delete from fruits";
//
//        val latch = CountDownLatch(1)
//        Mono.from(
//                this.template.withConnection { conn: Connection ->
//                    Mono.from(conn.beginTransaction())
//                        .then(Mono.from(conn.createStatement(sql).execute())
//                            .flatMap { Mono.from(it.rowsUpdated) }
//                            .doOnNext { log.debug("deleted rows: $it ") }
//                        ).then(Mono.from(conn.commitTransaction()))
//                        .doOnError { Mono.from(conn.rollbackTransaction()).then() }
//                }
//            )
//            .log()
//            .doOnTerminate { latch.countDown() }
//            .subscribe(
//                { data -> log.debug("deleted posts: $data ") },
//                { error -> log.error("error of cleaning posts: $error") },
//                { log.info("done") }
//            )
//
//        latch.await(5000, TimeUnit.MILLISECONDS)
//    }

}

