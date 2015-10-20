package shun.nakahara.androidcalendar;

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
import shun.nakahara.androidcalendar.model.CalendarMemo;


/**
 * Calendar Fragment
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

        this.materialCalendarView.setOnDateChangedListener(this);
        this.materialCalendarView.setOnMonthChangedListener(this);
        this.materialCalendarView.setDateSelected(CalendarDay.today(), true);

        this.editText.setOnEditorActionListener(this);
        this.editText.setOnTouchListener(this);

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

    //region Material Calendar
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

        CalendarDay today = CalendarDay.today();
        if (date.getYear() == today.getYear() && date.getMonth() == today.getMonth()) {
            CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, CalendarDay.today().getDate());
            if (calendarMemo != null) {
                this.editText.setText(calendarMemo.getMemo());
            }
            this.materialCalendarView.setDateSelected(CalendarDay.today(), true);
        }

    }

    //endregion

    //region Edit Text
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, CalendarDay.today().getDate());

        if (calendarMemo == null) {
            CalendarRealm.saveCalenderMemo(realm, materialCalendarView.getSelectedDate().getDate(), textView.getText().toString());
        } else {
            CalendarRealm.editCalenderMemo(realm, calendarMemo, textView.getText().toString());
        }
        this.editText.setFocusable(false);
        return false;
    }
    //endregion

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        return false;
    }

    private void closedKeyboard(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
