package shun.nakahara.androidcalendar.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import shun.nakahara.androidcalendar.model.CalendarMemo;

/**
 * Realm で Calendar の日付に対して書き込んだメモを保存するための仲介クラス
 *
 * @author shun_nakahara
 * @see {@link CalendarMemo}
 */
public class CalendarRealm {

    /**
     * SsQLite (Realm) File Name
     */
    private static final String CALENDAR_REALM_NAME = "calendar.realm";

    /**
     * SQLite schema version
     */
    private static final int CALENDAR_SCHEMA_VERSION = 0;

    /**
     * Create Realm Instance
     *
     * @param context {@link Context}
     * @return {@link Realm}
     */
    @NonNull
    public static Realm getRealm(@NonNull Context context) {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder(context);
        builder.name(CALENDAR_REALM_NAME);
        builder.schemaVersion(CALENDAR_SCHEMA_VERSION);
        RealmConfiguration realmConfiguration = builder.build();
        return Realm.getInstance(realmConfiguration);
    }

    /**
     * Save Calendar Memo with date
     *
     * @param realm {@link Realm}
     * @param date  {@link Date}
     * @param memo  {@link String}
     */
    private static void saveCalendarMemo(@NonNull Realm realm, @NonNull final Date date, @NonNull final String memo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CalendarMemo calendarMemo = realm.createObject(CalendarMemo.class);
                calendarMemo.setDate(date);
                calendarMemo.setMemo(memo);
            }
        });
    }

    /**
     * Update Calendar Memo
     *
     * @param realm        {@link Realm}
     * @param calendarMemo {@link CalendarMemo}
     * @param memo         {@link String}
     */
    private static void editCalendarMemo(@NonNull Realm realm, @NonNull final CalendarMemo calendarMemo, @NonNull final String memo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                calendarMemo.setMemo(memo);
            }
        });
    }

    /**
     * Delete Calendar Memo
     *
     * @param realm        {@link Realm}
     * @param calendarMemo {@link CalendarMemo}
     */
    public static void deleteCalendarMemo(@NonNull Realm realm, @NonNull final CalendarMemo calendarMemo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                calendarMemo.removeFromRealm();
            }
        });

    }

    /**
     * Get Calendar Memo Model
     *
     * @param realm {@link Realm}
     * @param date  {@link Date} 取得したいメモの日付
     * @return {@link CalendarMemo}
     */
    @Nullable
    public static CalendarMemo getCalendarMemo(@NonNull Realm realm, @NonNull Date date) {
        RealmQuery<CalendarMemo> memoRealmQuery = realm.where(CalendarMemo.class);
        memoRealmQuery.equalTo("date", date);
        return memoRealmQuery.findFirst();
    }

    public static void createOrUpdateCalendarMemo(@NonNull Realm realm, @NonNull Date date, @NonNull String memo) {
        CalendarMemo calendarMemo = getCalendarMemo(realm, date);
        if (calendarMemo == null) {
            saveCalendarMemo(realm, date, memo);
        } else {
            editCalendarMemo(realm, calendarMemo, memo);
        }
    }
}
