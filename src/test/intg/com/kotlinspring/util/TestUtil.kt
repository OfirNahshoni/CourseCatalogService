package com.kotlinspring.util

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(
        null,
        "Kotlin & SpringBoot",
        "Development",
        instructor
    ),
    Course(
        null,
        "SpringBoot Services & Docker",
        "Development",
        instructor
    ),
    Course(
        null,
        "Micro Services Architecture",
        "Design",
        instructor
    )
)

fun instructorEntityList() = listOf(
    Instructor(null, "Ofir Nahshoni"),
    Instructor(null, "Ofir the First"),
    Instructor(null, "Ofir Nahshoni"),
    Instructor(null, "Ofifir")
)

fun courseDTO(
    id: Int? = null,
    name: String = "Micro Services & Kafka Architecture",
    category: String = "Design",
    instructorId: Int? = null
) = CourseDTO(id, name, category, instructorId)

fun instructorDTO(
    id: Int? = null,
    name: String = "Ofir Nahshoni"
) = InstructorDTO(id, name)

fun instructorEntity(
    id: Int? = null,
    name: String = "Ofir Nahshoni"
) = Instructor(id, name)
