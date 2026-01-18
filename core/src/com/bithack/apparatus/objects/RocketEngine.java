package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.SoundManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public class RocketEngine extends GrabableObject {
    static BodyDef _bd;
    static FixtureDef _fd;
    static FixtureDef _fd2;
    static FixtureDef _sfd;
    static PolygonShape _shape;
    static PolygonShape _shape2;
    static PolygonShape _sshape;
    public PanelCableEnd attached_cable_end;
    private Fixture sensor;
    private static boolean _initialized = false;
    private static Vector2 tmp = new Vector2();
    private static Vector2 tmp2 = new Vector2();
    public static Matrix4 ztransform = new Matrix4();
    public static Matrix4 ztransform_l = new Matrix4();
    static Random _r = new Random();
    public static ArrayList<Fire> fires = new ArrayList<>();
    public static ArrayList<Light> lights = new ArrayList<>();
    Vector2 hejhej = new Vector2(0.0f, 1.0f);
    Light mylight = new Light();
    public boolean attached = false;
    float real_countdown = 0.0f;
    float countdown = 0.0f;
    public float thrust = 0.5f;
    int counter = 0;
    boolean active = false;
    float output = 1.0f;

    public static class Light {
        Vector2 pos = new Vector2();
    }

    public RocketEngine(World world) {
        _init();
        this.sandbox_only = false;
        this.layer = 0;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.DynamicBody;
        this.fixed_layer = true;
        this.body = world.createBody(_bd);
        this.body.createFixture(_fd);
        this.body.createFixture(_fd2);
        this.sensor = this.body.createFixture(_sfd);
        this.sensor.setUserData(this.hejhej);
        this.body.setUserData(this);
        this.properties = new BaseObject.Property[]{new BaseObject.Property("thrust", BaseObject.Property.Type.FLOAT, new Float(0.5f))};
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
        set_property("thrust", Float.valueOf(this.thrust));
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("thrust")) {
            this.thrust = ((Float) value).floatValue();
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream o) throws IOException {
        update_properties();
        super.write_to_stream(o);
    }

    public static void _init() {
        if (!_initialized) {
            Explosive._init();
            ztransform.setToTranslation(0.0f, 0.0f, 1.1f);
            ztransform_l.setToTranslation(0.0f, 0.0f, 1.6f);
            _initialized = true;
            _sshape = new PolygonShape();
            _sshape.setAsBox(0.5f, 0.25f, new Vector2(0.0f, 1.25f), 0.0f);
            _sfd = new FixtureDef();
            _sfd.isSensor = true;
            _sfd.density = 0.0f;
            _sfd.shape = _sshape;
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.DynamicBody;
            _shape = new PolygonShape();
            _shape.setAsBox(0.5f, 0.75f, new Vector2(0.0f, -0.75f), 0.0f);
            _shape2 = new PolygonShape();
            _shape2.setAsBox(0.5f, 0.5f, new Vector2(0.0f, 0.5f), 0.0f);
            _fd = new FixtureDef();
            _fd.density = 1.0f;
            _fd.restitution = 0.0f;
            _fd.friction = 0.4f;
            _fd.shape = _shape;
            _fd2 = new FixtureDef();
            _fd2.density = 2.0f;
            _fd2.restitution = 0.0f;
            _fd2.friction = 0.4f;
            _fd2.shape = _shape2;
        }
    }

    public void render_box() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef(get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
            G.gl.glTranslatef(0.0f, 0.752f, 0.0f);
            G.gl.glScalef(1.2f, 0.5f, 1.0f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        if (!this.attached) {
            this.output = 1.0f;
            activate();
        }
        super.play();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void pause() {
        deactivate();
        super.pause();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        if (!this.culled) {
            Vector2 pos = get_state().position;
            float angle = get_state().angle;
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(1.0f, 2.0f, 0.8f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glTranslatef(-0.4f, -1.0f, 0.0f);
            G.gl.glScalef(0.2f, 1.0f, 0.5f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glTranslatef(0.4f, -1.0f, 0.0f);
            G.gl.glScalef(0.2f, 1.0f, 0.5f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void step(float deltatime) {
        if (this.active) {
            this.counter++;
            if (this.output > 0.0f) {
                float angle = (float) (get_state().angle + 1.5707963267948966d);
                tmp.set((float) (Math.cos(angle) * 500.0d * this.thrust * this.output), (float) (Math.sin(angle) * 500.0d * this.thrust * this.output));
                this.body.applyForce(tmp, this.body.getWorldCenter(), true);
                tmp.set(0.0f, -1.5f);
                Vector2 t = this.body.getWorldPoint(tmp);
                fires.add(new Fire(0, t, angle));
                this.mylight.pos.set(t);
            }
        }
    }

    void activate() {
        if (!lights.contains(this.mylight)) {
            lights.add(this.mylight);
            if (!this.active) {
                SoundManager.play_rocket_launch();
            }
        }
        this.active = true;
    }

    void deactivate() {
        try {
            lights.remove(this.mylight);
        } catch (Exception e) {
        }
        this.active = false;
        if (lights.isEmpty()) {
            SoundManager.stop_rocket_thrust();
        }
    }

    public static class Fire {
        Vector2 pos = new Vector2();
        float time;
        float vx;
        float vy;

        public Fire(int p, Vector2 origo, float angle) {
            this.time = 0.0f;
            this.pos.set(origo);
            this.pos.x += (RocketEngine._r.nextFloat() - 0.25f) * 0.5f;
            this.pos.y += (RocketEngine._r.nextFloat() - 0.25f) * 0.5f;
            this.time = 0.0f;
            this.vx = -((float) Math.cos(angle));
            this.vy = -((float) Math.sin(angle));
        }
    }

    public static void render_fire() {
        int sz = fires.size();
        if (sz > 0) {
            G.batch.setBlendFunction(770, 771);
            G.batch.begin();
            Iterator<Fire> i = fires.iterator();
            while (i.hasNext()) {
                Fire f = i.next();
                f.time += G.delta * 6.0f;
                f.pos.x += f.vx * 0.08f;
                f.pos.y += f.vy * 0.08f;
                if (f.time >= 0.0f) {
                    float time = f.time;
                    float alpha = 1.0f;
                    if (time > 1.0f) {
                        alpha = 1.0f - (time - 1.0f);
                        time = 1.0f;
                        if (alpha <= 0.0f) {
                            i.remove();
                        }
                    }
                    float t1 = time < 0.0f ? 0.0f : time;
                    float t2 = t1 * t1;
                    G.batch.setColor(1.0f - (0.8f * t2), 1.0f - t2, 1.0f - t1, alpha);
                    G.batch.draw(Explosive._firetex, f.pos.x - 0.5f, f.pos.y - 0.5f, 0.25f, 0.25f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0, 0, 128, 128, false, false);
                }
            }
            G.batch.end();
        }
    }

    public static void render_lights() {
        if (!lights.isEmpty()) {
            G.batch.setBlendFunction(770, 771);
            G.batch.begin();
            Iterator<Light> i = lights.iterator();
            while (i.hasNext()) {
                Light l = i.next();
                G.batch.setColor(1.0f, 0.99f, 0.99f, 0.3f);
                float s = 5.0f + (_r.nextFloat() * 2.0f);
                G.batch.draw(Explosive._lighttex, l.pos.x - (s / 2.0f), l.pos.y - (s / 2.0f), 0.0f, 0.0f, s, s, 1.0f, 1.0f, 0.0f, 0, 0, 32, 32, false, false);
            }
            G.batch.end();
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Vector2 get_position() {
        return this.body.getPosition();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public float get_bb_radius() {
        return 0.0f;
    }

    public void set_output(float f) {
        this.output = f;
        if (f <= 0.0f) {
            deactivate();
        } else {
            activate();
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void dispose(World world) {
        if (this.attached_cable_end != null) {
            this.attached_cable_end.detach();
        }
        super.dispose(world);
    }
}
