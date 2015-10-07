package itba.undiaparadar.model;

/**
 * Created by mpurita on 10/7/15.
 */
public class UnDiaParaDarMarker {
    private final Topic topic;
    private final PositiveAction positiveAction;

    public UnDiaParaDarMarker(final Topic topic, final PositiveAction positiveAction) {
        this.topic = topic;
        this.positiveAction = positiveAction;
    }

    public Topic getTopic() {
        return topic;
    }

    public PositiveAction getPositiveAction() {
        return positiveAction;
    }
}
