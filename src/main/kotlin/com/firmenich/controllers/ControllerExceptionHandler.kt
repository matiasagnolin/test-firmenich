package com.firmenich.controllers

import com.firmenich.controllers.exceptions.ExpressionAlreadyExists
import com.firmenich.controllers.exceptions.ExpressionNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.WebRequest
import java.lang.IllegalArgumentException

@ControllerAdvice
@ResponseBody
class ControllerExceptionHandler {

    @ExceptionHandler(ExpressionNotFoundException::class)
    fun resourceNotFoundException(ex: ExpressionNotFoundException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgument(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ExpressionAlreadyExists::class)
    fun expressionAlreadyExists(ex: ExpressionAlreadyExists, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.CONFLICT)
    }
}