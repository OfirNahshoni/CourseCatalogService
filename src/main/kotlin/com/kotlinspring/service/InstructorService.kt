package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Instructor
import com.kotlinspring.exception.InstructorNotFoundException
import com.kotlinspring.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        // cast DTO into JPA object
        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        // save instructor to db
        instructorRepository.save(instructorEntity)

        // return instructorDTO to user
        return instructorEntity.let {
            InstructorDTO(it.id, it.name)
        }
    }

    fun retrieveAllInstructors(instructorName: String?): List<InstructorDTO> {
        val instructors = instructorName?.let {
            instructorRepository.findByName(instructorName)
        } ?: instructorRepository.findAll()

        return instructors
            .map {
                InstructorDTO(it.id, it.name)
            }
    }

    fun deleteInstructor(instructorId: Int) {
        val existingInstructor = instructorRepository.findById(instructorId)

        if (existingInstructor.isPresent) {
            existingInstructor.get()
                .let {
                    instructorRepository.deleteById(instructorId)
                }
        } else {
            throw InstructorNotFoundException("NO instructor found with this id : $instructorId")
        }
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }
}
