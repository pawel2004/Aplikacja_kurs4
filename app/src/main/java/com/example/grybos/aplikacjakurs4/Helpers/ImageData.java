package com.example.grybos.aplikacjakurs4.Helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageData implements Serializable{

    private int x, y, w, h;

    public ImageData(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

}