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
import com.bithack.apparatus.Level;
import com.bithack.apparatus.ObjectFactory;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

public class MetalBar extends Bar {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    private static FixtureDef _sfd;
    private static PolygonShape _shape;
    private static PolygonShape _sshape;
    static int sidea;
    static int sideb;
    public static Texture texture;
    private Fixture f;
    private final World world;
    private static boolean _initialized = false;
    private static Vector2 _tmp = new Vector2();
    static Vector2 basepos1 = new Vector2();
    static Vector2 basepos2 = new Vector2();
    static Vector2 cornertop = null;
    static Vector2 dist = new Vector2();
    private static final Vector2[] sensor_pos = {new Vector2(1.0f, 1.0f), new Vector2(1.0f, -1.0f), new Vector2(-1.0f, 1.0f), new Vector2(-1.0f, -1.0f)};
    static final float[] _material = {0.2f, 0.2f, 0.2f, 0.2f, 0.5f, 0.5f, 0.5f, 0.5f, 0.9f, 0.9f, 0.9f, 1.0f, 10.0f, 0.0f, 0.0f, 0.0f};
    public Vector2 position = new Vector2();
    public boolean attached_a = false;
    public boolean attached_b = false;
    private MetalBar pending_corner = null;
    protected MetalCorner corner_a = null;
    protected MetalCorner corner_b = null;
    private Fixture[] fa = new Fixture[2];
    private Fixture[] fb = new Fixture[2];
    private int _sz = 2;

    public MetalBar(World world) {
        this.world = world;
        if (!_initialized) {
            _init();
        }
        if (!Bar._initialized) {
            Bar._init();
        }
        if (Level.version > 1) {
            _fd.friction = 0.5f;
        } else {
            _fd.friction = 0.1f;
        }
        this.body = world.createBody(_bd);
        _shape.setAsBox(4.0f, 0.5f);
        this.f = this.body.createFixture(_fd);
        this.body.setUserData(this);
        this.size.set(8.0f, 1.0f, 2.0f);
        this.properties = new BaseObject.Property[]{new BaseObject.Property("size", BaseObject.Property.Type.FLOAT, Float.valueOf(this.size.x))};
        this.ingame_type = BodyDef.BodyType.StaticBody;
        this.build_type = BodyDef.BodyType.StaticBody;
        this.z = 0.5f;
    }

    @Override
    public void rotate(float a) {
        escape_corners();
        super.rotate(a);
    }

    private void create_sensors() {
        if (!this.attached_a) {
            if (this.fa[0] == null) {
                _sshape.setAsBox(0.15f, 0.15f, _tmp.set(this.size.x / 2.0f, 0.5f), 0.0f);
                this.fa[0] = this.body.createFixture(_sfd);
                this.fa[0].setUserData(sensor_pos[0]);
            }
            if (this.fa[1] == null) {
                _sshape.setAsBox(0.15f, 0.15f, _tmp.set(this.size.x / 2.0f, -0.5f), 0.0f);
                this.fa[1] = this.body.createFixture(_sfd);
                this.fa[1].setUserData(sensor_pos[1]);
            }
        }
        if (!this.attached_b) {
            if (this.fb[0] == null) {
                _sshape.setAsBox(0.15f, 0.15f, _tmp.set(-(this.size.x / 2.0f), 0.5f), 0.0f);
                this.fb[0] = this.body.createFixture(_sfd);
                this.fb[0].setUserData(sensor_pos[2]);
            }
            if (this.fb[1] == null) {
                _sshape.setAsBox(0.15f, 0.15f, _tmp.set(-(this.size.x / 2.0f), -0.5f), 0.0f);
                this.fb[1] = this.body.createFixture(_sfd);
                this.fb[1].setUserData(sensor_pos[3]);
            }
        }
    }

