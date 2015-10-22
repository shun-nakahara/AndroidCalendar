package shun.nakahara.androidcalendar.calendar.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import shun.nakahara.androidcalendar.model.CalendarMemo;
import shun.nakahara.androidcalendar.util.CalendarRealm;


/**
 * Calendar Fragment
 * <p>
 * カレンダー画面のロジックを実装してあります。
 *
 * @author shun_nakahara
 * @version 1.0
 */
public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener, TextView.OnEditorActionListener, View.OnTouchListener {

    @Bind(R.id.calendarView)
    MaterialCalendarView materialCalendarView;

    @Bind(R.id.editText)
    EditText editText;

    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.realm = CalendarRealm.getRealm(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
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
        this.editText.setOnEditorActionListener(this);
        this.editText.setOnTouchListener(this);

        // Setting Default Edit Text Memo
        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, CalendarDay.today().getDate());
        if (calendarMemo != null) {
            this.editText.setText(calendarMemo.getMemo());
        }
    }

    @Override
    public void onDestroyView() {
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
        this.editText.setFocusable(false);

        closedKeyboard(getActivity(), widget);


        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, date.getDate());
        if (calendarMemo == null) {
            editText.setText("");
        } else {
            editText.setText(calendarMemo.getMemo());
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        editText.setText("");

        // 今日の日付がある月にきたらポインターとメモをセットする
        CalendarDay today = CalendarDay.today();
        if (date.getYear() != today.getYear() || date.getMonth() != today.getMonth()) {
            return;
        }

        // Setting Memo
        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, today.getDate());
        if (calendarMemo != null) {
            this.editText.setText(calendarMemo.getMemo());
        }

        // Setting Date Select
        this.materialCalendarView.setDateSelected(today, true);
    }
    //endregion


    /**
     * 今表示されているキーボードを消す処理
     *
     * @param activity {@link Activity} 現在表示している Activity
     * @param view     {@link View} キーボードが表示されているView
     */
    private static void closedKeyboard(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //region edit text
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        CalendarRealm.createOrUpdateCalendarMemo(realm, materialCalendarView.getSelectedDate().getDate(), textView.getText().toString());
        this.editText.setFocusable(false);
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        return false;
    }
    // endregion
}
