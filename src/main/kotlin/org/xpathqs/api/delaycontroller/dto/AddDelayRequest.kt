package org.xpathqs.api.delaycontroller.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.slf4j.LoggerFactory

data class ResponseConfig(
    val code: Int,
    val body: String
)

data class AddDelayRequest(
    val equals: String? = null,
    val startsWith: String? = null,
    val endsWith: String? = null,
    val contains: String? = null,

    val delay: DelayBehaviour = NoDelayBehavior(),
    val response: ResponseConfig? = null
) {
    private val log = LoggerFactory.getLogger(AddDelayRequest::class.java)

    fun isApplicable(url: String): Boolean {
        if (equals != null && url == equals) {
            log.info("Apply equals for $url")
            return true
        }
        if (startsWith != null && url.startsWith(startsWith)) {
            log.info("Apply startsWith for $url")
            return true
        }
        if (endsWith != null && url.endsWith(endsWith)) {
            log.info("Apply endsWith for $url")
            return true
        }
        if (contains != null && url.contains(contains)) {
            log.info("Apply contains for $url")
            return true
        }

        log.info("No filter for $url")

        return false
    }
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ConstantDelayBehaviour::class, name = "constant"),
    JsonSubTypes.Type(value = RandomDelayBehaviour::class, name = "random"),
    JsonSubTypes.Type(value = InternalDelayBehavior::class, name = "internal")
)
sealed class DelayBehaviour(val type: String)

class ConstantDelayBehaviour(
    val ms: Long
) : DelayBehaviour(type = "constant")

class RandomDelayBehaviour(
    val minMs: Long,
    val maxMs: Long,
) : DelayBehaviour(type = "random")

class InternalDelayBehavior() : DelayBehaviour("internal")

class NoDelayBehavior() : DelayBehaviour("nodelay")