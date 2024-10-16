package com.asm.privacymethodhooker

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.asm.privacymethodhooker.test.TextUtil.write
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import glog.android.Glog
import java.io.File
import java.util.concurrent.atomic.AtomicLong

object Work {

    var count = 0
    fun manyWork() {
        while (true) {
            write("many work start.......$count")
            100 * 300 + 1232 - 23123
//            Thread.sleep(50)
            count++
        }
    }

    private fun work() {
        100 * 300 + 1232 - 23123
//            Thread.sleep(50)
        count++
    }

    fun ioWork(file: File) {
        while (true) {
            file.appendText("abcd$count")
            count++
        }

    }

}
