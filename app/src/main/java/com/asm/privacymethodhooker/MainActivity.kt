package com.asm.privacymethodhooker

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.asm.privacymethodhooker.net.ConnectReceiver
import com.asm.privacymethodhooker.test.TextUtil
import com.asm.privacymethodhooker.test.TextUtil.getOriginTxt
import com.asm.privacymethodhooker.test.TextUtil.write
import com.asm.privacymethodhooker.testcase.*
import com.asm.privacymethodhooker.utils.NetWorkUtil
import com.dianping.logan.Logan
import com.tencent.mars.xlog.Log
import glog.android.Glog
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.concurrent.atomic.AtomicLong

const val XLOG_FILE_PREFIX = "X"

// generate by generate-ecc-key.py
const val SVR_PUB_KEY =
    "41B5F5F9A53684A1C09B931B7BDF7D7C3959BC7FB31827ADBE1524DDC8F2D90AD4978891385D956CE817B293FC57CF07A4EC3DAF03F63852D75A32A956B84176"
const val SVR_PRIV_KEY = "9C8B23A406216B7B93AA94C66AA5451CCE41DD57A8D5ADBCE8D9F1E7F3D33F45"

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
//        write("glog存储路径 ")
//        write("注册广播监听")
        registerReceiver(ConnectReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//        write("监听网络状态")
        NetWorkUtil.checkNetConnection(App.appContext)

        println(getOriginTxt())

        TextUtil.insetBeforeTest()
        TextUtil.insetAfterTest()
        log.setOnClickListener {
            TextUtil.readNonIncremental()
            TextUtil.clearLog()
        }
        task.setOnClickListener {
            Thread.sleep(5000)
        }
        manyWork.setOnClickListener {
//            write("manyWork.setOnClickListener start ")
            Work.manyWork()
//            write("manyWork.setOnClickListener end")
        }
        ioWork.setOnClickListener {
            val start = System.currentTimeMillis()
            for(i in 0..10000){
                write("manyWorksetOnClickListenerendmanyWorksetOnClickListenerendmanyWorksetOnClickListenerend1234567890")
            }
            android.util.Log.v("tag" , "循环1万次100个字符耗费 ${System.currentTimeMillis() - start}")
//            externalCacheDir?.absolutePath?.run {
//                val file = File("$this/ioWork.txt")
//                if (file.exists()) {
//                    file.delete()
//                }
//                file.createNewFile()
//                Work.ioWork(file)
//            }
        }
    }

//    private final void test1() {
//        Button button = (Button) _$_findCachedViewById(R.id.btnGetRunningAppProcesses);
//        Object systemService = App.appContext.getSystemService("connectivity");
//        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.net.ConnectivityManager");
//        ?? r0 = (ConnectivityManager) systemService;
//        NetworkInfo activeNetworkInfo = PrivacyUtil.getActiveNetworkInfo();
//        r0.setText(activeNetworkInfo != null ? activeNetworkInfo.getTypeName() : null);
//    }

    private fun test1() {
        val info =
            (App.appContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        log.text = info.toString()
    }


//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun test4() {
//        val info = PrivacyUtil.getActiveNetworkInfo()
//        btnGetRunningAppProcesses.text = info.toString()
//    }

    private fun test2() {
        Thread.sleep(20)
        val mConnManager =
            App.appContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnManager.activeNetworkInfo
    }


    private fun test3(): NetworkInfo? {
        val mConnManager =
            App.appContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return mConnManager.activeNetworkInfo
    }

}