package com.example.grybos.aplikacjakurs4.Helpers;

public class Item {

    private String name;
    private String time;
    private String size;

    public Item(String name, String time, String size) {
        this.name = name;
        this.time = time;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getSize() {
        return size;
    }
}
