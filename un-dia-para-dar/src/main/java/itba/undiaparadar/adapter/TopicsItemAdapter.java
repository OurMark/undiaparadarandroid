package itba.undiaparadar.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import itba.undiaparadar.R;
import itba.undiaparadar.model.Topic;

public class TopicsItemAdapter extends BaseAdapter {

    private final Context context;
    private final List<Topic> topics;

    public TopicsItemAdapter(final Context context, final List<Topic> topics) {
        this.context = context;
        this.topics = topics;
    }

    @Override
    public int getCount() {
        return topics == null ? 0 : topics.size();
    }

    @Nullable
    @Override
    public Topic getItem(int position) {
        return position > topics.size() ? null : topics.get(position);
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

        viewHolder.topicImg.setImageResource(topic.getImageId());
        viewHolder.topicName.setText(topic.getName());

        return convertView;
    }

    protected class ViewHolder {
        private ImageView topicImg;
        private TextView topicName;
    }
}
