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
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InstructorControllerIntgTest {
    @LocalServerPort
    private var port: Int = 0
    lateinit var webTestClient: WebTestClient
    @Autowired
    lateinit var instructorRepository: InstructorRepository

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
