package com.firmenich.model

import java.util.stream.Collectors
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import kotlin.collections.ArrayDeque

@Entity
class ArithmeticExpression(
    @Id
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "expression")
    val expression: String = ""
) {
    val expressionWithOutSeparator = expression.replace(",", "")

    @Transient
    private val stack = ArrayDeque<String>()

    @Transient
    private var list = mutableListOf<String>()

    @Transient
    private val operationsMap = mutableMapOf<String, (Double, Double) -> Double>()

    /**with this mapping I try to implement the strategy pattern
     * avoiding nested if or switch/when statements
     */
    init {
        operationsMap["+"] = ::sum
        operationsMap["-"] = ::sub
        operationsMap["/"] = ::div
        operationsMap["*"] = ::mult
    }

    fun evalExpression(): String {
        val str = expression.substring(0, expression.lastIndexOf(","))

        if (str.toCharArray().isEmpty()) {
            return "0"
        }

        list = str.split(",").toList().stream()
            .map { item -> item.toString() }
            .collect(Collectors.toList())

        list.forEach {
            if (it.numberOrOperator() is Double) {
                stack.addLast(it)
            } else {
                val result =
                    calculate(stack.removeLast().toDouble(), stack.removeLast().toDouble(), operationsMap[it]!!)
                stack.addFirst(result.toString())
            }
        }
        return stack.last()
    }

    private fun String.numberOrOperator(): Any {
        return when (val v = toDoubleOrNull()) {
            null -> this
            else -> v
        }
    }

    private fun calculate(x: Double, y: Double, operation: (Double, Double) -> Double): Double {
        return operation(x, y)
    }

    private fun sum(x: Double, y: Double) = x + y
    private fun mult(x: Double, y: Double) = x * y
    private fun sub(x: Double, y: Double) = x - y
    private fun div(x: Double, y: Double) = x / y
}