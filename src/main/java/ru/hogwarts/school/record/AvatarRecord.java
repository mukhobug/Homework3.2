package ru.hogwarts.school.record;

public class AvatarRecord {
    private Long id;
    private String mediaType;
    private String url;

    public AvatarRecord() {
    }

    public AvatarRecord(Long id, String mediaType, String url) {
        this.id = id;
        this.mediaType = mediaType;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
