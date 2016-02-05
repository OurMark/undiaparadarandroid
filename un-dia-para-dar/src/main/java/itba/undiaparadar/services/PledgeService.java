package itba.undiaparadar.services;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.SaveCallback;

import itba.undiaparadar.model.Pledge;

public interface PledgeService {
    void retrievePledge(final String pledgeId, final GetCallback<Pledge>callback);

    void savePledge(final Pledge pledge, final SaveCallback saveCallback);

    void retrievePledges(final String userId, final FindCallback<Pledge> findCallback);

    void savePledge(final Pledge pledge);

    void getPledgeDone(final String userId, final CountCallback countCallback);

    void getPledgeTotal(final String userId, final CountCallback countCallback);
}
