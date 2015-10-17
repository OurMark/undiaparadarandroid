package itba.undiaparadar.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.services.TopicService;

public class MapFilterItemAdapter extends BaseAdapter {

    @Inject
    private TopicService topicService;
    private final List<Topic> topics;
    private final Context context;

    public MapFilterItemAdapter(final Context context, final List<Topic> topics) {
        UnDiaParaDarApplication.injectMembers(this);
        this.context = context;
        this.topics = topics;
    }

    @Override
    public int getCount() {
        return topics.size();
    }

    @Override
    public Topic getItem(int position) {
        return topics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.map_filter_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.topicImg = (ImageView) convertView.findViewById(R.id.topic_img);

            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final Topic topic, final ViewHolder holder) {
        if (topic.isSelected()) {
            holder.topicImg.setImageResource(topic.getEnableImageResId());
        } else {
            holder.topicImg.setImageResource(topic.getDisableImageResId());
        }
    }

    protected class ViewHolder {
        private ImageView topicImg;
    }

    public void addUnselectedItems(@NonNull final Collection<Topic> topics) {
        for (final Topic topic : topics) {
            if (!topic.isSelected() && !this.topics.contains(topic)) {
                this.topics.add(topic);
            }
        }
        notifyDataSetChanged();
    }

    public void showSelectedTopics(final ModifyItemsCallback modifyItemsCallback) {
        final List<Topic> selectedTopics = topicService.getSelectedTopics(topics);
        if (selectedTopics.isEmpty()) {
            modifyItemsCallback.emptyTopics();
        } else {
            setItems(selectedTopics);
            modifyItemsCallback.fullTopics();
        }
    }

    private void setItems(@NonNull final List<Topic> topics) {
        this.topics.clear();
        this.topics.addAll(topics);
        notifyDataSetChanged();
    }

    public static interface ModifyItemsCallback {
        void emptyTopics();
        void fullTopics();
    }
}
