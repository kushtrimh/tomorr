package com.kushtrimh.tomorr.spotify.api.response;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ImageResponseData {
    private int height;
    private int width;
    private String url;

    public ImageResponseData() {
    }

    public ImageResponseData(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageResponseData that = (ImageResponseData) o;
        return height == that.height && width == that.width && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, width, url);
    }

    @Override
    public String toString() {
        return "ImageResponseData{" +
                "height=" + height +
                ", width=" + width +
                ", url='" + url + '\'' +
                '}';
    }
}
