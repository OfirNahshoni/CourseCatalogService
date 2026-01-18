package com.kotlinspring.repository

import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import com.kotlinspring.util.instructorEntityList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.stream.Stream

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CourseRepositoryIntgTest {
    @Autowired
    lateinit var courseRepository: CourseRepository
    @Autowired
    lateinit var instructorRepository: InstructorRepository

    companion object {
        @Container
        val postgres = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:16.3-alpine")).apply {
            withDatabaseName("courses_db")
            withUsername("postgres")
            withPassword("password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }

        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("Spring", 2),
                Arguments.arguments("Services", 2),
                Arguments.arguments("Docker", 1)
            )
        }
    }

    @BeforeEach
    fun setup() {
        instructorRepository.deleteAll()
        courseRepository.deleteAll()

        val instructor = instructorEntity()
        val instructors = instructorEntityList()
        instructorRepository.save(instructor)
        instructorRepository.saveAll(instructors)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("Spring")
        println("[findByNameContaining()] courses saved : $courses")
        assertEquals(2, courses.size)
    }

    @Test
    fun findCoursesbyName() {
        val courses = courseRepository.findCoursesbyName("Services")
        println("[findCoursesbyName()] courses found : $courses")
        assertEquals(2, courses.size)
    }

    // test multiple sets of data
    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesbyName_approach2(name: String, expectedResultSize: Int) {
        val courses = courseRepository.findCoursesbyName(name)
        println("[findCoursesbyName_approach2()] courses found : $courses")
        assertEquals(expectedResultSize, courses.size)
    }
}