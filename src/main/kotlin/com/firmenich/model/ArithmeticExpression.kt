package com.firmenich.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ArithmeticExpression(
    @Id
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "expression")
    val expression: String = "",
)