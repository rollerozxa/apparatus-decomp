package com.bithack.apparatus.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

/* loaded from: classes.dex */
public class TextButtonWidget extends Widget {
    public boolean checked = false;
    public Vector3 color = new Vector3(0.2f, 0.2f, 0.2f);
    BitmapFont font;
    String text;

    public TextButtonWidget(int id, String text, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.text = text;
        this.font = new BitmapFont();
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void render(Texture texture, SpriteBatch batch) {
        G.gl.glPushMatrix();
        G.gl.glTranslatef(this.x + (this.width / 2), this.y + (this.height / 2), 10.0f);
        G.gl.glScalef(this.width, this.height, 1.0f);
        G.gl.glDisable(3553);
        if (this.checked) {
            G.color(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            G.color(0.2f, 0.2f, 0.2f, 1.0f);
        }
        MiscRenderer.draw_colored_box();
        G.gl.glPopMatrix();
        G.batch.begin();
        if (this.checked) {
            G.font.setColor(Color.BLACK);
        } else {
            G.font.setColor(Color.WHITE);
        }
        G.font.draw(G.batch, this.text, this.x + 24, (this.y + this.height) - 2);
        G.batch.end();
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_down_local(int x, int y) {
        if (this.callback != null) {
            this.callback.on_widget_value_change(this.id, 1.0f);
        }
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_drag_local(int x, int y) {
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_up_local(int x, int y) {
    }
}
