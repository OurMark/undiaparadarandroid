package itba.undiaparadar.services;

import android.widget.ImageView;

import itba.undiaparadar.model.Topic;

public class TopicServiceImpl implements TopicService {

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
}
