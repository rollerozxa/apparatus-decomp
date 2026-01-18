package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

public class RopeEnd extends GrabableObject implements BaseRopeEnd {
    protected static FixtureDef _fd;
    private Fixture f;
    public final Rope rope;

    public RopeEnd(World world, Rope rope) {
        this.f = null;
        this.rope = rope;
        this.body = world.createBody(Rope._bd);
        this.f = this.body.createFixture(_fd);
        this.body.setUserData(this);
        this.sandbox_only = false;
        this.fixed_rotation = true;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.DynamicBody;
    }

    @Override
    public void on_click() {
    }

    public void render_inner_half() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 0.75f);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(0.5f, 0.5f, 0.7f);
            MiscRenderer.draw_hqcylinder();
            G.gl.glPopMatrix();
        }
    }

    @Override
    public void render() {
        Vector2 pos = get_state().position;
        if (this.layer == 0) {
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.25f);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(0.25f, 0.25f, 0.6f);
            MiscRenderer.draw_hqcylinder();
            G.gl.glPopMatrix();
        }
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
    public BaseRope get_baserope() {
        return this.rope;
    }

    @Override
    public void update_properties() {
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
