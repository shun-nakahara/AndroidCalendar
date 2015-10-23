package shun.nakahara.androidcalendar.edite.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import shun.nakahara.androidcalendar.CalendarApplication;
import shun.nakahara.androidcalendar.R;
import shun.nakahara.androidcalendar.edite.activity.EditActivity;
import shun.nakahara.androidcalendar.model.CalendarMemo;
import shun.nakahara.androidcalendar.util.CalendarRealm;

/**
 * A simple {@link Fragment} subclass.
 * <p/>
 * 編集ページの画面ロジックを書いています。
 */
public class EditFragment extends Fragment implements View.OnKeyListener {

    @Bind(R.id.memoEditText)
    EditText memoEditText;

    @Bind(R.id.dateTextView)
    TextView dateTextView;

    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.realm = CalendarRealm.getRealm(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());

        // set date label
        long milliseconds = getMilliseconds();
        Date date = new Date(milliseconds);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
        String dateString = dateFormat.format(date);
        this.dateTextView.setText(dateString);

        // set calendar memo
        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(this.realm, date);
        if (calendarMemo != null) {
            this.memoEditText.setText(calendarMemo.getMemo());
        }
        this.memoEditText.setOnKeyListener(this);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.realm.close();
        super.onDestroy();
        CalendarApplication.watch(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Date date = new Date(getMilliseconds());
        CalendarRealm.createOrUpdateCalendarMemo(this.realm, date, this.memoEditText.getText().toString());
        return false;
    }

    /**
     * Activity に渡された extra milliseconds 値を取得
     *
     * @return milliseconds
     */
    private long getMilliseconds() {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return 0L;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return 0L;
        }

        return bundle.getLong(EditActivity.EXTRA_LONG_MILLISECONDS);
    }
}
