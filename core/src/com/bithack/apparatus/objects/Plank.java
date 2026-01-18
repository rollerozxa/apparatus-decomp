package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.ApparatusApp;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public class Plank extends Bar implements FreeObject {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    private static FixtureDef _sfd;
    private static PolygonShape _shape;
    private static PolygonShape _sshape;
    public static Texture texture;
    public Fixture f;
    private static Vector2 _tmp = new Vector2();
    static final float[] _material = {0.15f, 0.15f, 0.15f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.5f, 0.2f, 0.2f, 0.2f, 0.2f, 0.5f, 0.5f, 0.5f, 0.8f, 10.0f, 0.0f, 0.0f, 0.0f};
    public static boolean _initialized = false;
    private static final Vector2 _dir = new Vector2();
    private static final Vector2 _p1 = new Vector2();
    private static final Vector2 _p2 = new Vector2();
    private static final Vector2 _p3 = new Vector2();
    private static final Vector2 _p4 = new Vector2();
    public Fixture fa = null;
    public Fixture fb = null;
    private boolean attached_a = false;
    private boolean attached_b = false;
    int _sz = 2;

    public Plank(World world, float size) {
        this.f = null;
        if (!_initialized) {
            _init();
        }
        this.sandbox_only = false;
        this.body = world.createBody(_bd);
        _shape.setAsBox(4.0f, 0.25f);
        this.f = this.body.createFixture(_fd);
        this.body.setUserData(this);
        this.size.set(2.0f * size, 0.5f, 0.5f);
        this.properties = new BaseObject.Property[]{new BaseObject.Property("size", BaseObject.Property.Type.FLOAT, Float.valueOf(this.size.x))};
        this.layer = 0;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.DynamicBody;
    }

    public static void _init() {
        if (!Bar._initialized) {
            Bar._init();
        }
        texture = TextureFactory.load_mipmapped("data/wood.png");
        _bd = new BodyDef();
        _bd.type = BodyDef.BodyType.DynamicBody;
        _shape = new PolygonShape();
        _fd = new FixtureDef();
        _fd.shape = _shape;
        _fd.friction = 0.8f;
        _fd.density = 1.0f;
        _fd.restitution = 0.2f;
        _sshape = new PolygonShape();
        _sfd = new FixtureDef();
        _sfd.isSensor = true;
        _sfd.shape = _sshape;
        _initialized = true;
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        float angle = s.angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1);
        G.gl.glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
        MiscRenderer.hqplankmesh.render(4, this._sz * 54, 54);
        G.gl.glPopMatrix();
    }

    public void render_lq() {
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        float angle = s.angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1);
        G.gl.glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
        G.gl.glScalef(this.size.x, this.size.y, 0.5f);
        MiscRenderer.Aplankmesh.render(4);
        G.gl.glPopMatrix();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void pause() {
        create_sensors();
        super.pause();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        destroy_sensors();
        super.play();
    }

    private void create_sensors() {
        if (!this.attached_a && this.fa == null) {
            _sshape.setAsBox(0.5f, 0.25f, _tmp.set((this.size.x / 2.0f) + 0.5f, 0.0f), 0.0f);
            this.fa = this.body.createFixture(_sfd);
        }
        if (!this.attached_b && this.fb == null) {
            _sshape.setAsBox(0.5f, 0.25f, _tmp.set(-((this.size.x / 2.0f) + 0.5f), 0.0f), 0.0f);
            this.fb = this.body.createFixture(_sfd);
        }
    }

    private void destroy_sensors() {
        if (this.fa != null) {
            if (this.body.getFixtureList().contains(this.fa, false)) {
                this.body.destroyFixture(this.fa);
            }
            this.fa = null;
        }
        if (this.fb != null) {
            if (this.body.getFixtureList().contains(this.fb, false)) {
                this.body.destroyFixture(this.fb);
            }
            this.fb = null;
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void step(float deltatime) {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Vector2 get_position() {
        return this.body.getPosition();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Body[] get_body_list() {
        return null;
    }

    public static void init_materials(boolean shadowed) {
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, _material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _material, 12);
    }

    public static void init_materials() {
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, _material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _material, 12);
    }

    public static void init_dark_material() {
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _material, 13);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _material, 17);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _material, 21);
    }

    public static void init_light_material() {
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _material, 12);
    }

    @Override // com.bithack.apparatus.objects.FreeObject
    public void set_layer() {
    }

    public Vector2 check_sensor_intersection(Fixture a, Plank gb) {
        float dir;
        if (a == this.fa) {
            dir = 1.0f;
        } else {
            if (a != this.fb) {
                return null;
            }
            dir = -1.0f;
        }
        float sz1 = this.size.x / 2.0f;
        float f = gb.size.x / 2.0f;
        float angle = get_angle();
        _dir.set((float) Math.cos(angle), (float) Math.sin(angle));
        _p1.set(_dir.x * dir * (sz1 + 0.5f), _dir.y * dir * (sz1 + 0.5f));
        _p1.add(get_position());
        if (gb.f.testPoint(_p1)) {
            return _p1;
        }
        return null;
    }

    public Vector2 check_sensor_intersection(Fixture a, Wheel gb) {
        _p1.set((this.size.x / 2.0f) + 0.5f, 0.0f);
        _p1.set(this.body.getWorldPoint(_p1));
        if (a.testPoint(_p1)) {
            if (gb.f.testPoint(_p1)) {
                return _p1;
            }
            return null;
        }
        _p1.set(((-this.size.x) / 2.0f) - 0.5f, 0.0f);
        _p1.set(this.body.getWorldPoint(_p1));
        if (a.testPoint(_p1)) {
            if (gb.f.testPoint(_p1)) {
                return _p1;
            }
            return null;
        }
        _p1.set((this.size.x / 2.0f) + 0.25f, 0.0f);
        _p1.set(this.body.getWorldPoint(_p1));
        if (a.testPoint(_p1)) {
            if (gb.f.testPoint(_p1)) {
                return _p1;
            }
            return null;
        }
        _p1.set(((-this.size.x) / 2.0f) - 0.25f, 0.0f);
        _p1.set(this.body.getWorldPoint(_p1));
        if (a.testPoint(_p1) && gb.f.testPoint(_p1)) {
            return _p1;
        }
        return null;
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("size")) {
            this.size.x = ((Float) value).floatValue();
            reshape();
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
        set_property("size", Float.valueOf(this.size.x));
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream s) throws IOException {
        update_properties();
        super.write_to_stream(s);
    }

    @Override // com.bithack.apparatus.objects.Bar, com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
        int i = 0;
        ApparatusApp.game.remove_potential_fixture_pair(this.body);
        if (this.f != null && this.body.getFixtureList().contains(this.f, false)) {
            this.body.destroyFixture(this.f);
        }
        _shape.setAsBox(this.size.x / 2.0f, 0.25f);
        destroy_sensors();
        create_sensors();
        this.body.setType(BodyDef.BodyType.KinematicBody);
        this.f = this.body.createFixture(_fd);
        this.body.setType(BodyDef.BodyType.DynamicBody);
        this.last_angle = 1231321.0f;
        if (this.size.x == 8.0f) {
            i = 2;
        } else if (this.size.x == 4.0f) {
            i = 1;
        }
        this._sz = i;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
