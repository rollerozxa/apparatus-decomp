package com.bithack.apparatus.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public class Damper extends GrabableObject {
    public static final float MAX_FORCE = 1000.0f;
    public static final float MAX_SPEED = 200.0f;
    protected static BodyDef _bd;
    protected static FixtureDef _fd;
    protected static FixtureDef _sfd;
    public DamperEnd g1;
    public DamperEnd g2;
    private final Joint joint2;
    public float size;
    private World world;
    public static Matrix4 ztransform = new Matrix4();
    public static Matrix4 idttransform = new Matrix4();
    public static Matrix4 shadow_ztransform = new Matrix4();
    public static boolean _initialized = false;
    protected static PrismaticJointDef _jd = new PrismaticJointDef();
    private static Vector2 tmp = new Vector2();
    private static Vector2 tmp2 = new Vector2();
    private static Vector2 tmp3 = new Vector2();
    private static Vector2 tmp4 = new Vector2();
    private static Vector2 nullpos = new Vector2(0.0f, 0.0f);
    public Joint joint1 = null;
    private final Vector2[] _saved_pos = new Vector2[5];
    private final float[] _saved_angle = new float[5];
    public float speed = 50.0f;
    public float force = 100.0f;

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
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.DynamicBody;
            _fd = new FixtureDef();
            _fd.friction = 0.1f;
            _fd.density = 0.5f;
            _fd.restitution = 0.0f;
            _fd.shape = new CircleShape();
            _fd.shape.setRadius(0.1f);
            _sfd = new FixtureDef();
            _sfd.density = 0.01f;
            _sfd.isSensor = true;
            _sfd.shape = new PolygonShape();
            ((PolygonShape) _sfd.shape).setAsBox(0.5f, 0.4f, new Vector2(0.0f, -1.4f), 0.0f);
            DamperEnd._fd = new FixtureDef();
            DamperEnd._fd.friction = 0.5f;
            DamperEnd._fd.density = 1.0f;
            DamperEnd._fd.restitution = 0.1f;
            DamperEnd._fd.shape = new PolygonShape();
            DamperEnd._fd.isSensor = false;
            ((PolygonShape) DamperEnd._fd.shape).setAsBox(0.5f, 1.0f);
        }
    }

    protected void create_ends(World world) {
        Gdx.app.log("create ", "first");
        _bd.position.set(0.0f, this.size / 4.0f);
        _bd.angle = 3.1415927f;
        this.g1 = new DamperEnd(world, this, 0);
        Gdx.app.log("create ", "second");
        _bd.position.set(0.0f, (-this.size) / 4.0f);
        _bd.angle = 0.0f;
        this.g2 = new DamperEnd(world, this, 1);
        Gdx.app.log("setting angle", "");
        Gdx.app.log("angle was set", "");
        _bd.angle = 0.0f;
        _bd.position.set(0.0f, 0.0f);
    }

    public Damper(World world, float size) {
        this.size = 2.0f;
        _init();
        this.joint2 = null;
        this.size = 2.0f;
        this.world = world;
        create_ends(world);
        this.properties = new BaseObject.Property[]{new BaseObject.Property("e1x", BaseObject.Property.Type.FLOAT, new Float(0.0f)), new BaseObject.Property("e1y", BaseObject.Property.Type.FLOAT, new Float(0.0f)), new BaseObject.Property("e2x", BaseObject.Property.Type.FLOAT, new Float(0.0f)), new BaseObject.Property("e2y", BaseObject.Property.Type.FLOAT, new Float(0.0f)), new BaseObject.Property("e1a", BaseObject.Property.Type.FLOAT, new Float(0.0f)), new BaseObject.Property("e2a", BaseObject.Property.Type.FLOAT, new Float(0.0f)), new BaseObject.Property("e1id", BaseObject.Property.Type.INT, new Integer(0)), new BaseObject.Property("e2id", BaseObject.Property.Type.INT, new Integer(0)), new BaseObject.Property("force", BaseObject.Property.Type.FLOAT, new Float(100.0f)), new BaseObject.Property("speed", BaseObject.Property.Type.FLOAT, new Float(50.0f)), new BaseObject.Property("size", BaseObject.Property.Type.FLOAT, new Float(size))};
        this.fixed_rotation = true;
        create_ropejoint();
    }

    public void create_ropejoint() {
        if (this.joint1 != null) {
            try {
                this.world.destroyJoint(this.joint1);
                this.joint1 = null;
            } catch (Exception e) {
            }
        }
        _jd.initialize(this.g1.body, this.g2.body, this.g1.body.getWorldCenter(), new Vector2(0.0f, -1.0f));
        _jd.lowerTranslation = (-this.size) / 2.0f;
        _jd.upperTranslation = this.size / 2.0f;
        _jd.collideConnected = false;
        _jd.enableLimit = true;
        _jd.enableMotor = false;
        _jd.maxMotorForce = this.force;
        _jd.motorSpeed = this.speed;
        this.joint1 = this.world.createJoint(_jd);
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
        ((PrismaticJoint) this.joint1).enableMotor(false);
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
        ((PrismaticJoint) this.joint1).enableMotor(true);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    public void render_edge_boxes() {
        this.g1.render_box();
        this.g2.render_box();
    }

    public void render_edges() {
        this.g1.render();
        this.g2.render();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void step(float deltatime) {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void translate(float x, float y) {
        this.g1.layer = this.layer;
        this.g2.layer = this.layer;
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
        this.joint1 = null;
        this.g1.dispose(world);
        this.g2.dispose(world);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void set_angle(float angle) {
    }

    public void set_size(float size) {
        if (size != this.size) {
            this.size = size;
            create_ropejoint();
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("e1x")) {
            tmp.set(((Float) value).floatValue(), this.g1.body.getPosition().y);
            this.g1.translate(tmp.x, tmp.y);
        } else if (name.equals("e1y")) {
            tmp.set(this.g1.body.getPosition().x, ((Float) value).floatValue());
            this.g1.translate(tmp.x, tmp.y);
        } else if (name.equals("e2x")) {
            tmp.set(((Float) value).floatValue(), this.g2.body.getPosition().y);
            this.g2.translate(tmp.x, tmp.y);
        } else if (name.equals("e2y")) {
            tmp.set(this.g2.body.getPosition().x, ((Float) value).floatValue());
            this.g2.translate(tmp.x, tmp.y);
        } else if (name.equals("e1id")) {
            this.g1.__unique_id = ((Integer) value).intValue();
        } else if (name.equals("e1a")) {
            this.g1.set_angle(((Float) value).floatValue());
        } else if (name.equals("e2a")) {
            this.g2.set_angle(((Float) value).floatValue());
        } else if (name.equals("e2id")) {
            this.g2.__unique_id = ((Integer) value).intValue();
        } else if (name.equals("force")) {
            this.force = ((Float) value).floatValue();
            ((PrismaticJoint) this.joint1).setMaxMotorForce(this.force);
        } else if (name.equals("speed")) {
            this.speed = ((Float) value).floatValue();
            ((PrismaticJoint) this.joint1).setMotorSpeed(this.speed);
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
        super.set_property("e1x", Float.valueOf(this.g1.body.getPosition().x));
        super.set_property("e1y", Float.valueOf(this.g1.body.getPosition().y));
        super.set_property("e2x", Float.valueOf(this.g2.body.getPosition().x));
        super.set_property("e2y", Float.valueOf(this.g2.body.getPosition().y));
        super.set_property("e1a", Float.valueOf(this.g1.body.getAngle()));
        super.set_property("e2a", Float.valueOf(this.g2.body.getAngle()));
        super.set_property("e1id", Integer.valueOf(this.g1.__unique_id));
        super.set_property("e2id", Integer.valueOf(this.g2.__unique_id));
        super.set_property("size", new Float(this.size));
        super.set_property("speed", Float.valueOf(this.speed));
        super.set_property("force", Float.valueOf(this.force));
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

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
    }

    public void update_motor() {
        ((PrismaticJoint) this.joint1).setMotorSpeed(this.speed);
        ((PrismaticJoint) this.joint1).setMaxMotorForce(this.force);
    }
}
