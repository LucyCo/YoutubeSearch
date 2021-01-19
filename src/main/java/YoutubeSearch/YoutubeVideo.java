package YoutubeSearch;

import java.util.Objects;

public class YoutubeVideo {
    public String videoId;
    public String title;
    public String channelId;
    public String channelTitle;
    public String description;

    public YoutubeVideo() {
        this.title = "";
    }

    public YoutubeVideo(String videoId) {
        this.videoId = videoId;
    }

    public YoutubeVideo(String videoId, String title, String channelId,
                        String channelTitle, String description) {
        this.videoId = videoId;
        this.title = title;
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YoutubeVideo)) return false;
        YoutubeVideo video = (YoutubeVideo) o;
        return Objects.equals(getVideoId(), video.getVideoId()) && Objects.equals(getTitle(), video.getTitle()) && Objects.equals(getChannelId(), video.getChannelId()) && Objects.equals(getChannelTitle(), video.getChannelTitle()) && Objects.equals(getDescription(), video.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVideoId(), getTitle(), getChannelId(), getChannelTitle(), getDescription());
    }

    @Override
    public String toString() {
        return "YoutubeVideo{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
