package com.firmenich.services

import com.firmenich.controllers.exceptions.ExpressionAlreadyExists
import com.firmenich.controllers.exceptions.ExpressionNotFoundException
import com.firmenich.model.ArithmeticExpression
import com.firmenich.repositories.ExpressionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ArithmeticExpressionWriteService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var repository: ExpressionRepository

    fun saveExpression(arithmeticExpression: ArithmeticExpression): ArithmeticExpression {
        if (repository.existsById(arithmeticExpression.id)) {
            throw ExpressionAlreadyExists("Expression already exists.")
        }
        return repository.save(arithmeticExpression)
    }

    fun deleteExpression(id: Int) {
        if (!repository.existsById(id)) {
            throw ExpressionNotFoundException("Expression not found.")
        }
        repository.deleteById(id)
    }
}