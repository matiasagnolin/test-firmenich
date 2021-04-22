package com.firmenich.services

import com.firmenich.dto.ArithmeticExpressionIdsDTO
import com.firmenich.model.ArithmeticExpression
import com.firmenich.repositories.ExpressionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.stream.Collectors

@Service
class ArithmeticExpressionReadService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var repository: ExpressionRepository


    fun getExpressions(): ArithmeticExpressionIdsDTO {
        val expressions = repository.findAll().stream()
            .map { item -> item.id }
            .collect(Collectors.toList())

        log.info("returning all expressions size ${expressions.size}")
        return ArithmeticExpressionIdsDTO(expressions)
    }

    fun getExpressionById(id: Int): Optional<ArithmeticExpression> {
        log.info("returning returning expression with ID $id")
        return repository.findById(id)
    }

}