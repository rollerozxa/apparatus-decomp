package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.SoundManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

public class Battery extends GrabableObject {
    private static BodyDef _bd;
    private static FixtureDef _bfd;
    private static PolygonShape _bshape;
    private static FixtureDef _fd;
    private static PolygonShape _shape;
    private static PolygonShape _smallshape;
    public static Texture _texture;
    protected Mesh silhouette_mesh;
    protected static boolean _initialized = false;
    private static Vector2 tmp2 = new Vector2();
    public static Matrix4 transform = new Matrix4();
    public static Matrix4 tmpmat = new Matrix4();
    public boolean on = true;
    public boolean real_on = true;
    private Fixture f = null;
    private Fixture btn = null;
    protected float last_angle = 4512.0f;
    public int size = 2;
    public float voltage = 0.5f;
    public float current = 0.5f;
    private long last_btn = 0;
    boolean[] used_socket = new boolean[2];
    PanelCableEnd[] pcable = new PanelCableEnd[2];
    float output = 1.0f;

    public static void _init() {
        if (!_initialized) {
            _texture = TextureFactory.load("data/voltage.png");
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.DynamicBody;
            _fd = new FixtureDef();
            _shape = new PolygonShape();
            _shape.setAsBox(1.125f, 0.90000004f);
            _fd.density = 3.0f;
            _fd.friction = 0.5f;
            _fd.restitution = 0.0f;
            _fd.shape = _shape;
            _smallshape = new PolygonShape();
            _smallshape.setAsBox(0.5f, 0.5f);
            _bfd = new FixtureDef();
            _bshape = new PolygonShape();
            _bshape.setAsBox(0.5f, 0.25f, new Vector2(-0.5f, 0.8f), 0.0f);
            _bfd.density = 1.0E-4f;
            _bfd.friction = 0.1f;
            _bfd.restitution = 0.0f;
            _bfd.shape = _bshape;
            _initialized = true;
        }
    }

    public int get_available_socket() {
        for (int x = 0; x < this.used_socket.length; x++) {
            if (!this.used_socket[x]) {
                return x;
            }
        }
        return -1;
    }

    @Override
    public void translate(float x, float y) {
        for (int i = 0; i < this.used_socket.length; i++) {
            if (this.used_socket[i]) {
                this.pcable[i].update_pos();
            }
        }
        if (this.cabled) {
            if (((CableEnd) this.cable.g1).attached_object == this) {
                ((CableEnd) this.cable.g1).update_pos();
            } else if (((CableEnd) this.cable.g2).attached_object == this) {
                ((CableEnd) this.cable.g2).update_pos();
            }
        }
        super.translate(x, y);
    }

    @Override
    public void grab() {
        this.grabbed = true;
    }

    @Override
    public void release() {
        this.grabbed = false;
    }

    @Override
    public void pause() {
        super.pause();
        this.on = this.real_on;
        if (Game.sandbox) {
            this.body.setType(BodyDef.BodyType.DynamicBody);
        } else {
            this.body.setType(BodyDef.BodyType.StaticBody);
        }
    }

    @Override
    public void play() {
        super.play();
        if (this.cabled) {
            if (this.on) {
                this.cable.on_connect();
            } else {
                this.cable.on_disconnect();
            }
        }
        this.body.setType(BodyDef.BodyType.DynamicBody);
    }

    public Battery(World world) {
        if (!_initialized) {
            _init();
        }
        this.sandbox_only = true;
        this.body = world.createBody(_bd);
        reshape();
        this.body.setUserData(this);
        this.layer = 0;
        this.properties = new BaseObject.Property[]{
                new BaseObject.Property("v", BaseObject.Property.Type.FLOAT, 0.5f),
                new BaseObject.Property("a", BaseObject.Property.Type.FLOAT, 0.5f),
                new BaseObject.Property("s", BaseObject.Property.Type.INT, this.size),
                new BaseObject.Property("on", BaseObject.Property.Type.BOOLEAN, Boolean.TRUE)};
        this.fixed_layer = true;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.StaticBody;
    }

