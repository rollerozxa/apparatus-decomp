package com.bithack.apparatus.ui;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

/* loaded from: classes.dex */
public class TilesetWidget extends Widget {
    private Vector2 drag_start;
    private float iheight;
    private float iwidth;
    private Vector2 last_drag;
    Mesh mesh;
    int offset_x;
    int offset_y;
    int tile_x;
    int tile_y;
    Texture tileset;
    private float trn_x;
    private float trn_y;

    public TilesetWidget(int id, int width, int height) {
        this(id, null, width, height);
    }

    public TilesetWidget(int id, Texture tiletex, int width, int height) {
        this.tileset = null;
        this.mesh = null;
        this.offset_x = 0;
        this.offset_y = 0;
        this.tile_x = 0;
        this.tile_y = 0;
        this.last_drag = new Vector2();
        this.drag_start = new Vector2();
        this.trn_x = 0.0f;
        this.trn_y = 0.0f;
        this.id = id;
        this.width = (width * 32) + 8;
        this.height = (height * 32) + 8;
        this.iwidth = width;
        this.iheight = height;
        if (tiletex != null) {
            set_texture(tiletex);
        }
    }

    public void set_texture(Texture texture) {
        if (this.mesh != null) {
            this.mesh.dispose();
        }
        this.tileset = texture;
        this.mesh = new Mesh(true, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoord"));
        float step = 32.0f / this.tileset.getWidth();
        float[] v = {((-32.0f) * this.iwidth) / 2.0f, (32.0f * this.iheight) / 2.0f, 0.0f, 0.0f, ((-32.0f) * this.iwidth) / 2.0f, ((-32.0f) * this.iheight) / 2.0f, 0.0f, this.iheight * step, (32.0f * this.iwidth) / 2.0f, ((-32.0f) * this.iheight) / 2.0f, this.iwidth * step, this.iheight * step, (32.0f * this.iwidth) / 2.0f, (32.0f * this.iheight) / 2.0f, this.iwidth * step, 0.0f};
        this.mesh.setVertices(v);
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void render(Texture _unused, SpriteBatch __unused) {
        if (this.mesh != null && this.tileset != null) {
            int ttx = (((int) this.trn_x) / 32) * 32;
            int tty = (((int) this.trn_y) / 32) * 32;
            G.gl.glMatrixMode(GL10.GL_PROJECTION);
            G.gl.glPushMatrix();
            G.gl.glLoadMatrixf(G.cam_p.combined.getValues(), 0);
            G.gl.glMatrixMode(GL10.GL_MODELVIEW);
            G.gl.glDisable(3553);
            G.gl.glPushMatrix();
            G.gl.glTranslatef(this.x + (this.width / 2), this.y + (this.height / 2), 0.0f);
            G.gl.glScalef(1.1f, 1.1f, 1.1f);
            G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            this.mesh.render(6);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            G.gl.glEnable(3553);
            this.tileset.bind();
            G.gl.glMatrixMode(5890);
            G.gl.glLoadIdentity();
            G.gl.glTranslatef(ttx / this.tileset.getWidth(), tty / this.tileset.getWidth(), 0.0f);
            G.gl.glMatrixMode(GL10.GL_MODELVIEW);
            this.mesh.render(6);
            G.gl.glMatrixMode(5890);
            G.gl.glLoadIdentity();
            G.gl.glMatrixMode(GL10.GL_MODELVIEW);
            G.gl.glDisable(3553);
            G.gl.glPopMatrix();
            if (this.tile_x * 32 >= ttx && (this.tile_x * 32) + 32 <= this.width + ttx) {
                G.color(0.0f, 0.0f, 0.0f, 1.0f);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(((this.x + (this.tile_x * 32)) - ttx) + 20, ((this.y + this.height) - ((this.tile_y * 32) - tty)) - 20, 0.0f);
                G.gl.glScalef(32.0f, 32.0f, 1.0f);
                MiscRenderer.draw_colored_square();
                G.gl.glScalef(1.1f, 1.1f, 1.0f);
                G.color(1.0f, 1.0f, 1.0f, 1.0f);
                MiscRenderer.draw_colored_square();
                G.gl.glPopMatrix();
            }
            G.gl.glMatrixMode(GL10.GL_PROJECTION);
            G.gl.glPopMatrix();
            G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        }
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_down_local(int x, int y) {
        this.last_drag.set(x, y);
        this.drag_start.set(this.last_drag);
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_drag_local(int x, int y) {
        this.trn_x -= x - this.last_drag.x;
        this.trn_y += y - this.last_drag.y;
        if (this.trn_x < -1.0f) {
            this.trn_x = -1.0f;
        }
        if (this.trn_y < 0.0f) {
            this.trn_y = 0.0f;
        }
        this.last_drag.set(x, y);
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_up_local(int x, int y) {
        if (Math.abs(this.drag_start.x - x) < 16.0f && Math.abs(this.drag_start.y - y) < 16.0f) {
            this.tile_x = (((((int) this.trn_x) / 32) * 32) + x) / 32;
            this.tile_y = (((((int) this.trn_y) / 32) * 32) + (this.height - y)) / 32;
            this.callback.on_widget_value_change(this.id, ((char) this.tile_x) + (((char) this.tile_y) * 16));
        }
    }
}
