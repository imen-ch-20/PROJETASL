package model;

public class Lesson {
    private int id;
    private String name;
    private String contentText;
    private String contentImage;

    public Lesson(int id, String name, String contentText, String contentImage) {
        this.id = id;
        this.name = name;
        this.contentText = contentText;
        this.contentImage = contentImage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContentText() {
        return contentText;
    }

    public String getContentImage() {
        return contentImage;
    }
}
