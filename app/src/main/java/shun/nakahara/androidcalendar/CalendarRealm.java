package shun.nakahara.androidcalendar;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import shun.nakahara.androidcalendar.model.CalendarMemo;

/**
 * Created by shun_nakahara on 10/20/15.
 *
 * @author shun_nakahara
 */
public class CalendarRealm {

    private static final String CALENDAR_REALM_NAME = "calendar.realm";
    private static final int CALENDAR_SCHEMA_VERSION = 0;

    public static Realm getRealm(@NonNull Context context) {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder(context);
        builder.name(CALENDAR_REALM_NAME);
        builder.schemaVersion(CALENDAR_SCHEMA_VERSION);
        RealmConfiguration realmConfiguration = builder.build();
        return Realm.getInstance(realmConfiguration);
    }

    public static void saveCalenderMemo(@NonNull Realm realm, @NonNull final Date date, @NonNull final String memo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CalendarMemo calendarMemo = realm.createObject(CalendarMemo.class);
                calendarMemo.setDate(date);
                calendarMemo.setMemo(memo);
            }
        });
    }

    public static void editCalenderMemo(@NonNull Realm realm, final CalendarMemo calendarMemo, final String memo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                calendarMemo.setMemo(memo);
            }
        });
    }

    public static CalendarMemo getCalendarMemo(@NonNull Realm realm, @NonNull Date date) {
        RealmQuery<CalendarMemo> memoRealmQuery = realm.where(CalendarMemo.class);
        memoRealmQuery.equalTo("date", date);
        return memoRealmQuery.findFirst();
    }
}
