package itba.undiaparadar.services;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import itba.undiaparadar.model.Pledge;
import itba.undiaparadar.model.PledgeStatus;

public class PledgeServiceImpl implements PledgeService {
    private static final String USER_ID = "userId";
    private static final String OBJECT_ID = "objectId";
    private static final String DONE = "done";

    @Override
    public void retrievePledge(final String pledgeId, final GetCallback<Pledge> callback) {
        ParseQuery query = new ParseQuery(Pledge.class);
        query.whereEqualTo(OBJECT_ID, pledgeId);
        query.getFirstInBackground(callback);
    }

    @Override
    public void savePledge(final Pledge pledge, final SaveCallback saveCallback) {
        pledge.saveInBackground(saveCallback);
    }

    @Override
    public void retrievePledges(final String userId, final FindCallback<Pledge> findCallback) {
        ParseQuery<Pledge> query = ParseQuery.getQuery(Pledge.class);
        query.whereEqualTo(USER_ID, userId);
        query.addAscendingOrder(DONE);
        query.findInBackground(findCallback);
    }

    @Override
    public void retrievePledges(final String userId, final FindCallback<Pledge> findCallback, final PledgeStatus pledgeStatus) {
        if (pledgeStatus == null) {
            retrievePledges(userId, findCallback);
        } else {
            ParseQuery<Pledge> query = ParseQuery.getQuery(Pledge.class);
            query.whereEqualTo(USER_ID, userId).whereEqualTo(DONE, pledgeStatus.ordinal());
            query.addAscendingOrder(DONE);
            query.findInBackground(findCallback);
        }
    }

    @Override
    public void savePledge(final Pledge pledge) {
        pledge.saveInBackground();
    }

    @Override
    public void getPledgeDone(final String userId, final CountCallback countCallback) {
        ParseQuery<Pledge> query = ParseQuery.getQuery(Pledge.class);
        query.whereEqualTo(USER_ID, userId);
        query.whereEqualTo(DONE, PledgeStatus.DONE.ordinal());
        query.countInBackground(countCallback);
    }

    @Override
    public void getPledgeTotal(final String userId, final CountCallback countCallback) {
        ParseQuery<Pledge> query = ParseQuery.getQuery(Pledge.class);
        query.whereEqualTo(USER_ID, userId);
        query.countInBackground(countCallback);
    }


}
