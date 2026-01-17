package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration
import kotlin.jvm.java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CourseControllerIntgTest {
    @LocalServerPort
    private var port: Int = 0
    lateinit var webTestClient: WebTestClient
    @Autowired
    lateinit var instructorRepository: InstructorRepository
    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .responseTimeout(Duration.ofSeconds(10))
            .build()

        instructorRepository.deleteAll()
        courseRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    // POST /courses
    @Test
    fun addCourse() {
        val instructor = instructorRepository.findAll().first()

        val courseDTO = CourseDTO(
            null,
            "Kotlin & Spring course",
            "Development",
            instructor.id
        )

        val savedCourseDTO = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("Course created is : $savedCourseDTO")

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    // GET /courses
    @Test
    fun retrieveAllCourses() {
        val courseDTOs = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs : $courseDTOs")

        Assertions.assertEquals(3, courseDTOs!!.size)
    }

    // GET courses by name
    @Test
    fun retrieveAllCourses_Byname() {
        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("course_name", "Spring")
            .toUriString()

        val courseDTOs = webTestClient.get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courses with name containing 'Spring' : $courseDTOs")
        Assertions.assertEquals(2, courseDTOs!!.size)
    }

    // PUT /courses/{courseId}
    @Test
    fun updateCourse() {
        val instructor = instructorRepository.findAll().first()

        // existing course
        val course = Course(
            null,
            "build backend app with SpringBoot & Kotlin",
            "Development",
            instructor
        )

        courseRepository.save(course)

        val updatedCourseDTO = CourseDTO(
            null,
            "Kotlin Design Patterns",
            "Design"
        )

        // simulate put request
        val updatedCourse = webTestClient.put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("updated course : $updatedCourse")

        Assertions.assertEquals("Kotlin Design Patterns", updatedCourse!!.name)
    }

    // DELETE /courses/{courseId}
    @Test
    fun deleteCourse() {
        val instructor = instructorRepository.findAll().first()

        val course = Course(
            null,
            "Kotlin & Spring",
            "Development",
            instructor
        )

        courseRepository.save(course)

        webTestClient.delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent

        println("deleted course : $course")
    }
}
