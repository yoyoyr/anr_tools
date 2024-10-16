package com.asm.privacymethodhooker.test

import androidx.annotation.Keep
import androidx.annotation.WorkerThread
import kotlinx.coroutines.awaitAll

class Child : Parent() {

    override fun testAnnota() {
        super.testAnnota()
    }

    fun shouldCheck(){
        System.out.println("111")
        System.out.println("111")
        System.out.println("111")
        System.out.println("111")
        Thread.sleep(100)
        System.out.println("111")
        System.out.println("111")
        System.out.println("111")
    }
}