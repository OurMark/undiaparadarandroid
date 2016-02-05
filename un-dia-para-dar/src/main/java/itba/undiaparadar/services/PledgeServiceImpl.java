package itba.undiaparadar.services;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import itba.undiaparadar.model.Pledge;

public class PledgeServiceImpl implements PledgeService {
    private static final String USER_ID = "userId";
    private static final String OBJECT_ID = "objectId";

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
        query.addAscendingOrder("done");
        query.findInBackground(findCallback);
    }

    @Override
    public void savePledge(final Pledge pledge) {
        pledge.saveInBackground();
    }

    @Override
    public void getPledgeDone(final String userId, final CountCallback countCallback) {
        ParseQuery<Pledge> query = ParseQuery.getQuery(Pledge.class);
        query.whereEqualTo(USER_ID, userId);
        query.whereEqualTo("done", 1);
        query.countInBackground(countCallback);
    }

    @Override
    public void getPledgeTotal(final String userId, final CountCallback countCallback) {
        ParseQuery<Pledge> query = ParseQuery.getQuery(Pledge.class);
        query.whereEqualTo(USER_ID, userId);
        query.countInBackground(countCallback);
    }


}
