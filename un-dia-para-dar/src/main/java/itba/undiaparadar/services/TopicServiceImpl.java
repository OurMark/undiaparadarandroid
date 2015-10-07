package itba.undiaparadar.services;

import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.monits.volleyrequests.restsupport.Rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.model.Topic;

public class TopicServiceImpl implements TopicService {
    @Inject
    private Gson gson;
    @Inject
    private RequestQueue requestQueue;

    @Override
    public void loadImageResId(final Topic topic, final ImageView imageView) {
        if (topic.isSelected()) {
            topic.unselect();
            imageView.setImageResource(topic.getDisableImageResId());
        } else {
            topic.select();
            imageView.setImageResource(topic.getEnableImageResId());
        }
    }

    @Override
    public List<Topic> getSelectedTopics(final Collection<Topic> topics) {
        final List<Topic> selectedTopics = new ArrayList<>();

        for (final Topic topic : topics) {
            if (topic.isSelected()) {
                selectedTopics.add(topic);
            }
        }
        return selectedTopics;
    }

    @Override
    public void getPositiveActionsForTopics(final Collection<Topic> collection, final Response.Listener<List<PositiveAction>> listener,
        final Response.ErrorListener errorListener) {
        Rest.setBaseUrl("http://search.ourmark.com/");
        Rest.setGson(gson);
        final Map<String, String> topicsQuery = new HashMap<>();
        final StringBuilder topicQueryBuilder = new StringBuilder("topics:(");
        final Topic[] topics = collection.toArray(new Topic[collection.size()]);
        for (int i = 0; i < topics.length; i++) {
            topicQueryBuilder.append(topics[i].getId());
            if (i != topics.length - 1) {
                topicQueryBuilder.append("+OR+");
            }
        }
        topicQueryBuilder.append(")");
        topicsQuery.put("q", topicQueryBuilder.toString());
        topicsQuery.put("wt", "json");
        topicsQuery.put("indent", "true");
        topicsQuery.put("rows", "10000000");

        final Request<List<PositiveAction>> request = Rest
                .one("solr", "classfield_core")
            .all("select")
            .get(PositiveAction.class)
            .query(topicsQuery)
            .onSuccess(listener)
            .onError(errorListener)
            .request();

        requestQueue.add(request);
    }
}
