package itba.undiaparadar.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mpurita on 10/7/15.
 */
public class PositiveAction implements Serializable {
    private long id;
    @SerializedName("latlng_0_coordinate")
    private double latitude;
    @SerializedName("latlng_1_coordinate")
    private double longitude;
    @SerializedName("video_url")
    private String videoUrl;
    private String country;
    private String title;
    private String subtitle;
    @SerializedName("desc")
    private String description;
    @SerializedName("ong_name")
    private String organizationName;
    @SerializedName("ong_id")
    private long organizationId;
    @SerializedName("topics")
    private long topicId;
    @SerializedName("external_url")
    private String externalUrl;

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getCountry() {
        return country;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public long getTopicId() {
        return topicId;
    }

    public String getExternalUrl() {
        return externalUrl;
    }
}
