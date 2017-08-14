package baway.com.qiaoshi20170814;

import android.app.Application;

import org.xutils.x;

/**
 * Created by qiaoshi on 2017/8/14.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
