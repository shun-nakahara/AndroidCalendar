package shun.nakahara.androidcalendar;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by shun_nakahara on 10/20/15.
 *
 * @author shun_nakahara
 * @version 1.0
 */
public class CalendarApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        // Leak Canary
        this.refWatcher = LeakCanary.install(this);
    }

    /**
     *  メモリーリークを監視するためのメソッドです
     *
     *  @param watchedReference {@link Fragment} メモリーリークを確認する {@link Fragment} を指定します。
     *
     */
    public static void watch(@NonNull Fragment watchedReference) {
        Activity activity = watchedReference.getActivity();
        if (activity != null) {
            CalendarApplication application = (CalendarApplication) activity.getApplication();
            application.refWatcher.watch(watchedReference);
        }
    }
}
