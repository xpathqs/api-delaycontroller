package org.xpathqs.api.delaycontroller.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.xpathqs.api.delaycontroller.component.DelayConfig
import org.xpathqs.api.delaycontroller.dto.AddDelayRequest


@RestController
@RequestMapping("delay")
class DelayController(
    @Autowired
    private val config: DelayConfig
) {
    private val log = LoggerFactory.getLogger(DelayController::class.java)

    @PostMapping("/add")
    fun addDelay(@RequestBody request: AddDelayRequest) {
        log.info("add new delay $request")
        config.delays.add(request)
    }

    @PostMapping("/disableAll")
    fun disableAll() {
        log.info("disable all endpoints of the service")
        config.disableAll = true
    }

    @PostMapping("/clear")
    fun clear() {
        log.info("All delays was removed")
        config.delays.clear()
        config.disableAll = false
    }
}