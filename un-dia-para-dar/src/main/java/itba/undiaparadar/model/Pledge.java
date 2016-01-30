package itba.undiaparadar.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Pledge")
public class Pledge extends ParseObject {
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
        return getString(USER_ID);
    }

    public String getPositiveActionTitle() {
        return getString(POSITIVE_ACTION_TITLE);
    }

    public String getTargetDate() {
        return getString(TARGET_DATE);
    }

    public int getDone() {
        return getInt(DONE);
    }

    public long getPositiveActionId() {
        return getInt(POSITIVE_ACTION_ID);
    }

    public String getCode() {
        return getString(CODE);
    }

    public void setUserId(final String userId) {
        put(USER_ID, userId);
    }

    public void setPositiveActionTitle(final String positiveActionTitle) {
        put(POSITIVE_ACTION_TITLE, positiveActionTitle);
    }

    public void setTargetDate(final String targetDate) {
        put(TARGET_DATE, targetDate);
    }

    public void setDone(final int done) {
        put(DONE, done);
    }

    public void setPositiveActionId(final long positiveActionId) {
        put(POSITIVE_ACTION_ID, positiveActionId);
    }

    public void setCode(final String code) {
        put(CODE, code);
    }
}
