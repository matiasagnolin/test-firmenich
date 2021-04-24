package com.firmenich.services

import com.firmenich.controllers.exceptions.ExpressionAlreadyExists
import com.firmenich.controllers.exceptions.ExpressionNotFoundException
import com.firmenich.model.ArithmeticExpression
import com.firmenich.repositories.ExpressionRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.junit.Assert
import java.lang.IllegalArgumentException

/**
 * since service layer isn't that big I test the repository layer here too
 * and to avoid overhead with @DirtiesContext or sql cleaner scripts
 * I decided to use spring.jpa.hibernate.ddl-auto=create
 * and a different ID in each test. In case of other people work on this project too,
 * for maintainability reasons, the db must be clean before each test.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
class ArithmeticExpressionServiceTest {

    @Autowired
    private lateinit var repository: ExpressionRepository

    @Autowired
    private lateinit var arithmeticExpressionWriteService: ArithmeticExpressionWriteService

    @Autowired
    private lateinit var arithmeticExpressionReadService: ArithmeticExpressionReadService

    @Autowired
    private lateinit var arithmeticExpressionPushervice: ArithmeticExpressionPushService

    @Test
    fun `should save expression service successfully `() {
        // given
        val arithmeticExpression = ArithmeticExpression(1, "1242")

        // when
        val result = arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        // then
        Assert.assertEquals(arithmeticExpression.id, result.id)
    }

    @Test(expected = ExpressionAlreadyExists::class)
    fun `should throw ExpressionAlreadyExists`() {
        // given
        val arithmeticExpression = ArithmeticExpression(2, "1242")

        // when
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        Assert.fail()
    }

    @Test
    fun `should delete expression successfully`() {
        // given
        val arithmeticExpression = ArithmeticExpression(3, "1242")

        // when
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)
        arithmeticExpressionWriteService.deleteExpression(arithmeticExpression.id)

        // then
        Assert.assertFalse(repository.existsById(arithmeticExpression.id))
    }

    @Test(expected = ExpressionNotFoundException::class)
    fun `should throw ExpressionNotFoundException`() {
        // given
        val arithmeticExpression = ArithmeticExpression(4, "1242")

        // when
        arithmeticExpressionWriteService.deleteExpression(arithmeticExpression.id)

        Assert.fail()
    }

    @Test
    fun `should find all expressions`() {
        // given
        val arithmeticExpression = ArithmeticExpression(5, "1242")

        // when
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)
        val result = arithmeticExpressionReadService.getExpressions()

        // then
        Assert.assertTrue(result.ids.isNotEmpty())
    }

    @Test
    fun `should find expression by id`() {
        // given
        val arithmeticExpression = ArithmeticExpression(6, "1242")

        // when
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)
        val result = arithmeticExpressionReadService.getExpressionById(6)

        // then
        Assert.assertEquals(arithmeticExpression.id, result.id)
    }

    @Test
    fun `should evaluate expression correctly`() {
        // given
        var arithmeticExpression = ArithmeticExpression(7, "512+4*+3-")

        // when
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)
        var result = arithmeticExpressionReadService.evalExpression(arithmeticExpression.id)

        //then
        Assert.assertEquals("-14.0", result)
    }

    @Test(expected = NoSuchElementException::class)
    fun `evaluation should throw Exception`() {
        // given
        var arithmeticExpression = ArithmeticExpression(15, "512+4*+3--")

        // when
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)
        var result = arithmeticExpressionReadService.evalExpression(arithmeticExpression.id)

        //then
        Assert.fail()
    }

    @Test
    fun `should push value correctly`() {
        // given
        var arithmeticExpression = ArithmeticExpression(8, "32+5*64*/10+")
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        // when
        var result = arithmeticExpressionPushervice.pushValuebyId(8, "4")

        //then
        Assert.assertEquals("32+5*64*/10+4", result.expression)
    }

    @Test
    fun `should push operator correctly`() {
        // given
        var arithmeticExpression = ArithmeticExpression(9, "32+5*64*/10+")
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        // when
        var result = arithmeticExpressionPushervice.pushOperatorbyId(9, "-")

        //then
        Assert.assertEquals("32+5*64*/10+-", result.expression)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception for invalid value`() {
        // given
        var arithmeticExpression = ArithmeticExpression(10, "32+5*64*/10+")
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        // when
         arithmeticExpressionPushervice.pushValuebyId(10, "#")

        //then
        Assert.fail()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception for invalid operator`() {
        // given
        var arithmeticExpression = ArithmeticExpression(11, "32+5*64*/10+")
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        // when
        arithmeticExpressionPushervice.pushOperatorbyId(11, "=")

        //then
        Assert.fail()
    }

    @Test(expected = ExpressionNotFoundException::class)
    fun `push operator should throw exception expression not found`() {
        // given
        var arithmeticExpression = ArithmeticExpression(12, "32+5*64*/10+")
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        // when
         arithmeticExpressionPushervice.pushOperatorbyId(13, "/")

        //then
        Assert.fail()
    }

    @Test(expected = ExpressionNotFoundException::class)
    fun `push value should throw exception expression not found`() {
        // given
        var arithmeticExpression = ArithmeticExpression(13, "32+5*64*/10+")
        arithmeticExpressionWriteService.saveExpression(arithmeticExpression)

        // when
         arithmeticExpressionPushervice.pushValuebyId(14, "5")

        //then
        Assert.fail()
    }
}