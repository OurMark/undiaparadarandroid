package itba.undiaparadar.services;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.model.Topic;

public interface TopicService {
    void loadImageResId(final Topic topic, final ImageView imageView);

    List<Topic> getSelectedTopics(final Collection<Topic> topics);

    void getPositiveActionsForTopics(final Collection<Topic> topics, final Response.Listener<List<PositiveAction>> listener,
        final Response.ErrorListener errorListener);

    HashMap<Long, Topic> createTopics(final Context context);
}
