package itba.undiaparadar.adapter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.services.TopicService;

public class TopicsItemAdapter extends BaseAdapter {

    private final Context context;
    private final Topic[] topics;

    public TopicsItemAdapter(final Context context, final Collection<Topic> topics) {
        this.context = context;
        this.topics = topics.toArray(new Topic[topics.size()]);
    }

    @Override
    public int getCount() {
        return topics == null ? 0 : topics.length;
    }

    @Nullable
    @Override
    public Topic getItem(int position) {
        return position > topics.length ? null : topics[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.topics_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.topicImg = (ImageView) convertView.findViewById(R.id.topic_img);
            viewHolder.topicName = (TextView) convertView.findViewById(R.id.topic_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Topic topic = getItem(position);

        if (topic.isSelected()) {
            viewHolder.topicImg.setImageResource(topic.getEnableImageResId());
        } else {
            viewHolder.topicImg.setImageResource(topic.getDisableImageResId());
        };
        viewHolder.topicName.setText(topic.getName());

        return convertView;
    }

    protected class ViewHolder {
        private ImageView topicImg;
        private TextView topicName;
    }
}
