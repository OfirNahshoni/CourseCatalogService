package com.kotlinspring.exceptionhandler

import com.kotlinspring.exception.InstructorNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import kotlin.collections.sorted

@Component
@ControllerAdvice
class GlobalErrorHandler: ResponseEntityExceptionHandler() {
    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        Companion.logger.error("MethodArgumentNotValidException observed : ${ex.message}", ex)

        val errors = ex.bindingResult.allErrors
            .map { error -> error.defaultMessage!! }
            .sorted()

        Companion.logger.info("errors : $errors")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        Companion.logger.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.message)
    }

    @ExceptionHandler(InstructorNotFoundException::class)
    fun handleAllExceptions(ex: InstructorNotValidException, request: WebRequest): ResponseEntity<Any> {
        Companion.logger.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }
}
