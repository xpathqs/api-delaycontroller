package org.xpathqs.api.delaycontroller.component

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class DelayFilter(
    @Autowired
    val config: DelayConfig
) : Filter {
    val log = LoggerFactory.getLogger(DelayFilter::class.java)

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        if(config.disableAll) {
            httpResponse.setResponse(500)
            return
        }

        val url = httpRequest.requestURL.toString().substringAfter("//").substringAfter("/")
        log.info("url: $url")
        val resp = config.processUrl(url)?.response

        if (resp != null) {
            log.info("Response set in filter to ${resp.body} code: ${resp.code}")
            httpResponse.setResponse(resp.code, resp.body)
        } else {
            log.info("default process")
            chain.doFilter(request, response)
        }
    }

    private fun HttpServletResponse.setResponse(code: Int, body: String = "") {
        resetBuffer()
        status = code
        setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        outputStream.print(body)
        flushBuffer()
    }
}