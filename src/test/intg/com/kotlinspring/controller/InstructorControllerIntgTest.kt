package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.repository.InstructorRepository
import com.kotlinspring.util.instructorEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class InstructorControllerIntgTest {
    @LocalServerPort
    private var port: Int = 0
    lateinit var webTestClient: WebTestClient
    @Autowired
    lateinit var instructorRepository: InstructorRepository

    // test-conainers object
    companion object {
        @Container
        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:16.3-alpine")).apply {
            withDatabaseName("courses_db")
            withUsername("udemy_training_user")
            withPassword("ofir221")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
            registry.add("spring.datasource.username", postgresDB::getUsername)
            registry.add("spring.datasource.password", postgresDB::getPassword)
        }
    }

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .responseTimeout(Duration.ofSeconds(10))
            .build()

        instructorRepository.deleteAll()
        val instructors = instructorEntityList()
        instructorRepository.saveAll(instructors)
    }

    // POST /instructors
    @Test
    fun addInstructor() {
        val instructorDTO = InstructorDTO(null, "Ofir The First")

        val savedInstructorDTO = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        println("Instructor created is : $savedInstructorDTO")

        Assertions.assertTrue { savedInstructorDTO!!.id != null }
    }

    // GET /instructors (all)
    @Test
    fun retrieveAllInstructors() {
        val instructorDTOs = webTestClient.get()
            .uri("/v1/instructors")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        println("instructorDTOs : $instructorDTOs")

        Assertions.assertEquals(4, instructorDTOs!!.size)
    }

    // GET /instructors (by name)
    @Test
    fun retrieveAllInstructors_ByName() {
        val instructorDTOs = webTestClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/v1/instructors")
                    .queryParam("instructor_name", "Ofir Nahshoni")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBodyList(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        println("instructors with name 'Ofir Nahshoni' : $instructorDTOs")

        Assertions.assertEquals(2, instructorDTOs!!.size)
    }
}
