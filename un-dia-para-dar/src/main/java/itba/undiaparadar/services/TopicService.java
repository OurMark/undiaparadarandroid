package itba.undiaparadar.services;

import android.widget.ImageView;

import itba.undiaparadar.model.Topic;

public interface TopicService {
    void loadImageResId(final Topic topic, final ImageView imageView);
}
