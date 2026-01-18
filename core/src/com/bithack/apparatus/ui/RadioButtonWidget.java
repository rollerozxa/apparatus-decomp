package com.bithack.apparatus.ui;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/* loaded from: classes.dex */
public class RadioButtonWidget extends Widget implements RadioWidget {
    public Boolean checked;
    private Texture custom_texture;
    private RadioButtonWidgetGroup group;
    private int group_id;
    private Mesh mesh;
    private int tex_x;
    private int tex_y;

    public RadioButtonWidget(int id, Boolean checked, int width, int height, int tex_x, int tex_y, Texture custom_texture, RadioButtonWidgetGroup group) {
        this.checked = false;
        this.custom_texture = null;
        this.checked = checked;
        this.id = id;
        this.width = width;
        this.height = height;
        this.tex_x = tex_x;
        this.tex_y = tex_y;
        this.group = group;
        this.group_id = group.add_button(this);
        if (this.custom_texture != null) {
            this.custom_texture = custom_texture;
            init_mesh(this.custom_texture.getWidth(), this.custom_texture.getHeight());
        }
    }

    public void set_texture(Texture texture) {
        this.custom_texture = texture;
        init_mesh(this.custom_texture.getWidth(), this.custom_texture.getHeight());
    }

    public RadioButtonWidget(int id, Boolean checked, int width, int height, int tex_x, int tex_y, RadioButtonWidgetGroup group) {
        this.checked = false;
        this.custom_texture = null;
        this.checked = checked;
        this.id = id;
        this.width = width;
        this.height = height;
        this.tex_x = tex_x;
        this.tex_y = tex_y;
        this.group = group;
        this.group_id = group.add_button(this);
        init_mesh(256, 256);
    }

    private void init_mesh(int tex_w, int tex_h) {
        if (this.mesh != null) {
            this.mesh.dispose();
        }
        this.mesh = new Mesh(true, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoord"));
        float step = 1.0f / tex_w;
        float[] v = {(-this.width) / 2, this.height / 2, this.tex_x * step, this.tex_y * step, (-this.width) / 2, (-this.height) / 2, this.tex_x * step, (this.tex_y * step) + (this.height * step), this.width / 2, (-this.height) / 2, (this.tex_x * step) + (this.width * step), (this.tex_y * step) + (this.height * step), this.width / 2, this.height / 2, (this.tex_x * step) + (this.width * step), this.tex_y * step};
        this.mesh.setVertices(v);
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void render(Texture texture, SpriteBatch batch) {
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_down_local(int x, int y) {
        if (!this.checked.booleanValue()) {
            this.group.click(this.group_id);
            if (this.callback != null) {
                this.callback.on_widget_value_change(this.id, 1.0f);
            }
        }
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_drag_local(int x, int y) {
    }

    @Override // com.bithack.apparatus.ui.IWidget
    public void touch_up_local(int x, int y) {
    }

    @Override // com.bithack.apparatus.ui.RadioWidget
    public void set_checked(boolean ch) {
        this.checked = Boolean.valueOf(ch);
    }
}
