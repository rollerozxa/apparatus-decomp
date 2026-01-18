package com.bithack.apparatus.ui;

public abstract class Widget implements IWidget {
    WidgetValueCallback callback;
    int height;
    int width;
    public int x;
    public int y;
    public int id = 0;
    boolean disabled = false;
}
