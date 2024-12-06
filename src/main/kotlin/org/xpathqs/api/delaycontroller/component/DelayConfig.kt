package org.xpathqs.api.delaycontroller.component

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.xpathqs.api.delaycontroller.dto.AddDelayRequest
import org.xpathqs.api.delaycontroller.dto.ConstantDelayBehaviour
import org.xpathqs.api.delaycontroller.dto.InternalDelayBehavior
import org.xpathqs.api.delaycontroller.dto.NoDelayBehavior
import org.xpathqs.api.delaycontroller.dto.RandomDelayBehaviour

@Component
class DelayConfig {
    val delays = mutableListOf<AddDelayRequest>()
    var disableAll = false

    private val log = LoggerFactory.getLogger(DelayFilter::class.java)

    fun processUrl(url: String): AddDelayRequest? {
        delays.forEach {
            log.info(it.toString())
        }

        delays.firstOrNull {
            it.isApplicable(url)
        }?.apply {
            log.info("Filter applyed for $this")
            when (delay) {
                is ConstantDelayBehaviour -> {
                    val sleepFor = delay.ms
                    log.info("Sleep for $sleepFor")
                    Thread.sleep(sleepFor)
                }

                is RandomDelayBehaviour -> {
                    val sleepFor = (delay.minMs..delay.maxMs).random()
                    log.info("Sleep for $sleepFor")
                    Thread.sleep(sleepFor)
                }

                is InternalDelayBehavior -> {
                    val sleepFor = Long.MAX_VALUE
                    log.info("Sleep for $sleepFor")
                    Thread.sleep(sleepFor)
                }

                is NoDelayBehavior -> {}
            }
            return this
        }
        return null
    }
}