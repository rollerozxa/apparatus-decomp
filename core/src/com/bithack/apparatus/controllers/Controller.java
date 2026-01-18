package com.bithack.apparatus.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Controller extends InputProcessor {
    void render(SpriteBatch spriteBatch);
}
