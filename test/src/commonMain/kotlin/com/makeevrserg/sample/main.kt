@file:Suppress("Filename")
@file:JvmName("Main")

package com.makeevrserg.sample


fun main() {
    val sampleApi: SampleApi = createSampleApi()
    println("Hello world: ${sampleApi.getRandomInteger()}")
}