    @Override
    public final void render() {
        if (!this.culled) {
            Vector2 pos = get_state().position;
            float angle = get_state().angle;
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, 0.5f + this.layer);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            if (this.size == 1) {
                G.gl.glScalef(1.0f, 1.0f, 1.001f);
            } else {
                G.gl.glScalef(2.25f, 1.8000001f, 1.001f);
            }
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    public void render_btn() {
        if (!this.culled && this.size == 2) {
            Vector2 pos = get_state().position;
            float angle = get_state().angle;
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 0.5f);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glTranslatef(-0.5f, 0.8f, 0.0f);
            G.gl.glRotatef(this.on ? 15 : -15, 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(1.0f, 0.5f, 0.5f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    public void render_light() {
        if (!this.culled && this.size == 2) {
            Vector2 vector2 = get_state().position;
            float f = get_state().angle;
            G.gl.glPushMatrix();
            G.gl.glTranslatef(get_state().position.x, get_state().position.y, 1.01f);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glTranslatef(0.7f, -0.55f, 0.0f);
            G.gl.glScalef(0.5f, 0.38f, 1.0f);
            MiscRenderer.boxmesh.render(6);
            G.gl.glPopMatrix();
        }
    }

    public void render_sign() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            G.gl.glTranslatef(get_state().position.x, get_state().position.y, 1.01f);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            if (this.size == 2) {
                G.gl.glTranslatef(-0.4f, 0.2f, 0.0f);
                G.gl.glScalef(0.6f, 0.6f, 1.0f);
            } else {
                G.gl.glTranslatef(-0.0f, 0.1f, 0.0f);
                G.gl.glScalef(0.3f, 0.3f, 1.0f);
            }
            MiscRenderer.boxmesh.render(6);
            G.gl.glPopMatrix();
        }
    }

    public void render_levels() {
        if (!this.culled && this.size == 2) {
            G.gl.glPushMatrix();
            G.gl.glTranslatef(get_state().position.x, get_state().position.y, 1.01f);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glColor4f(0.05f, 0.05f, 0.05f, 1.0f);
            G.gl.glPushMatrix();
            G.gl.glTranslatef(0.65f, 0.5f, 0.0f);
            G.gl.glScalef(0.3f, 0.0925f, 1.0f);
            MiscRenderer.boxmesh.render(6);
            G.gl.glPopMatrix();
            G.gl.glPushMatrix();
            G.gl.glTranslatef(0.65f, 0.1f, 0.0f);
            G.gl.glScalef(0.3f, 0.0925f, 1.0f);
            MiscRenderer.boxmesh.render(6);
            G.gl.glPopMatrix();
            G.gl.glColor4f(0.95f, 0.95f, 1.0f, 1.0f);
            G.gl.glPushMatrix();
            G.gl.glTranslatef((this.voltage * 0.325f) + 0.325f, 0.5f, 0.0f);
            G.gl.glScalef(this.voltage * 0.325f, 0.0925f, 1.0f);
            MiscRenderer.boxmesh.render(6);
            G.gl.glPopMatrix();
            G.gl.glPushMatrix();
            G.gl.glTranslatef((this.current * 0.325f) + 0.325f, 0.1f, 0.0f);
            G.gl.glScalef(this.current * 0.325f, 0.0925f, 1.0f);
            MiscRenderer.boxmesh.render(6);
            G.gl.glPopMatrix();
            if (this.on) {
                G.gl.glColor4f(0.8f, 1.0f, 0.8f, 1.0f);
            } else {
                G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            }
            G.gl.glPushMatrix();
            G.gl.glTranslatef(0.7f, -0.55f, 0.0f);
            G.gl.glScalef(0.2f, 0.14f, 1.0f);
            MiscRenderer.boxmesh.render(6);
            G.gl.glPopMatrix();
            G.gl.glPopMatrix();
        }
    }

    public void render_deco(Camera cam) {
        if (!this.culled) {
            Vector2 pos = get_state().position;
            float angle = get_state().angle;
            transform.setToTranslation(pos.x, pos.y, 1.01f);
            tmpmat.setToRotation(G.vec_rot, (float) (angle * 57.29577951308232d));
            transform.mul(tmpmat);
            G.batch.setProjectionMatrix(cam.combined);
            G.batch.setTransformMatrix(transform);
            G.batch.begin();
            G.batch.draw(_texture, -0.97499996f, -0.3f, 0.0f, 0.0f, 1.05f, 0.88199997f, 1.0f, 1.0f, 0.0f, 0, 0, 64, 54, false, false);
            G.batch.setColor(0.1f, 0.1f, 0.1f, 1.0f);
            G.batch.draw(_texture, 0.3f, 0.3f, 0.0f, 0.0f, 0.6f, 0.2775f, 1.0f, 1.0f, 0.0f, 0, 58, 49, 6, false, false);
            G.batch.draw(_texture, 0.3f, -0.15f, 0.0f, 0.0f, 0.6f, 0.2775f, 1.0f, 1.0f, 0.0f, 0, 58, 49, 6, false, false);
            if (this.on) {
                G.batch.setColor(0.8f, 1.0f, 0.8f, 1.0f);
            } else {
                G.batch.setColor(0.0f, 0.0f, 0.0f, 0.5f);
            }
            G.batch.draw(_texture, 0.6f, -0.75f, 0.0f, 0.0f, 0.4f, 0.2f, 1.0f, 1.0f, 0.0f, 50, 57, 13, 6, false, false);
            G.batch.setColor(0.6f, 1.0f, 0.6f, 0.2f);
            if (this.on) {
                G.batch.draw(BaseRope._texture, 0.32999998f, -0.97499996f, 0.0f, 0.0f, 0.9f, 0.7f, 1.0f, 1.0f, 0.0f, 0, 0, BaseRope._texture.getWidth(), BaseRope._texture.getHeight(), false, false);
            }
            G.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            G.batch.draw(_texture, 0.3f, 0.3f, 0.0f, 0.0f, 0.4f * this.voltage * 1.5f, 0.2775f, 1.0f, 1.0f, 0.0f, 0, 58, (int) (49.0f * this.voltage), 6, false, false);
            G.batch.draw(_texture, 0.3f, -0.15f, 0.0f, 0.0f, 0.4f * this.current * 1.5f, 0.2775f, 1.0f, 1.0f, 0.0f, 0, 58, (int) (49.0f * this.current), 6, false, false);
            G.batch.end();
            G.batch.getProjectionMatrix().idt();
            G.batch.getTransformMatrix().idt();
        }
    }

    public void render_deco() {
        render_deco(G.p_cam);
    }

    @Override
    public void on_click() {
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
        if (this.f != null) {
            this.body.destroyFixture(this.f);
        }
        if (this.btn != null) {
            this.body.destroyFixture(this.btn);
        }
        if (this.size == 2) {
            this.btn = this.body.createFixture(_bfd);
            this.btn.setUserData(this);
            _fd.shape = _shape;
        } else {
            this.btn = null;
            _fd.shape = _smallshape;
        }
        this.f = this.body.createFixture(_fd);
    }

    public void toggle_onoff() {
        long now = System.currentTimeMillis();
        if (now - this.last_btn > 800) {
            this.on = !this.on;
            this.last_btn = now;
            if (this.cabled) {
                this.cable.update();
            }
            SoundManager.play_battery_switch();
        }
    }

    public void force_toggle_onoff() {
        this.on = !this.on;
        if (this.cabled) {
            this.cable.update();
        }
    }

    @Override
    public void set_property(String name, Object value) {
        if (name.equals("v")) {
            this.voltage = (Float) value;
        } else if (name.equals("a")) {
            this.current = (Float) value;
        } else if (name.equals("on")) {
            boolean zBooleanValue = (Boolean) value;
            this.on = zBooleanValue;
            this.real_on = zBooleanValue;
        } else if (name.equals("s")) {
            this.size = (Integer) value;
            reshape();
        }
        super.set_property(name, value);
    }

    @Override
    public void update_properties() {
        set_property("a", this.current);
        set_property("v", this.voltage);
        set_property("on", this.real_on);
        set_property("s", this.size);
    }

    @Override
    public void write_to_stream(JarOutputStream s) throws IOException {
        update_properties();
        super.write_to_stream(s);
    }

    public void set_output(float g) {
        this.output = g;
        if (this.cabled) {
            this.cable.update();
        }
    }

    public void connect_socket(int socket, PanelCableEnd end) {
        if (socket < this.used_socket.length && socket >= 0) {
            this.used_socket[socket] = true;
            this.pcable[socket] = end;
        }
    }

    public void disconnect_socket(int socket) {
        if (socket < this.used_socket.length && socket >= 0) {
            this.used_socket[socket] = false;
            this.pcable[socket] = null;
            if (Game.mode != Game.MODE_PLAY) {
                set_output(1.0f);
            }
        }
    }

    public void resize(int size) {
        detach_pcables();
        switch (size) {
            case 1:
                this.size = 1;
                break;
            default:
                this.size = 2;
                break;
        }
        reshape();
    }

    public void detach_pcables() {
        for (int x = 0; x < this.used_socket.length; x++) {
            if (this.used_socket[x]) {
                this.pcable[x].detach();
            }
        }
    }

    @Override
    public void dispose(World world) {
        detach_pcables();
        super.dispose(world);
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
