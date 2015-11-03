package itba.undiaparadar.model;

import java.io.Serializable;

public class Topic implements Serializable {
	private final int enableImageResId;
	private final int disableImageResId;
	private final long id;
	private final String name;
	private boolean selected;

	public Topic(final long id, final int enableImageId, final String name, final int disableImageResId) {
		this.id = id;
		this.enableImageResId = enableImageId;
		this.name = name;
		this.disableImageResId = disableImageResId;
	}

	public long getId() {
		return this.id;
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Topic topic = (Topic) o;

		return id == topic.id;

	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}
}
