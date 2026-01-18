package com.bithack.apparatus.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bithack.apparatus.graphics.G;

/* loaded from: classes.dex */
public class CheckBoxWidget extends Widget {
    public Boolean checked;
    private int tex_x;
    private int tex_y;

    public CheckBoxWidget(int id, boolean checked, int tex_x, int tex_y, int tex_w, int tex_h) {
        this(id, Boolean.valueOf(checked));
        this.width = tex_w;
        this.height = tex_h;
        this.tex_x = tex_x;
        this.tex_y = tex_y;
    }

    public CheckBoxWidget(int id, Boolean checked) {
        this.checked = false;
        this.tex_x = 64;
        this.tex_y = 32;
        this.checked = checked;
        this.id = id;
        this.width = 32;
        this.height = 32;
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void render(Texture texture, SpriteBatch batch) {
        if (this.checked.booleanValue()) {
            G.batch.setColor(Color.toFloatBits(1.0f, 1.0f, 1.0f, 1.0f));
        } else {
            G.batch.setColor(Color.toFloatBits(1.0f, 1.0f, 1.0f, 0.5f));
        }
        G.batch.begin();
        G.batch.draw(texture, this.x, this.y, 0.0f, 0.0f, this.width, this.width, 1.0f, 1.0f, 0.0f, this.tex_x, this.tex_y, this.height, this.width, false, false);
        G.batch.end();
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_down_local(int x, int y) {
        this.checked = Boolean.valueOf(!this.checked.booleanValue());
        if (this.callback != null) {
            this.callback.on_widget_value_change(this.id, this.checked.booleanValue() ? 1.0f : -1.0f);
        }
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_drag_local(int x, int y) {
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_up_local(int x, int y) {
    }
}
