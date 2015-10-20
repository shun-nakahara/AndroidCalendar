package shun.nakahara.androidcalendar.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * Created by shun_nakahara on 10/20/15.
 *
 * @author shun_nakahara
 */
public class CalendarMemo extends RealmObject {

    @Index
    private Date date;

    @Required
    private String memo;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