    private void destroy_sensors() {
        if (this.fa[0] != null) {
            if (this.body.getFixtureList().contains(this.fa[0], false)) {
                this.body.destroyFixture(this.fa[0]);
            }
            this.fa[0] = null;
        }
        if (this.fa[1] != null) {
            if (this.body.getFixtureList().contains(this.fa[1], false)) {
                this.body.destroyFixture(this.fa[1]);
            }
            this.fa[1] = null;
        }
        if (this.fb[0] != null) {
            if (this.body.getFixtureList().contains(this.fb[0], false)) {
                this.body.destroyFixture(this.fb[0]);
            }
            this.fb[0] = null;
        }
        if (this.fb[1] != null) {
            if (this.body.getFixtureList().contains(this.fb[1], false)) {
                this.body.destroyFixture(this.fb[1]);
            }
            this.fb[1] = null;
        }
    }

    @Override
    public void render() {
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        float angle = s.angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, (this.layer * 1.5f) + 0.5f);
        G.gl.glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
        if (this.layer == 1) {
            G.gl.glScalef(1.0f, 1.0f, 0.5f);
        }
        MiscRenderer.hqmetalmesh.render(4, this._sz * 54, 54);
        G.gl.glPopMatrix();
    }

    public void render_lq() {
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        float angle = s.angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, (this.layer * 1.5f) + 0.5f);
        G.gl.glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
        G.gl.glScalef(this.size.x, this.size.y, 2.0f);
        MiscRenderer.Ametalmesh.render(4);
        G.gl.glPopMatrix();
    }

    public static void _init() {
        _initialized = true;
        texture = TextureFactory.load_mipmapped("data/metal.jpg");
        _bd = new BodyDef();
        _bd.type = BodyDef.BodyType.StaticBody;
        _bd.fixedRotation = true;
        _shape = new PolygonShape();
        _fd = new FixtureDef();
        _fd.restitution = 0.1f;
        _fd.density = 1.0f;
        _fd.shape = _shape;
        _sshape = new PolygonShape();
        _sfd = new FixtureDef();
        _sfd.isSensor = true;
        _sfd.shape = _sshape;
    }

    @Override
    public void play() {
        destroy_sensors();
        this.body.setType(BodyDef.BodyType.StaticBody);
        super.play();
    }

    @Override
    public void pause() {
        create_sensors();
        super.pause();
    }

    @Override
    public void grab() {
        this.grabbed = true;
        this.body.setType(BodyDef.BodyType.DynamicBody);
    }

    @Override
    public void release() {
        this.body.setType(BodyDef.BodyType.DynamicBody);
        translate(get_position().x, get_position().y);
        this.body.setType(BodyDef.BodyType.StaticBody);
        this.grabbed = false;
    }

    @Override
    public void translate(float x, float y) {
        if (!this.attached_b && !this.attached_a) {
            this.position.set(x, y);
            super.translate(x, y);
        } else if (this.body.getPosition().dst(x, y) > 1.0f) {
            this.body.setType(BodyDef.BodyType.DynamicBody);
            escape_corners();
            this.position.set(x, y);
            super.translate(x, y);
        }
        save_state();
    }

    public void escape_corners() {
        if (this.attached_a) {
            ApparatusApp.game.remove_corner(this.corner_a);
            this.corner_a.dispose(this.world);
        }
        if (this.attached_b) {
            ApparatusApp.game.remove_corner(this.corner_b);
            this.corner_b.dispose(this.world);
        }
    }

    @Override
    public void dispose(World world) {
        escape_corners();
        super.dispose(world);
    }

    @Override
    public void on_click() {
    }

    @Override
    public void set_property(String name, Object value) {
        if (name.equals("size")) {
            boolean reshape = false;
            float newsize = ((Float) value).floatValue();
            if (newsize != this.size.x) {
                reshape = true;
            }
            this.size.x = newsize;
            if (reshape) {
                reshape();
            }
        }
        super.set_property(name, value);
    }

    @Override
    public void update_properties() {
        set_property("size", Float.valueOf(this.size.x));
    }

    @Override
    public void write_to_stream(JarOutputStream s) throws IOException {
        update_properties();
        super.write_to_stream(s);
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
    public Body[] get_body_list() {
        return null;
    }

    public static void init_materials(boolean shadowed) {
        G.gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
    }

    public static void init_materials() {
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, _material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _material, 12);
    }

    @Override
    public void reshape() {
        int i = 0;
        ApparatusApp.game.remove_potential_fixture_pair(this.body);
        escape_corners();
        if (this.f != null && this.body.getFixtureList().contains(this.f, false)) {
            this.body.destroyFixture(this.f);
        }
        _shape.setAsBox(this.size.x / 2.0f, 0.5f);
        this.f = this.body.createFixture(_fd);
        this.last_angle = 1.2321321E7f;
        if (this.size.x == 8.0f) {
            i = 2;
        } else if (this.size.x == 4.0f) {
            i = 1;
        }
        this._sz = i;
        reshape_corners();
    }

    public void reshape_corners() {
        destroy_sensors();
        create_sensors();
    }

    public void tick() {
        if (this.pending_corner != null) {
            basepos1.add(dist);
            double angle = this.pending_corner.body.getAngle() % 6.283185307179586d;
            double angle2 = this.body.getAngle() % 6.283185307179586d;
            double adist = Math.abs((this.pending_corner.body.getAngle() % 6.283185307179586d) - ((this.body.getAngle() % 3.141592653589793d) * 2.0d));
            double pdist = basepos1.dst(basepos2);
            if (pdist < 0.10000000149011612d || adist < 0.0010000000474974513d) {
                this._saved_pos.set(this.body.getPosition());
                Vector2 aa = this.body.getPosition().add(dist);
                translate(aa.x, aa.y);
                MetalCorner c = (MetalCorner) ObjectFactory.adapter.wrap(new MetalCorner(this.world, this, this.pending_corner, sidea, sideb));
                if (!c.invalid) {
                    ApparatusApp.game.add_corner(c);
                } else {
                    c.dispose(this.world);
                    translate(this._saved_pos.x, this._saved_pos.y);
                }
            } else if (pdist < 1.2000000476837158d) {
                this._saved_pos.set(this.body.getPosition());
                Vector2 aa2 = this.body.getPosition().add(dist);
                translate(aa2.x, aa2.y);
                MetalCorner c2 = (MetalCorner) ObjectFactory.adapter.wrap(new MetalCorner(this.world, this, this.pending_corner, cornertop, basepos1, basepos2, sidea, sideb));
                if (!c2.invalid) {
                    ApparatusApp.game.add_corner(c2);
                } else {
                    c2.dispose(this.world);
                    translate(this._saved_pos.x, this._saved_pos.y);
                }
            }
            this.pending_corner = null;
        }
    }

    public void create_corner(MetalBar o, Vector2 thispos, Vector2 opos) {
        cornertop = opos;
        sidea = get_adj_sensor_world_pos(thispos, basepos1);
        sideb = o.get_adj_sensor_world_pos(opos, basepos2);
        get_sensor_dist(thispos, opos, o, dist);
        if (sidea == 0 && !this.attached_a) {
            this.pending_corner = o;
        } else if (sidea == 1 && !this.attached_b) {
            this.pending_corner = o;
        }
    }

    protected void get_sensor_dist(Vector2 a, Vector2 b, MetalBar o, Vector2 out) {
        out.set(b);
        out.x *= o.size.x / 2.0f;
        out.y *= o.size.y / 2.0f;
        out.set(o.body.getWorldPoint(out));
        Vector2 t = a.cpy();
        t.x *= this.size.x / 2.0f;
        t.y *= this.size.y / 2.0f;
        out.sub(this.body.getWorldPoint(t));
    }

    protected int get_adj_sensor_world_pos(Vector2 p, Vector2 out) {
        Vector2 tmp = null;
        int side = -1;
        if (this.fa[0] != null && p == sensor_pos[0]) {
            tmp = sensor_pos[1];
            side = 0;
        } else if (this.fa[1] != null && p == sensor_pos[1]) {
            tmp = sensor_pos[0];
            side = 0;
        } else if (this.fb[0] != null && p == sensor_pos[2]) {
            tmp = sensor_pos[3];
            side = 1;
        } else if (this.fb[1] != null && p == sensor_pos[3]) {
            tmp = sensor_pos[2];
            side = 1;
        }
        if (tmp != null) {
            out.set(tmp);
            out.x *= this.size.x / 2.0f;
            out.y *= this.size.y / 2.0f;
            out.set(this.body.getWorldPoint(out));
        }
        return side;
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
