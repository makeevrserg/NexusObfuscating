@file:Suppress("Filename")
@file:JvmName("Main")

package com.makeevrserg.sample

fun main() {
    val sampleApi: SampleApi = SampleApiImpl()
    println("Hello world: ${sampleApi.getRandomInteger()}")
}
