package com.makeevrserg.sample

import java.time.Instant

interface JvmOnlyInterface {
    fun getInstant(): Instant
}

/**
 * To create implementation of our JvmOnlyInterface
 */
fun createJvmOnlyInterface(): JvmOnlyInterface = JvmOnlyInterfaceImpl()
