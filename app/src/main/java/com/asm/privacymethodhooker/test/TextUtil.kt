package com.asm.privacymethodhooker.test

import android.os.Looper
import android.util.Log
import com.asm.privacymethodhooker.App
import com.asm.privacymethodhooker.LogProtos
import com.asm.privacymethodhooker.SVR_PRIV_KEY
import com.asm.privacymethodhooker.SVR_PUB_KEY
import glog.android.Glog
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.log


object TextUtil {
    fun getOriginTxt(): String {
        return "origin text"
    }


    @JvmStatic
    fun insetBeforeTest() {
        System.out.println("doing.....")
    }

    @JvmStatic
    fun insetAfterTest() {
        System.out.println("doing.....")
    }


    @JvmStatic
    fun checkThread() {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            Log.e("Check_UIThread", Log.getStackTraceString(Throwable("在主线程进行耗时操作")))
        }

    }

    var deep = 0

    @JvmStatic
    fun methodIn(methodName: String) {
        deep++
        write(">$deep-$methodName")
    }

    @JvmStatic
    fun methodOut(startTime: Long) {
        write("<$deep-" + (System.currentTimeMillis() - startTime))
        deep--
    }


    private val nonIncrementalGlog: Glog by lazy { // 重命名缓存文件的方式归档
        Glog.Builder(App.appContext)
            .protoName("glog-anr")
            .encryptMode(Glog.EncryptMode.AES) // 可选
            .key(SVR_PUB_KEY) // 可选
            .build()
    }

    fun write(log: String) {
        val bytes = serializeLog(log)
        nonIncrementalGlog.write(bytes)
    }

    fun readNonIncremental() {
        val logArchiveFiles = arrayListOf<String>()
        // 如果缓存中日志条数 > 0 或 缓存中日志体积 > 0 则 flush 缓存中日志到归档文件, 返回归档相关状态信息
        nonIncrementalGlog.getArchiveSnapshot(logArchiveFiles, 0, 0)
        val inBuf = ByteArray(Glog.getSingleLogMaxLength())
        val endIndex = logArchiveFiles.size - 1
        var startIndex = logArchiveFiles.size - 1 - 9
        if (startIndex < 1) {
            startIndex = logArchiveFiles.size - 1
        }
        Log.v("tag", "缓存文件格式 ${logArchiveFiles.size}  $startIndex - $endIndex")
        logArchiveFiles.forEachIndexed { index, file ->
            if (index in startIndex..endIndex) {
                Log.v("tag", "读取文件 $file")
                nonIncrementalGlog.openReader(file, SVR_PRIV_KEY).use { reader ->
                    while (true) {
                        val count = reader.read(inBuf)
                        if (count < 0) { // 读取结束
                            break
                        } else if (count == 0) { // 破损恢复
                            continue
                        }
                        val outBuf = ByteArray(count)
                        System.arraycopy(inBuf, 0, outBuf, 0, count)
//                        deserializeLog(outBuf)
                    }
                }
            }
        }
    }

    fun clearLog() {
        nonIncrementalGlog.removeAll()
    }


    private val seq = AtomicLong()

    private fun serializeLog(log: String): ByteArray {
        return LogProtos.Log.newBuilder()
            .setLogLevel(LogProtos.Log.Level.INFO)
            .setSequence(seq.getAndIncrement())
            .setTimestamp(System.currentTimeMillis().toString())
            .setPid(android.os.Process.myPid())
            .setTid(Thread.currentThread().id.toString())
            .setTag("GlogSample")
            .setMsg(log)
            .build()
            .toByteArray()
    }

    private fun deserializeLog(bytes: ByteArray) {
        val log = LogProtos.Log.parseFrom(bytes)
        println(log.msg)
    }

    private fun LogProtos.Log.string(): String {
        return "Log{" +
                "sequence=" + sequence +
                ", timestamp='" + timestamp + '\'' +
                ", logLevel=" + logLevel +
                ", pid=" + pid +
                ", tid='" + tid + '\'' +
                ", tag='" + tag + '\'' +
                ", msg='" + msg + '\'' +
                '}'
    }
}