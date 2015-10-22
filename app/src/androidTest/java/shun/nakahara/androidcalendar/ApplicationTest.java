package shun.nakahara.androidcalendar;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import io.realm.Realm;
import shun.nakahara.androidcalendar.model.CalendarMemo;
import shun.nakahara.androidcalendar.util.CalendarRealm;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private Realm realm;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.realm = CalendarRealm.getRealm(getContext());
    }

    /**
     * Create Realm Object Test
     *
     * @throws Exception
     */
    public void testRealm() throws Exception {
        assertNotNull(realm);
    }

    /**
     * Create Calendar Memo Object Test
     *
     * @throws Exception
     */
    public void testCreateCalendarMemo() throws Exception {
        String memoString = "テストをかきかき！";

        CalendarDay calendarDay = CalendarDay.today();
        CalendarRealm.createOrUpdateCalendarMemo(realm, calendarDay.getDate(), memoString);

        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, calendarDay.getDate());

        assertNotNull(calendarMemo);
        assertEquals(calendarMemo.getMemo(), memoString);
        assertEquals(calendarMemo.getDate().getTime(), calendarDay.getDate().getTime());
    }

    /**
     * Update Calendar Memo Object Test
     *
     * @throws Exception
     */
    public void testEditCalenarMemo() throws Exception {
        String memoString = "テストをかきかき！";

        CalendarDay calendarDay = CalendarDay.today();
        CalendarRealm.createOrUpdateCalendarMemo(realm, calendarDay.getDate(), memoString);

        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, calendarDay.getDate());

        assertNotNull(calendarMemo);
        assertEquals(calendarMemo.getMemo(), memoString);

        String updateMemoString = "更新かきかき！";
        CalendarRealm.createOrUpdateCalendarMemo(realm, calendarDay.getDate(), updateMemoString);

        calendarMemo = CalendarRealm.getCalendarMemo(realm, calendarDay.getDate());

        assertNotNull(calendarMemo);
        assertEquals(calendarMemo.getMemo(), updateMemoString);
    }

    /**
     * Delete Calendar Memo Object Test
     *
     * @throws Exception
     */
    public void testDeleteCalendarMemo() throws Exception {
        String memoString = "テストをかきかき！";

        CalendarDay calendarDay = CalendarDay.today();
        CalendarRealm.createOrUpdateCalendarMemo(realm, calendarDay.getDate(), memoString);

        CalendarMemo calendarMemo = CalendarRealm.getCalendarMemo(realm, calendarDay.getDate());

        assertNotNull(calendarMemo);
        assertEquals(calendarMemo.getMemo(), memoString);

        CalendarRealm.deleteCalendarMemo(realm, calendarMemo);

        calendarMemo = CalendarRealm.getCalendarMemo(realm, calendarDay.getDate());

        assertNull(calendarMemo);
    }

    @Override
    protected void tearDown() throws Exception {

        this.realm.close();
        super.tearDown();
    }
}