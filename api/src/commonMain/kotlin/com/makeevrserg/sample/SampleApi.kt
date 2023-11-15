package com.makeevrserg.sample

/**
 * This sample api will bla bla
 */
interface SampleApi {
    /**
     * This custom random function will bla bla
     */
    fun getRandomInteger(): Int
}

/**
 * To create implementation of our api call this function
 */
fun createSampleApi(): SampleApiImpl = SampleApiImpl()
