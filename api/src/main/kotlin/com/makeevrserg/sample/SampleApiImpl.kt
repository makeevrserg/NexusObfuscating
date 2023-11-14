package com.makeevrserg.sample

import kotlin.random.Random

class SampleApiImpl : SampleApi {
    override fun getRandomInteger(): Int {
        return Random.nextInt()
    }
}
