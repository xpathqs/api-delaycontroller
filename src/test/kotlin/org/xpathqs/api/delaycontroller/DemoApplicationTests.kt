package org.xpathqs.api.delaycontroller

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.xpathqs.api.delaycontroller.controller.DelayController
import org.xpathqs.api.delaycontroller.dto.AddDelayRequest
import org.xpathqs.api.delaycontroller.dto.ConstantDelayBehaviour
import org.xpathqs.api.delaycontroller.dto.ResponseConfig

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	lateinit var mapper: ObjectMapper

	@LocalServerPort
	var port = 0

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	val url : String
		get() = "http://localhost:$port"

	@Test
	fun contextLoads() {
		assertThat(
			restTemplate.getForObject("$url/test2", String::class.java),
		).isEqualTo("hellow1")
	}

	@Test
	fun addDelay() {
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		val json = mapper.writeValueAsString(AddDelayRequest(
			equals = "test",
			delay = ConstantDelayBehaviour(1000),
			response = ResponseConfig(300, "{}")
		))

		println(
			json
		)

		val obj = mapper.readValue<AddDelayRequest>(json, AddDelayRequest::class.java)

		println(obj.equals)

		val headers = HttpHeaders()
		headers.setContentType(MediaType.APPLICATION_JSON);
		val request = HttpEntity<String>(json, headers)

		restTemplate.postForEntity(
			"$url/delay/add", request, String::class.java
		)
		assertThat(
			restTemplate.getForObject("$url/test", String::class.java),
		).isEqualTo("{}")

		assertThat(
			restTemplate.getForObject("$url/test2", String::class.java),
		).isEqualTo("hellow2")
	}
}
