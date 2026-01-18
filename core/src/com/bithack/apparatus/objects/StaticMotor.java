package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

public class StaticMotor extends BaseMotor {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    private static CircleShape _shape;
    private static boolean initialized = false;

    public StaticMotor(World world) {
        super(world);
        this.layer = -1;
        this.fixed_layer = true;
        this.fixed_rotation = true;
        this.ingame_type = BodyDef.BodyType.StaticBody;
        this.build_type = BodyDef.BodyType.StaticBody;
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
        if (this.attached_object != null) {
            this.attached_object.set_active(true);
        }
    }

    private static void init() {
        if (!initialized) {
            initialized = true;
            _bd = new BodyDef();
            _fd = new FixtureDef();
            _shape = new CircleShape();
            _shape.setRadius(0.6f);
            _bd.type = BodyDef.BodyType.StaticBody;
            _bd.fixedRotation = true;
            _fd.isSensor = true;
            _fd.density = 1.0f;
            _fd.friction = 0.1f;
            _fd.restitution = 0.0f;
            _fd.shape = _shape;
        }
    }

    @Override
    protected void create_body() {
        init();
        this.body = this.world.createBody(_bd);
        this.body.createFixture(_fd);
        this.body.setUserData(this);
    }

    @Override
    public void play() {
        this.layer = -1;
        this.body.setType(BodyDef.BodyType.StaticBody);
        super.play();
    }

    @Override
    public void pause() {
        this.layer = -1;
        this.body.setType(BodyDef.BodyType.StaticBody);
        super.pause();
    }

    public void render_deco() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            G.gl.glTranslatef(get_state().position.x, get_state().position.y, -0.45f);
            G.gl.glScalef(this.dir * 0.4f, 0.4f, 1.0f);
            MiscRenderer.draw_textured_box();
            G.gl.glPopMatrix();
        }
    }

    @Override
    public void render() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, -0.5f);
            G.gl.glScalef(0.6f, 0.6f, 0.05f);
            MiscRenderer.draw_cylinder();
            G.gl.glPopMatrix();
        }
    }

    public void render_box() {
        G.gl.glPushMatrix();
        Vector2 pos = get_state().position;
        G.gl.glTranslatef(pos.x, pos.y - 0.25f, 1.25f);
        G.gl.glScalef(0.25f, 0.4f, 0.25f);
        MiscRenderer.cubemesh.render(4);
        G.gl.glPopMatrix();
    }

    public void render_hinge() {
        G.gl.glPushMatrix();
        Vector2 pos = get_state().position;
        G.gl.glTranslatef(pos.x, pos.y, 1.0f);
        G.gl.glScalef(0.1f, 0.1f, 3.0f);
        MiscRenderer.draw_smallcylinder();
        G.gl.glPopMatrix();
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
