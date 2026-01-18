package com.bithack.apparatus.ui;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.bithack.apparatus.graphics.G;

/* loaded from: classes.dex */
public class ButtonWidget extends Widget {
    private Mesh mesh;
    private Mesh mesh2;
    private boolean on = false;
    private int tex_x;
    private int tex_y;

    public ButtonWidget(int id, Boolean checked, int width, int height, int tex_x, int tex_y) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.tex_x = tex_x;
        this.tex_y = tex_y;
        this.mesh = new Mesh(true, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoord"));
        float[] v = {(-width) / 2, height / 2, tex_x * 0.00390625f, tex_y * 0.00390625f, (-width) / 2, (-height) / 2, tex_x * 0.00390625f, (tex_y * 0.00390625f) + (this.height * 0.00390625f), width / 2, (-height) / 2, (tex_x * 0.00390625f) + (this.width * 0.00390625f), (tex_y * 0.00390625f) + (this.height * 0.00390625f), width / 2, height / 2, (tex_x * 0.00390625f) + (this.width * 0.00390625f), tex_y * 0.00390625f};
        this.mesh.setVertices(v);
        this.mesh2 = new Mesh(true, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoord"));
        v[0] = (-width) / 2;
        v[1] = height / 2;
        v[2] = (tex_x + 64) * 0.00390625f;
        v[3] = tex_y + (64.0f * 0.00390625f);
        v[4] = (-width) / 2;
        v[5] = (-height) / 2;
        v[6] = (tex_x + 64) * 0.00390625f;
        v[7] = (tex_y * 0.00390625f) + (this.height * 0.00390625f);
        v[8] = width / 2;
        v[9] = (-height) / 2;
        v[10] = ((tex_x + 64) * 0.00390625f) + (this.width * 0.00390625f);
        v[11] = (tex_y * 0.00390625f) + (this.height * 0.00390625f);
        v[12] = width / 2;
        v[13] = height / 2;
        v[14] = ((tex_x + 64) * 0.00390625f) + (this.width * 0.00390625f);
        v[15] = tex_y * 0.00390625f;
        this.mesh2.setVertices(v);
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void render(Texture texture, SpriteBatch batch) {
        G.gl.glPushMatrix();
        G.gl.glTranslatef(this.x, this.y, 0.0f);
        if (this.on) {
            this.mesh2.render(6);
        } else {
            this.mesh.render(6);
        }
        G.gl.glPopMatrix();
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_down_local(int x, int y) {
        if (this.callback != null) {
            this.callback.on_widget_value_change(this.id, 1.0f);
        }
        this.on = true;
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_drag_local(int x, int y) {
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_up_local(int x, int y) {
        this.on = false;
    }
}
