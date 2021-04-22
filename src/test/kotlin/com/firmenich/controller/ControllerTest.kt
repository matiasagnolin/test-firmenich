package com.firmenich.controller

import com.firmenich.dto.ArithmeticExpressionIdsDTO
import com.firmenich.model.ArithmeticExpression
import com.firmenich.services.ArithmeticExpressionReadService
import com.firmenich.services.ArithmeticExpressionWriteService
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
class ControllerTest {

    @MockBean
    private lateinit var arithmeticExpressionService: ArithmeticExpressionReadService

    @MockBean
    private lateinit var arithmeticExpressionWriteService: ArithmeticExpressionWriteService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `get expressions ids list should return 200`() {
        // given
        val list: List<Int> = listOf(1, 2, 3)

        whenever(arithmeticExpressionService.getExpressions())
            .thenReturn(ArithmeticExpressionIdsDTO(list))
        // when
        mockMvc.get("/expressions") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isOk() }
            content { json("""{"expressionIds":[1,2,3]}""") }
        }
    }

    @Test
    fun `get expression by id should return 200`() {
        // given
        val expression = "11+"

        whenever(arithmeticExpressionService.getExpressionById(1))
            .thenReturn(Optional.of(ArithmeticExpression(id = 1, expression = expression)))
        // when
        mockMvc.get("/expressions/1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isOk() }
            content { json("""{"expression": 11+}""") }
        }
    }

    @Test
    fun `get expression by id should return 404`() {
        // given
        val id = 1

        whenever(arithmeticExpressionService.getExpressionById(id))
            .thenReturn(Optional.empty())
        // when
        mockMvc.get("/expressions/1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isNotFound() }
            content { "Expression not found" }
        }
    }

    @Test
    fun `get expression by id should return 409`() {
    }

    @Test
    fun `create expression should return 200`() {
        // given
        val arithmeticExpression = ArithmeticExpression(1, "11+")

        whenever(arithmeticExpressionWriteService.saveExpression(arithmeticExpression))
            .thenReturn(arithmeticExpression)
        // when
        mockMvc.post("/expressions/1") {
            contentType = MediaType.TEXT_PLAIN
            content = "11+"
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isOk() }
            content { "Expression created successfully" }
        }
    }

    @Test
    fun `create expression should return 409`() {
    }

    @Test
    fun `delete expression by id should return 200`() {
    }

    @Test
    fun `delete expression by id should return 404`() {
    }

    @Test
    fun `expression evaluation should return 200`() {
    }

    @Test
    fun `expression evaluation should return 404`() {
    }

    @Test
    fun `expression evaluation should return 409`() {
    }

    @Test
    fun `operation push value should return 200`() {
    }

    @Test
    fun `operation push value should return 400`() {
    }

    @Test
    fun `operation push value should return 404`() {
    }

    @Test
    fun `operation push operator should return 200`() {
    }

    @Test
    fun `operation push operator should return 400`() {
    }

    @Test
    fun `operation push operator should return 404`() {
    }

    @Test
    fun `operation push value should return 409`() {
    }

}