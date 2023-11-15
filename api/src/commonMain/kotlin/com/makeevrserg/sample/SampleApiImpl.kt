package com.makeevrserg.sample

class SampleApiImpl : SampleApi {
    val myCustomField = 10
    val testClass = TestClass(1, "Hello world!")
    override fun getRandomInteger(): Int {
        return testClass.namedField1
    }
}
