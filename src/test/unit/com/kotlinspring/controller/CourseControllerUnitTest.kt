package com.kotlinspring.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [CourseController::class])
class CourseControllerUnitTest {
    @Autowired
    lateinit var mockMvc: MockMvc
    @MockkBean
    lateinit var courseServiceMock: CourseService

    // helper for converting any type to json
    private fun Any.toJson(): String {
        val objectMapper = ObjectMapper()

        return objectMapper.writeValueAsString(this)
    }

    // POST /courses
    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(
            null,
            "Micro Services & Kafka Architecture",
            "Design",
            1
        )

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1, instructorId = 1)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseDTO.toJson())
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.id")
                    .value(1)
            )
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.name")
                    .value("Micro Services & Kafka Architecture")
            )
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.category")
                    .value("Design")
            )
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.instructorId")
                    .value(1)
            )
    }

    // NotBlank test for properties of CourseDTO
    @Test
    fun addCourse_validation() {
        val courseDTO = CourseDTO(null, "", "", null)

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(courseDTO.toJson())
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
    }

    // GET /courses
    @Test
    fun retrieveAllCourses() {
        val mockCoursesDTOs = listOf(
            courseDTO(id = 1),
            courseDTO(id = 2, "Kotlin & Spring", "Development"),
            courseDTO(id = 3, "micro services project", "Development")
        )

        // expected result
        every { courseServiceMock.retrieveAllCourses(any()) }.returnsMany(mockCoursesDTOs)

        // actual + assertions - andExpect()
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/courses"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Kotlin & Spring"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
    }

    // PUT /courses/{courseId}
    @Test
    fun updateCourse() {
        val course = CourseDTO(
            1,
            "Kotlin & Spring",
            "Development"
        )

        // expected result
        every { courseServiceMock.updateCourse(any(), any()) } returns courseDTO(id = 55,
            "Part 2 - Micro Services & Kafka Architecture", "Development")

        // updated course
        val updatedCourse = CourseDTO(
            55,
            "Part 2 - Micro Services & Kafka Architecture",
            "Development"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/courses/{courseId}", course.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "id": 55,
                        "name": "Part 2 - Micro Services & Kafka Architecture",
                        "category": "Development"
                    }
                """.trimIndent())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.id")
                    .value(55)
            )
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.name")
                    .value("Part 2 - Micro Services & Kafka Architecture")
            )
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath("$.category")
                    .value("Development")
            )
    }

    // Runtime Exception test for updateCourse
    @Test
    fun updateCourse_RuntimException() {
        val courseDTO = CourseDTO(null, "Kotlin & Kafka", "Development")
        val courseId = 1
        val errorMessage = "NO course found with this id : $courseId"

        every { courseServiceMock.updateCourse(any(), any()) } throws RuntimeException(errorMessage)

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/courses/{courseId}", courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(courseDTO.toJson())
        )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andExpect(MockMvcResultMatchers.content().string(errorMessage))
    }

    // DELETE /courses/{courseId}
    @Test
    fun deleteCourse() {
        every { courseServiceMock.deleteCourse(any()) } just runs

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/courses/{courseId}", 100))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}
