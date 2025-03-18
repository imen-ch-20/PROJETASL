package model;

public class Level {
    private int id;
    private String name;

    public Level(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // Display level name in the ListView
    }
}
