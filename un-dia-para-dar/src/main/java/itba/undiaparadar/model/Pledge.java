package itba.undiaparadar.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Pledge")
public class Pledge extends ParseObject {
    private static final String USER_ID = "userId";
    private static final String TARGET_DATE = "targetDate";
    private static final String DONE = "done";
    private static final String POSITIVE_ACTION_ID = "positiveActionId";
    private String userId;
    private Date targetDate;
    private boolean done;
    private long positiveActionId;

    public String getUserId() {
        return getString(USER_ID);
    }

    public Date getTargetDate() {
        return getDate(TARGET_DATE);
    }

    public boolean getDone() {
        return getBoolean(DONE);
    }

    public long getPositiveActionId() {
        return getInt(POSITIVE_ACTION_ID);
    }

    public void setUserId(final String userId) {
        put(USER_ID, userId);
    }

    public void setTargetDate(final Date targetDate) {
        put(TARGET_DATE, targetDate);
    }

    public void setDone(final boolean done) {
        put(DONE, done);
    }

    public void setPositiveActionId(final long positiveActionId) {
        put(POSITIVE_ACTION_ID, positiveActionId);
    }
}
