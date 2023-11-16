package com.makeevrserg.sample

import java.time.Instant

class JvmOnlyInterfaceImpl : JvmOnlyInterface {
    override fun getInstant(): Instant = Instant.now()
}
