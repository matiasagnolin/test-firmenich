package com.firmenich.controllers


import com.firmenich.controllers.exceptions.ExpressionNotFoundException
import com.firmenich.dto.ArithmeticExpressionIdsDTO
import com.firmenich.model.ArithmeticExpression
import com.firmenich.services.ArithmeticExpressionReadService
import com.firmenich.services.ArithmeticExpressionWriteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/expressions"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ExpressionsController {

    @Autowired
    private lateinit var readingService: ArithmeticExpressionReadService

    @Autowired
    private lateinit var writingService: ArithmeticExpressionWriteService

    @GetMapping
    fun getExpressionsIds(): ArithmeticExpressionIdsDTO {
        return readingService.getExpressions()
    }

    @GetMapping("/{expressionId}")
    fun getExpressionById(@PathVariable(value = "expressionId") id: String): String {
        val response = readingService.getExpressionById(id.toInt())
        return response.expression
    }

    @PostMapping("/{expressionId}")
    fun createExpression(
        @PathVariable(value = "expressionId") id: String,
        @RequestBody arithmeticExpression: String
    ): ResponseEntity<String> {
        writingService.saveExpression(ArithmeticExpression(id.toInt(), arithmeticExpression))
        return ResponseEntity("Expression created successfully", HttpStatus.OK)
    }

    @DeleteMapping("/{expressionId}")
    fun deleteExpression(
        @PathVariable(value = "expressionId") id: String
    ): ResponseEntity<String> {
        writingService.deleteExpression(id.toInt())
        return ResponseEntity("Expression deleted successfully.", HttpStatus.OK)
    }

    @GetMapping("/{expressionId}/eval")
    fun evalExpression(
        @PathVariable(value = "expressionId") id: String
    ): String {
        return readingService.evalExpression(id.toInt())
    }
}
