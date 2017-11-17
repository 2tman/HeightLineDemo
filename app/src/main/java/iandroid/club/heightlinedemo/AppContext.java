package iandroid.club.heightlinedemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

import iandroid.club.heightlinedemo.utils.LogUtils;
import iandroid.club.heightlinedemo.utils.Utils;

/**
 * Created by 加荣 on 2017/7/28.
 */

public class AppContext extends Application {

    private static AppContext appContext;

    public static AppContext getInstance() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Utils.init(this);
        LogUtils.init(this);
        Stetho.initializeWithDefaults(this);

    }
}
