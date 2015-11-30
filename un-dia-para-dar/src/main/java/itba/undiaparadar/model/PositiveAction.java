package itba.undiaparadar.model;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

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
    private String city;

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
        return encodedString(title);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return encodedString(description);
    }

    public String getOrganizationName() {
        return encodedString(organizationName);
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

    public String getCity() {
        return city;
    }

    private String encodedString(final String s) {
        try {
            final String encodedString = new String(s.getBytes(), "UTF-8");
            return Html.fromHtml(encodedString).toString();
        } catch (final UnsupportedEncodingException e) {
            return "";
        }
    }
}
