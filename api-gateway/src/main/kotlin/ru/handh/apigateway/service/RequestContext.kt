package ru.handh.apigateway.service

import org.springframework.stereotype.Component

@Component
class RequestContext {
    companion object {
        private val context = ThreadLocal<Int>()

        fun get(): Int = context.get()

        fun set(value: Int) = context.set(value)
    }
}