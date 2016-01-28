package itba.undiaparadar.services;

import com.parse.FindCallback;
import com.parse.SaveCallback;

import itba.undiaparadar.model.Pledge;

public interface PledgeService {
    void savePledge(final Pledge pledge, final SaveCallback saveCallback);

    void retrievePledges(final String userId, final FindCallback<Pledge> findCallback);
}
