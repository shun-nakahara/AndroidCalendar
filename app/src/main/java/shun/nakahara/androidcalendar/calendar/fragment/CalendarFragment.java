package shun.nakahara.androidcalendar.calendar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import shun.nakahara.androidcalendar.CalendarApplication;
import shun.nakahara.androidcalendar.R;
import shun.nakahara.androidcalendar.edite.activity.EditActivity;
import shun.nakahara.androidcalendar.model.CalendarMemo;
import shun.nakahara.androidcalendar.util.CalendarRealm;


/**
 * Calendar Fragment
 * <p/>
 * カレンダー画面のロジックを実装してあります。
 *
 * @author shun_nakahara
 * @version 1.0
 */
public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener, View.OnTouchListener {

    @Bind(R.id.calendarView)
    MaterialCalendarView materialCalendarView;

    @Bind(R.id.textView)
    TextView textView;

    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.realm = CalendarRealm.getRealm(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());

        // Setting Material Calendar
        this.materialCalendarView.setOnDateChangedListener(this);
        this.materialCalendarView.setOnMonthChangedListener(this);
        this.materialCalendarView.setDateSelected(CalendarDay.today(), true);

        // Setting Edit Text
        this.textView.setOnTouchListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        CalendarDay calendarDay = this.materialCalendarView.getSelectedDate();

        // Setting Default Edit Text Memo
        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, calendarDay.getDate());
        if (calendarMemo != null) {
            this.textView.setText(calendarMemo.getMemo());
        }
    }

    @Override
    public void onDestroyView() {
        this.materialCalendarView.setOnDateChangedListener(null);
        this.materialCalendarView.setOnMonthChangedListener(null);

        this.textView.setOnClickListener(null);

        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        realm.close();

        super.onDestroy();
        CalendarApplication.watch(this);
    }

    //region material calendar
    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {

        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, date.getDate());
        if (calendarMemo == null) {
            textView.setText("");
        } else {
            textView.setText(calendarMemo.getMemo());
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        this.textView.setText("");

        // 今日の日付がある月にきたらポインターとメモをセットする
        CalendarDay today = CalendarDay.today();
        if (date.getYear() != today.getYear() || date.getMonth() != today.getMonth()) {
            return;
        }

        // Setting Memo
        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, today.getDate());
        if (calendarMemo != null) {
            this.textView.setText(calendarMemo.getMemo());
        }

        // Setting Date Select
        this.materialCalendarView.setDateSelected(today, true);
    }
    //endregion

    //region text view
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CalendarDay calendarDay = this.materialCalendarView.getSelectedDate();
        if (calendarDay == null) {
            return false;
        }

        EditActivity.startActivity(this, calendarDay.getDate().getTime());
        return false;
    }
    // endregion
}
