package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

public class DamperEnd extends GrabableObject {
    protected static FixtureDef _fd;
    public final Damper damper;
    private Fixture f;
    int index;
    private Fixture sensor;

    public DamperEnd(World world, Damper damper, int index) {
        this.f = null;
        this.sensor = null;
        this.index = index;
        this.damper = damper;
        this.body = world.createBody(Damper._bd);
        this.f = this.body.createFixture(_fd);
        this.sensor = this.body.createFixture(Damper._sfd);
        this.sensor.setUserData(this);
        this.body.setUserData(this);
        this.sandbox_only = false;
        this.fixed_rotation = false;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.DynamicBody;
    }

    @Override
    public void on_click() {
    }

    @Override
    public void render() {
        G.gl.glPushMatrix();
        Vector2 pos = get_state().position;
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
        G.gl.glRotatef(get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
        G.gl.glTranslatef(0.0f, 0.15f, 0.0f);
        G.gl.glScalef((this.index == 0 ? 0.1f : 0.0f) + 0.8f, 1.75f, (this.index != 0 ? 0.0f : 0.1f) + 0.8f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
    }

    public void render_box() {
        G.gl.glPushMatrix();
        Vector2 pos = get_state().position;
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
        G.gl.glRotatef(get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
        G.gl.glTranslatef(0.0f, -0.752f, 0.0f);
        G.gl.glScalef(1.2f, 0.5f, 1.0f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
    }

    @Override
    public void step(float deltatime) {
    }

    @Override
    public Vector2 get_position() {
        return this.body.getPosition();
    }

    @Override
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override
    public void dispose(World world) {
        world.destroyBody(this.body);
    }

    @Override
    public void reshape() {
        this.body.destroyFixture(this.f);
        this.f = this.body.createFixture(_fd);
    }

    @Override
    public void update_properties() {
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
