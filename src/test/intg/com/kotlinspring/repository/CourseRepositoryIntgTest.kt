package com.kotlinspring.repository

import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@SpringBootTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {
    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
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

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("Spring", 2),
                Arguments.arguments("Services", 2),
                Arguments.arguments("Docker", 1)
            )
        }
    }
}