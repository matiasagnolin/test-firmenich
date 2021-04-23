package com.firmenich.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArithmeticExpressionPushService {

    @Autowired
    private lateinit var arithmeticExpressionWriteService: ArithmeticExpressionWriteService

    @Autowired
    private lateinit var arithmeticExpressionReadService: ArithmeticExpressionReadService


}