package hcmute.edu.vn.foregroundservices;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String single;
    private int resource;
    private int image;

    public Song(String title, String single, int resource, int image) {
        this.title = title;
        this.single = single;
        this.resource = resource;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getSingle() {
        return single;
    }

    public int getResource() {
        return resource;
    }

    public int getImage() {
        return image;
    }
}
