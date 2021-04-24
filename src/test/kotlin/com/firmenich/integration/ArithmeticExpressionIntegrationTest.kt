package com.firmenich.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
class ArithmeticExpressionIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `integration test`() {
        val id = 1
        val expression = listOf("3", "2", "+", "5", "*", "6", "4", "*", "/", "10", "+")
        val result = "11.041"

        // when
        mockMvc.post("/expressions/$id") {
            contentType = MediaType.TEXT_PLAIN
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isOk() }
            content { "Expression created successfully" }
        }

        expression.forEach {
            if (it.toIntOrNull() != null) {
                // when
                mockMvc.post("/expressions/$id/push_value") {
                    contentType = MediaType.APPLICATION_JSON
                    content = it
                    accept = MediaType.APPLICATION_JSON
                }.andExpect {
                    // then
                    status { isOk() }
                }
            } else {
                mockMvc.post("/expressions/$id/push_operator") {
                    contentType = MediaType.APPLICATION_JSON
                    content = it
                    accept = MediaType.APPLICATION_JSON
                }.andExpect {
                    // then
                    status { isOk() }
                }
            }
        }

        // when
        mockMvc.get("/expressions/$id") {
            contentType = MediaType.TEXT_PLAIN
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isOk() }
            content { "32+5*64*/10+" }
        }

        // when
        mockMvc.get("/expressions/$id/eval") {
            contentType = MediaType.TEXT_PLAIN
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isOk() }
            content { result }
        }


    }
}