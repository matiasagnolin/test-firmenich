package com.firmenich.repositories

import com.firmenich.model.ArithmeticExpression
import org.springframework.data.jpa.repository.JpaRepository

interface ExpressionRepository :
    JpaRepository<ArithmeticExpression, Int>