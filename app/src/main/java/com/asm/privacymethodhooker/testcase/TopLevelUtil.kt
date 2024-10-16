package com.asm.privacymethodhooker.testcase

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.os.Process

/**
 * @author lanxiaobin
 * @date 2020-04-25
 */

fun Context.getCurrentProcessName(): String? {
    val pid = Process.myPid()
    var processName = ""
    val manager: ActivityManager =
        applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningAppProcesses = manager.runningAppProcesses
    for (process in runningAppProcesses) {

        if (process.pid == pid) {
            processName = process.processName
        }
    }
    return processName
}

fun getRunningAppProcesses(context: Context): MutableList<RunningAppProcessInfo?>? {
    val manager: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return manager.runningAppProcesses
}




//@RequiresApi(Build.VERSION_CODES.M)
//fun getLastLocation(context: Activity):Location? {
//
//    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//    val provider = manager.getProviders(true)
//    if (ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        context.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),0)
//        return null
//    }
//    Log.i("TAG", "getLastKnownLocation: provider.size=${provider.size}")
//    return manager.getLastLocation(LocationManager.GPS_PROVIDER)
//
//}
