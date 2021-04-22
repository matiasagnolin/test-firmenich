package com.firmenich.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ArithmeticExpressionIdsDTO(@JsonProperty("expressionIds") val ids: List<Int>)
