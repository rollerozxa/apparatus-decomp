package com.bithack.apparatus.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

public class Bucket extends GrabableObject {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    private static FixtureDef _sfd;
    private static PolygonShape _shape;
    private float last_angle = 1232131.0f;
    private final Mesh[] silhouette_mesh = new Mesh[3];
    private static boolean _initialized = false;
    private static Vector2 tmp2 = new Vector2();

    public Bucket(World world) {
        this.sandbox_only = true;
        this.body = world.createBody(_bd);
        _shape.setAsBox(2.0f, 0.25f, new Vector2(0.0f, -1.0f), 0.0f);
        this.body.createFixture(_fd);
        _shape.setAsBox(0.25f, 1.15f, new Vector2(-2.0f, -0.16f), 0.0f);
        this.body.createFixture(_fd);
        _shape.setAsBox(0.25f, 1.15f, new Vector2(2.0f, -0.16f), 0.0f);
        this.body.createFixture(_fd);
        _shape.setAsBox(1.8f, 1.0f);
        this.body.createFixture(_sfd);
        this.body.setUserData(this);
        this.layer = 0;
        this.ingame_type = BodyDef.BodyType.StaticBody;
        this.build_type = BodyDef.BodyType.StaticBody;
    }

    public static void _init() {
        if (!_initialized) {
            _initialized = true;
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.StaticBody;
            _shape = new PolygonShape();
            _fd = new FixtureDef();
            _fd.restitution = 0.2f;
            _fd.friction = 0.2f;
            _fd.density = 0.7f;
            _fd.shape = _shape;
            _sfd = new FixtureDef();
            _sfd.shape = _shape;
            _sfd.isSensor = true;
            _sfd.density = 0.01f;
        }
    }

    @Override
    public void on_click() {
    }

    @Override
    public void update_properties() {
    }

    @Override
    public void render() {
        Vector2 pos = get_state().position;
        float angle = get_state().angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 0.5f);
        G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
        G.gl.glPushMatrix();
        G.gl.glTranslatef(0.0f, -1.0f, 0.0f);
        G.gl.glScalef(4.0f, 0.5f, 1.8f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
        G.gl.glPushMatrix();
        G.gl.glTranslatef(-2.0f, -0.16f, 0.0f);
        G.gl.glScalef(0.5f, 2.3f, 1.9f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
        G.gl.glPushMatrix();
        G.gl.glTranslatef(2.0f, -0.16f, 0.0f);
        G.gl.glScalef(0.5f, 2.3f, 1.9f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
        G.gl.glPushMatrix();
        G.gl.glTranslatef(0.0f, 0.5f, 1.0f);
        G.gl.glScalef(3.8f, 0.45f, 0.25f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
        G.gl.glPushMatrix();
        G.gl.glTranslatef(0.0f, 0.0f, 1.0f);
        G.gl.glScalef(3.8f, 0.45f, 0.25f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
        G.gl.glPushMatrix();
        G.gl.glTranslatef(0.0f, -0.5f, 1.0f);
        G.gl.glScalef(3.8f, 0.45f, 0.25f);
        MiscRenderer.hqcubemesh.render(4);
        G.gl.glPopMatrix();
        G.gl.glPopMatrix();
    }

    @Override
    public void grab() {
        super.grab();
        this.body.setType(BodyDef.BodyType.DynamicBody);
        Gdx.app.log("set to dynamic", "");
    }

    @Override
    public void release() {
        super.release();
        this.body.setType(BodyDef.BodyType.StaticBody);
    }

    @Override
    public void play() {
        this.body.setType(BodyDef.BodyType.StaticBody);
        super.play();
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
    public void reshape() {
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
