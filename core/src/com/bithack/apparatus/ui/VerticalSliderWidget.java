package com.bithack.apparatus.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bithack.apparatus.graphics.G;

/* loaded from: classes.dex */
public class VerticalSliderWidget extends Widget {
    private float max;
    private float min;
    private float snap;
    public float value;

    public VerticalSliderWidget(int id) {
        this(id, -1.0f, 1.0f, 0, 0);
    }

    public VerticalSliderWidget(int id, int size) {
        this(id, -1.0f, 1.0f, 0, 0);
        this.height = size;
    }

    public VerticalSliderWidget(int id, int size, float snap) {
        this(id, -1.0f, 1.0f, 0, 0);
        this.height = size;
        this.snap = snap;
    }

    public VerticalSliderWidget(int id, float min, float max, int x, int y) {
        this.snap = 0.0f;
        this.min = min;
        this.max = max;
        this.value = 0.0f;
        this.height = 128;
        this.width = 32;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void render(Texture texture, SpriteBatch batch) {
        G.batch.begin();
        G.batch.setColor(Color.WHITE);
        G.batch.draw(texture, this.x, this.y, 0.0f, 0.0f, 32.0f, 16.0f, 1.0f, 1.0f, 0.0f, 32, 48, 32, 16, false, false);
        G.batch.draw(texture, this.x, this.y + 16, 0.0f, 0.0f, this.width, this.height - 32, 1.0f, 1.0f, 0.0f, 80, 0, 32, 32, false, false);
        G.batch.draw(texture, this.x, (this.y + this.height) - 16.0f, 0.0f, 0.0f, 32.0f, 16.0f, 1.0f, 1.0f, 0.0f, 32, 32, 32, 16, false, false);
        float v = this.value - this.min;
        float m = this.max - this.min;
        float percentage = v / m;
        G.batch.draw(texture, this.x, (this.y + (this.height * percentage)) - 8.0f, 48.0f, 0.0f, 32.0f, 16.0f, 1.0f, 1.0f, 0.0f, 48, 0, 32, 16, false, false);
        G.batch.end();
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_down_local(int x, int y) {
        this.value = this.min + ((this.max - this.min) * (y / this.height));
        if (this.snap != 0.0f) {
            this.value = this.snap * ((int) (this.value / this.snap));
        }
        if (this.callback != null) {
            this.callback.on_widget_value_change(this.id, this.value);
        }
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_drag_local(int x, int y) {
        touch_down_local(x, y);
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_up_local(int x, int y) {
    }
}
