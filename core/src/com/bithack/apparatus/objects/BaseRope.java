package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.Level;
import com.bithack.apparatus.SilhouetteMesh;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public abstract class BaseRope extends GrabableObject {
    protected static BodyDef _bd;
    protected static DistanceJointDef _distjd;
    protected static FixtureDef _fd;
    protected static RopeJointDef _ropejd;
    protected static DistanceJointDef _rubberjd;
    public static Texture _texture;
    private static SilhouetteMesh silhouette;
    float[] angles;
    public GrabableObject g1;
    public GrabableObject g2;
    private float[] lengths;
    private Point[] points;
    private Segment[] segments;
    public float size;
    private World world;
    public static Matrix4 ztransform = new Matrix4();
    public static Matrix4 idttransform = new Matrix4();
    public static Matrix4 shadow_ztransform = new Matrix4();
    public static boolean _initialized = false;
    private static Mesh shadow_mesh = null;
    private static Vector2 tmp = new Vector2();
    private static Vector2 tmp2 = new Vector2();
    private static Vector2 tmp3 = new Vector2();
    private static Vector2 tmp4 = new Vector2();
    private static final Matrix4 shear_mat = new Matrix4();
    private static Vector2 nullpos = new Vector2(0.0f, 0.0f);
    private Joint joint1 = null;
    public boolean rubber = false;
    private final Vector2[] _saved_pos = new Vector2[5];
    private final float[] _saved_angle = new float[5];
    protected int quality = 16;
    protected float rz = 0.99f;
    private final Joint joint2 = null;

    protected abstract void create_ends(World world);

    public abstract void draw_edges_shadow_volume(Vector3 vector3);

    private static class Point extends Vector2 {
        float lastx;
        float lasty;

        public Point(float x, float y) {
            this.lastx = x;
            this.x = x;
            this.lasty = y;
            this.y = y;
        }

        protected void integrate() {
            float tx = this.x;
            float ty = this.y;
            this.x += this.x - this.lastx;
            this.y += this.y - this.lasty;
            this.y -= G.delta * 1.2f;
            this.lastx = tx;
            this.lasty = ty;
        }
    }

    private static class Segment {
        public Point a;
        public Point b;
        public float dist;
        public float lastdist;

        public Segment(Point a, Point b) {
            this.a = a;
            this.b = b;
            this.dist = (float) Math.sqrt(((b.x - a.x) * (b.x - a.x)) + ((b.y - a.y) * (b.y - a.y)));
        }

        public void constrain() {
			float distx = this.b.x - this.a.x;
			float disty = this.b.y - this.a.y;
			double dist2 = Math.sqrt((double) ((distx * distx) + (disty * disty)));
			double offs = dist2 - ((double) this.dist);
			double dispy = ((((double) disty) * offs) / dist2) * 0.5d;
			double dispx = ((((double) distx) * offs) / dist2) * 0.5d;
			Point point = this.a;
			point.y = (float) (((double) point.y) + dispy);
			Point point2 = this.a;
			point2.x = (float) (((double) point2.x) + dispx);
			Point point3 = this.b;
			point3.y = (float) (((double) point3.y) - dispy);
			Point point4 = this.b;
			point4.x = (float) (((double) point4.x) - dispx);
			this.lastdist = (float) dist2;
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void save_state() {
        this.g1.save_state();
        this.g2.save_state();
    }

    public static void _init() {
        if (!_initialized) {
            shadow_ztransform.setToTranslation(0.0f, 0.0f, -0.5f);
            idttransform.idt();
            ztransform.setToTranslation(0.0f, 0.0f, 1.601f);
            _initialized = true;
            _ropejd = new RopeJointDef();
            _ropejd.collideConnected = true;
            _rubberjd = new DistanceJointDef();
            _rubberjd.collideConnected = true;
            _distjd = new DistanceJointDef();
            _distjd.collideConnected = true;
            _distjd.frequencyHz = 20.0f;
            _distjd.dampingRatio = 0.9f;
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.DynamicBody;
            _fd = new FixtureDef();
            _fd.friction = 0.1f;
            _fd.density = 0.5f;
            _fd.restitution = 0.0f;
            _fd.shape = new CircleShape();
            _fd.shape.setRadius(0.1f);
            RopeEnd._fd = new FixtureDef();
            RopeEnd._fd.friction = 0.5f;
            RopeEnd._fd.density = 1.0f;
            RopeEnd._fd.restitution = 0.1f;
            RopeEnd._fd.shape = new CircleShape();
            RopeEnd._fd.shape.setRadius(0.5f);
            _texture = TextureFactory.load("data/smooth.png");
            Vector3 test = new Vector3(Game.light_pos).nor();
            shear_mat.set(new float[]{1.0f, test.x * test.y, 0.0f, 0.0f, test.y * test.x, 1.0f, 0.0f, 0.0f, test.z * test.x, test.z * test.y, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        }
    }

    public BaseRope(World world, float size) {
        this.size = 11.0f;
        this.size = size;
        this.world = world;
        size = Level.version >= 6 ? 10.0f : size;
        create_rope();
        create_ends(world);
        this.g2.body.setTransform(new Vector2(0.0f, 1.0f), 0.0f);
        this.g1.body.setTransform(new Vector2(0.0f, -(size - 2.0f)), 0.0f);
        create_ropejoint();
        BaseObject.Property[] propertyArr = new BaseObject.Property[12];
        propertyArr[0] = new BaseObject.Property("e1x", BaseObject.Property.Type.FLOAT, 0.0f);
        propertyArr[1] = new BaseObject.Property("e1y", BaseObject.Property.Type.FLOAT, 0.0f);
        propertyArr[2] = new BaseObject.Property("e2x", BaseObject.Property.Type.FLOAT, 0.0f);
        propertyArr[3] = new BaseObject.Property("e2y", BaseObject.Property.Type.FLOAT, 0.0f);
        propertyArr[4] = new BaseObject.Property("e1id", BaseObject.Property.Type.INT, 0);
        propertyArr[5] = new BaseObject.Property("e2id", BaseObject.Property.Type.INT, 0);
        propertyArr[6] = new BaseObject.Property("e1l", BaseObject.Property.Type.INT,0);
        propertyArr[7] = new BaseObject.Property("e2l", BaseObject.Property.Type.INT, 0);
        propertyArr[8] = new BaseObject.Property("e1oid", BaseObject.Property.Type.INT, -1);
        propertyArr[9] = new BaseObject.Property("e2oid", BaseObject.Property.Type.INT, -1);
        propertyArr[10] = new BaseObject.Property("size", BaseObject.Property.Type.FLOAT, size);
        propertyArr[11] = new BaseObject.Property("type", BaseObject.Property.Type.INT, this.rubber ? 1 : 0);
        this.properties = propertyArr;
        this.fixed_rotation = true;
    }

    public void create_ropejoint() {
        if (this.joint1 != null) {
            try {
                this.world.destroyJoint(this.joint1);
            } catch (Exception e) {
            }
        }
        if (this.rubber) {
            _ropejd.bodyA = this.g1.body;
            _ropejd.bodyB = this.g2.body;
            _ropejd.maxLength = this.size * 4.0f;
            _ropejd.localAnchorA.set(0.0f, 0.0f);
            _ropejd.localAnchorB.set(0.0f, 0.0f);
            this.joint1 = this.world.createJoint(_ropejd);
            return;
        }
        _ropejd.bodyA = this.g1.body;
        _ropejd.bodyB = this.g2.body;
        _ropejd.maxLength = this.size;
        _ropejd.localAnchorA.set(0.0f, 0.0f);
        _ropejd.localAnchorB.set(0.0f, 0.0f);
        this.joint1 = this.world.createJoint(_ropejd);
    }

    public void set_elastic(boolean b) {
        if (this.rubber != b) {
            this.rubber = b;
            create_ropejoint();
        }
    }

    public void create_rope() {
        float factor = 0.4f + (0.9f * (Game.rope_quality / 100.0f));
        this.quality = (int) (this.size * factor);
        if (this.quality < 5) {
            this.quality = 5;
        }
        this.points = new Point[this.quality];
        this.segments = new Segment[this.quality - 1];
        this.lengths = new float[this.quality - 1];
        this.angles = new float[this.quality - 1];
        for (int x = 0; x < this.quality; x++) {
            this.points[x] = new Point(0.0f, (-0.5f) - (x * (this.size / this.quality)));
        }
        for (int x2 = 0; x2 < this.quality - 1; x2++) {
            this.segments[x2] = new Segment(this.points[x2], this.points[x2 + 1]);
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void set_active(boolean a) {
        this.g1.set_active(a);
        this.g2.set_active(a);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void pause() {
        this.g1.pause();
        this.g2.pause();
        stabilize();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void sandbox_save() {
        this.g1.sandbox_save();
        this.g2.sandbox_save();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void sandbox_load() {
        this.g1.sandbox_load();
        this.g2.sandbox_load();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        this.g1.play();
        this.g2.play();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    public void render_edges() {
        this.g1.render();
        this.g2.render();
    }

    public void tick() {
        Vector2 p = this.g1.get_position();
        tmp2.set(p);
        this.points[0].x = p.x;
        this.points[0].y = p.y;
        Vector2 p2 = this.g2.get_position();
        this.points[this.quality - 1].x = p2.x;
        this.points[this.quality - 1].y = p2.y;
        tmp.set(p2);
        float dist = tmp2.dst(tmp);
        tmp.sub(tmp2);
        tmp4.set(tmp);
        tmp.mul(1.0f / this.quality);
        for (int x = 1; x < this.quality - 1; x++) {
            this.points[x].integrate();
        }
        for (int i = 0; i < 3; i++) {
            for (int x2 = 0; x2 < this.quality - 1; x2++) {
                this.segments[x2].constrain();
            }
            if (dist > this.size - 0.5f) {
                for (int z = 1; z < this.quality - 1; z++) {
                    tmp3.set(tmp).mul(z).add(this.g1.get_position());
                    Vector2 t = tmp3.cpy().sub((Vector2) this.points[z]);
                    t.mul(G.delta * ((float) Math.pow(dist / this.size, 2.0d)));
                    this.points[z].add(t);
                }
            }
        }
        if (this.rubber) {
            if (dist > this.size) {
                float d = dist - this.size;
                tmp4.nor();
                tmp4.mul(100.0f * d);
                this.g1.body.applyForce(tmp4, this.g1.body.getWorldCenter(), true);
                tmp4.mul(-1.0f);
                this.g2.body.applyForce(tmp4, this.g2.body.getWorldCenter(), true);
            }
        } else if (dist > this.size - 0.5f) {
            float offs = dist - (this.size - 0.5f);
            float max = ((this.size + 0.5f) * (this.size + 0.5f)) - ((this.size - 1.0f) * (this.size - 1.0f));
            float d2 = (offs * offs) / max;
            tmp3.set(tmp).mul(1000.0f * d2);
            this.g1.body.applyForce(tmp3, this.g1.body.getWorldCenter(), true);
            tmp3.set(tmp).mul((-1000.0f) * d2);
            this.g2.body.applyForce(tmp3, this.g2.body.getWorldCenter(), true);
        }
        Vector2 p3 = this.g1.get_position();
        this.points[0].x = p3.x;
        this.points[0].y = p3.y;
        Vector2 p4 = this.g2.get_position();
        this.points[this.quality - 1].x = p4.x;
        this.points[this.quality - 1].y = p4.y;
        for (int x3 = 0; x3 < this.quality - 1; x3++) {
            tmp.set(this.points[x3 + 1].x, this.points[x3 + 1].y).sub(this.points[x3].x, this.points[x3].y);
            this.lengths[x3] = tmp.len();
            this.angles[x3] = (float) (Math.atan2(tmp.y, tmp.x) * 57.29577951308232d);
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        for (int x = 0; x < this.quality - 1; x++) {
            G.batch.draw(_texture, this.points[x].x, this.points[x].y, 0.0f, 0.1f, 1.0f, 0.15f, this.lengths[x], 1.0f, this.angles[x], 4, 0, 0, 8, false, false);
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void step(float deltatime) {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void translate(float x, float y) {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Vector2 get_position() {
        return nullpos.set(this.g1.get_position()).add(this.g2.get_position()).mul(0.5f);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public float get_angle() {
        return 0.0f;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void dispose(World world) {
        if (this.joint1 != null) {
            world.destroyJoint(this.joint1);
        }
        this.g1.dispose(world);
        this.g2.dispose(world);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void set_angle(float angle) {
    }

    public void set_size(float size) {
        if (size != this.size) {
            this.size = size;
            create_rope();
            create_ropejoint();
            stabilize();
        }
    }

    public void draw_shadow_volume(Vector3 lightdir) {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("e1x")) {
            tmp.set((Float) value, this.g1.body.getPosition().y);
            this.g1.translate(tmp.x, tmp.y);
        } else if (name.equals("e1y")) {
            tmp.set(this.g1.body.getPosition().x, (Float) value);
            this.g1.translate(tmp.x, tmp.y);
        } else if (name.equals("e2x")) {
            tmp.set((Float) value, this.g2.body.getPosition().y);
            this.g2.translate(tmp.x, tmp.y);
        } else if (name.equals("e2y")) {
            tmp.set(this.g2.body.getPosition().x, (Float) value);
            this.g2.translate(tmp.x, tmp.y);
        } else if (name.equals("e1id")) {
            this.g1.__unique_id = (Integer) value;
        } else if (name.equals("e2id")) {
            this.g2.__unique_id = (Integer) value;
        } else if (name.equals("e2l")) {
            this.g2.layer = (Integer) value;
        } else if (name.equals("e1l")) {
            this.g1.layer = (Integer) value;
        } else if (name.equals("size")) {
            float nsize = (Float) value;
            if (nsize != this.size) {
                this.size = nsize;
                create_rope();
                create_ropejoint();
            }
        } else if (name.equals("type")) {
            this.rubber = (Integer) value == 1;
            create_ropejoint();
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
        super.set_property("e1x", this.g1.body.getPosition().x);
        super.set_property("e1y", this.g1.body.getPosition().y);
        super.set_property("e2x", this.g2.body.getPosition().x);
        super.set_property("e2y", this.g2.body.getPosition().y);
        super.set_property("e1id", this.g1.__unique_id);
        super.set_property("e2id", this.g2.__unique_id);
        super.set_property("e1l", this.g1.layer);
        super.set_property("e2l", this.g2.layer);
        super.set_property("size", this.size);
        super.set_property("type", this.rubber ? 1 : 0);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream o) throws IOException {
        update_properties();
        super.write_to_stream(o);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
        this.g1.reshape();
        this.g2.reshape();
    }

    public void stabilize() {
        Vector2 p = this.g1.get_position();
        tmp2.set(p);
        this.points[0].x = p.x;
        this.points[0].y = p.y;
        Vector2 p2 = this.g2.get_position();
        this.points[this.quality - 1].x = p2.x;
        this.points[this.quality - 1].y = p2.y;
        tmp.set(p2);
        tmp.sub(tmp2).mul(1.0f / this.quality);
        for (int x = 1; x < this.quality - 1; x++) {
            tmp3.set(tmp).mul(x).add(this.g1.get_position());
            this.points[x].x = tmp3.x;
            this.points[x].y = tmp3.y;
            this.points[x].lastx = tmp3.x;
            this.points[x].lasty = tmp3.y;
        }
        for (int x2 = 0; x2 < 150; x2++) {
            tick();
        }
    }
}
