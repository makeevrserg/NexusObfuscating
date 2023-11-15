@file:Suppress("Filename")
@file:JvmName("Main")

package com.makeevrserg.sample

import com.makeevrserg.sample.SampleApiKt.createSampleApi

fun main() {
    val sampleApi: SampleApi = createSampleApi()
    println("Hello world: ${sampleApi.randomInteger}")
}
