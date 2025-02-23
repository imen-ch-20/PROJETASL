package model;

public class Chapter {
    private int id;
    private String name;
    private int levelId;

    public Chapter(int id, String name, int levelId) {
        this.id = id;
        this.name = name;
        this.levelId = levelId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLevelId() { return levelId; }
    public void setLevelId(int levelId) { this.levelId = levelId; }
}
