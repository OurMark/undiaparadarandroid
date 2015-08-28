package itba.undiaparadar.model;

public class Topic {
    private final int enableImageResId;
    private final int disableImageResId;
    private final String name;
    private boolean selected;

    public Topic(final int enableImageId, final String name, final int disableImageResId) {
        this.enableImageResId = enableImageId;
        this.name = name;
        this.disableImageResId = disableImageResId;
    }

    public int getEnableImageResId() {
        return this.enableImageResId;
    }

    public String getName() {
        return this.name;
    }

    public int getDisableImageResId() {
        return this.disableImageResId;
    }

    public void select() {
        this.selected = true;
    }

    public void unselect() {
        this.selected = false;
    }

    public boolean isSelected() {
        return this.selected;
    }
}
