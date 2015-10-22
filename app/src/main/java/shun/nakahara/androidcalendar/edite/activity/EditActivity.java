package shun.nakahara.androidcalendar.edite.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import shun.nakahara.androidcalendar.R;

/**
 * memo 編集画面の土台クラス
 *
 * @author shun_nakahara
 * @version 1.0
 */
public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_LONG_MILLISECONDS = "date_milliseconds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    /**
     * start activity and milliseconds in to extra
     *
     * @param fragment {@link Fragment}
     * @param milliseconds long
     */
    public static void startActivity(Fragment fragment, long milliseconds) {
        startActivity(fragment.getActivity(), milliseconds);
    }

    /**
     * start activity and milliseconds in to extra
     *
     * @param activity {@link Activity}
     * @param milliseconds long
     */
    public static void startActivity(Activity activity, long milliseconds) {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra(EXTRA_LONG_MILLISECONDS, milliseconds);
        activity.startActivity(intent);
    }
}
