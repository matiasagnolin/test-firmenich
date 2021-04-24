package com.firmenich.services

import com.firmenich.controllers.exceptions.OperatorNotValidAtThisPointException
import com.firmenich.model.ArithmeticExpression
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class ArithmeticExpressionPushService {

    @Autowired
    private lateinit var arithmeticExpressionWriteService: ArithmeticExpressionWriteService

    @Autowired
    private lateinit var arithmeticExpressionReadService: ArithmeticExpressionReadService


    fun pushValuebyId(id: Int, value: String): ArithmeticExpression {
        value.toIntOrNull() ?: throw IllegalArgumentException("Invalid value.")
        val newArithmeticExpression = getNewArithmeticExpression(id, "$value,")
        return arithmeticExpressionWriteService
            .updateExpression(newArithmeticExpression)
    }

    fun pushOperatorbyId(id: Int, value: String): ArithmeticExpression {
        val operators: Set<String> = setOf("+", "-", "*", "/")

        if (!operators.any { it == value }) {
            throw IllegalArgumentException("Invalid operator.")
        }

        val newArithmeticExpression = getNewArithmeticExpression(id, "$value,")
        try {
            newArithmeticExpression.evalExpression()
        } catch (ex: NoSuchElementException) {
            throw OperatorNotValidAtThisPointException(
                "Invalid operation (The expression cannot receive an operator at this point).")
        }

        return arithmeticExpressionWriteService
            .updateExpression(newArithmeticExpression)
    }

    private fun getNewArithmeticExpression(id: Int, value: String): ArithmeticExpression {
        val arithmeticExpression = arithmeticExpressionReadService.getExpressionById(id)
        val newExpression = arithmeticExpression.expression + value
        return ArithmeticExpression(id, newExpression)
    }

}