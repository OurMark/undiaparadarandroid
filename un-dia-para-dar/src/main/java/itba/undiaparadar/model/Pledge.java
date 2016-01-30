package itba.undiaparadar.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

@ParseClassName("Pledge")
public class Pledge extends ParseObject implements Serializable {
    private static final String USER_ID = "userId";
    private static final String TARGET_DATE = "targetDate";
    private static final String POSITIVE_ACTION_TITLE = "positiveActionTitle";
    private static final String DONE = "done";
    private static final String POSITIVE_ACTION_ID = "positiveActionId";
    private static final String CODE = "code";
    private String userId;
    private String positiveActionTitle;
    private String code;
    private String targetDate;
    private int done;
    private long positiveActionId;

    public String getUserId() {
        if (userId != null) {
            return userId;
        }
        return getString(USER_ID);
    }

    public String getPositiveActionTitle() {
        if (positiveActionTitle != null) {
            return positiveActionTitle;
        }
        return getString(POSITIVE_ACTION_TITLE);
    }

    public String getTargetDate() {
        if (targetDate != null) {
            return targetDate;
        }
        return getString(TARGET_DATE);
    }

    public int getDone() {
        if (done != 0) {
            return done;
        }
        return getInt(DONE);
    }

    public long getPositiveActionId() {
        if (positiveActionId != 0) {
            return positiveActionId;
        }
        return getInt(POSITIVE_ACTION_ID);
    }

    public String getCode() {
        if (code != null) {
            return code;
        }
        return getString(CODE);
    }

    public void setUserId(final String userId) {
        this.userId = userId;
        put(USER_ID, userId);
    }

    public void setPositiveActionTitle(final String positiveActionTitle) {
        this.positiveActionTitle = positiveActionTitle;
        put(POSITIVE_ACTION_TITLE, positiveActionTitle);
    }

    public void setTargetDate(final String targetDate) {
        this.targetDate = targetDate;
        put(TARGET_DATE, targetDate);
    }

    public void setDone(final int done) {
        this.done = done;
        put(DONE, done);
    }

    public void setPositiveActionId(final long positiveActionId) {
        this.positiveActionId = positiveActionId;
        put(POSITIVE_ACTION_ID, positiveActionId);
    }

    public void setCode(final String code) {
        this.code = code;
        put(CODE, code);
    }
}
