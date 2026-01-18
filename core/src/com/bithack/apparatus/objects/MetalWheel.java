package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

public class MetalWheel extends Wheel {
    public MetalWheel(World world, float size) {
        super(world, size);
        this.sandbox_only = true;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.StaticBody;
    }

    @Override
    public void render() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(this.size, this.size, 0.8f);
            MiscRenderer.draw_hqcylinder();
            G.gl.glPopMatrix();
        }
    }

    @Override
    public void render_lq() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(this.size, this.size, 0.8f);
            MiscRenderer.draw_cylinder();
            G.gl.glPopMatrix();
        }
    }

    public void render_inner_lq() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, 1.125f + this.layer);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(this.size / 2.0f, this.size / 2.0f, 0.7f);
            MiscRenderer.draw_cylinder();
            G.gl.glPopMatrix();
        }
    }

    public void render_inner() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, 1.125f + this.layer);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(this.size / 2.0f, this.size / 2.0f, 0.7f);
            MiscRenderer.draw_hqcylinder();
            G.gl.glPopMatrix();
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (Game.sandbox) {
            this.body.setType(BodyDef.BodyType.DynamicBody);
        } else {
            this.body.setType(BodyDef.BodyType.StaticBody);
        }
    }

    @Override
    public void sandbox_save() {
        super.sandbox_save();
        this.body.setType(BodyDef.BodyType.StaticBody);
    }

    @Override
    public void sandbox_load() {
        this.body.setType(BodyDef.BodyType.DynamicBody);
        super.sandbox_load();
    }

    @Override
    public void play() {
        super.play();
        this.body.setType(BodyDef.BodyType.DynamicBody);
    }

    @Override
    public void reshape() {
        if (this.f != null) {
            this.body.destroyFixture(this.f);
        }
        _shape.setRadius(this.size);
        _fd.density = 10.0f;
        _fd.restitution = 0.0f;
        this.f = this.body.createFixture(_fd);
    }
}
