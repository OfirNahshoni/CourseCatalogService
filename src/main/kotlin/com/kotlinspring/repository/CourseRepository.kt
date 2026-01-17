package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
    fun findByNameContaining(name: String): List<Course>
    @Query(value = "SELECT * FROM COURSES where name like %?1%", nativeQuery = true)
    fun findCoursesbyName(name: String): List<Course>
}
