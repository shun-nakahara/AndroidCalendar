package shun.nakahara.androidcalendar.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import shun.nakahara.androidcalendar.R;

/**
 * カレンダーと簡単なメモの画面の土台として定義
 * 中身は特に書くことなかったのでからっぽです。
 *
 * @author shun_nakahara
 * @version 1.0
 * @see {@link shun.nakahara.androidcalendar.calendar.fragment.CalendarFragment}
 */
public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }
}
