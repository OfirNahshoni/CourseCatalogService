package com.kotlinspring.repository

import com.kotlinspring.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository: CrudRepository<Instructor, Int> {
    // find all instructors by name (exact match)
    fun findByName(name: String): List<Instructor>
}
