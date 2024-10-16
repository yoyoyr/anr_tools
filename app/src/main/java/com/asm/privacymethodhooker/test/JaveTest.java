package com.asm.privacymethodhooker.test;

import android.content.ContentResolver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.io.InterruptedIOException;
import java.util.List;
import java.util.TreeMap;

/**
 * @author lanxiaobin
 * @date 2021/10/10
 */
class JaveTest {


    public boolean postAndWait(long timeout) {


        synchronized (this) {
            if (timeout > 0) {
                final long expirationTime = SystemClock.uptimeMillis() + timeout;
                long delay = expirationTime - SystemClock.uptimeMillis();
                if (delay <= 0) {
                    return false;
                }
                try {
                    wait(delay);
                } catch (InterruptedException ignored) {
                }

            } else {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }

            }
        }
        return true;
    }

    private void waitForIo() throws InterruptedIOException {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        }
    }

    public String getDeviceId(Context context) {
        System.out.println(TextUtil.INSTANCE.getOriginTxt());
        ContentResolver cr = context.getContentResolver();
        String androidId = Settings.System.getString(cr, Settings.Secure.ANDROID_ID);
        return androidId;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    List<Sensor> test(Context context) {
        {
            SensorManager manager =
                    (SensorManager) context.getSystemService(SensorManager.class);

            return manager.getSensorList(Sensor.TYPE_ALL);
        }
    }
}
