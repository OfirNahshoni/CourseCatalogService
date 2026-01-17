package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(val instructorService: InstructorService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(@RequestBody instructorDTO: InstructorDTO)
    = instructorService.createInstructor(instructorDTO)

    @GetMapping
    fun retrieveAllInstructors(
        @RequestParam("instructor_name", required = true) instructorName: String?): List<InstructorDTO>
    = instructorService.retrieveAllInstructors(instructorName)

    @DeleteMapping("/{instructorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteInstructor(@PathVariable(value = "instructorId") instructorId: Int)
    = instructorService.deleteInstructor(instructorId)
}
