package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import com.kotlinspring.util.instructorDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import tools.jackson.databind.ObjectMapper

@WebMvcTest(controllers = [InstructorController::class])
class InstructorControllerUnitTest {
    @Autowired
    lateinit var mockMvc: MockMvc
    @MockkBean
    lateinit var instructorServiceMockk: InstructorService

    private fun Any.toJson(): String {
        val objectMapper = ObjectMapper()

        return objectMapper.writeValueAsString(this)
    }

    // POST /instructors
    @Test
    fun addInstructor() {
        val instructorDTO = InstructorDTO(null, "Moshe Rabenu")

        every { instructorServiceMockk.createInstructor(any()) } returns instructorDTO(1, "Moshe Rabenu")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(instructorDTO.toJson())
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Moshe Rabenu"))
    }

    // GET /instructors
    @Test
    fun retrieveAllInstructors() {
    }
}
