package com.bithack.apparatus.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public abstract class BaseMotor extends JointObject {
    public static Texture _dirtex;
    private static RevoluteJointDef _jd;
    public Body attached_body;
    public GrabableObject attached_object;
    protected World world;
    private static boolean _initialized = false;
    protected static Vector2 _tmp = new Vector2();
    private boolean attached = false;
    private int attached_id = 0;
    public Vector2 position = new Vector2();
    RevoluteJoint joint = null;
    public float dir = 1.0f;
    public boolean fixed = true;
    private float motor_speed = 0.0f;
    private float motor_torque = 0.0f;

    protected abstract void create_body();

    public BaseMotor(World world) {
        if (!_initialized) {
            _init();
        }
        Wheel._init();
        Hinge._init();
        this.world = world;
        create_body();
        this.properties = new BaseObject.Property[]{
                new BaseObject.Property("oid", BaseObject.Property.Type.INT, -1),
                new BaseObject.Property("attached", BaseObject.Property.Type.BOOLEAN, Boolean.FALSE),
                new BaseObject.Property("dir", BaseObject.Property.Type.FLOAT, 1.0f)};
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void translate(float x, float y) {
        this.position.set(x, y);
        this.body.setTransform(this.position, get_angle());
        super.translate(x, y);
    }

    public static void _init() {
        if (!_initialized) {
            _jd = new RevoluteJointDef();
            _jd.collideConnected = false;
            _dirtex = TextureFactory.load("data/dir.png");
            _initialized = true;
        }
    }

    public void joint_play() {
        if (this.attached) {
            recreate_joint();
            update();
        }
    }

    public void joint_pause() {
        if (this.attached) {
            recreate_joint();
            this.joint.enableMotor(false);
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        super.play();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void pause() {
        super.pause();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
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

    public void tick() {
    }

    public void load(ArrayList<GrabableObject> objects) {
        if (this.attached) {
            Iterator<GrabableObject> it = objects.iterator();
            while (it.hasNext()) {
                GrabableObject o = it.next();
                if (o.__unique_id == this.attached_id) {
                    if (!Game.sandbox && Game.level_type == 1) {
                        o.fixed_dynamic = true;
                    }
                    attach(o);
                    return;
                }
            }
        }
    }

    public void attach(GrabableObject b) {
        if (this.attached_body == null) {
            this.attached_body = b.body;
            this.attached_id = b.__unique_id;
            this.attached_object = b;
            this.attached = true;
            _jd.initialize(this.body, this.attached_body, this.body.getPosition());
            this.joint = (RevoluteJoint) this.world.createJoint(_jd);
            b.num_hinges++;
            this.num_hinges++;
        }
    }

    public void detach() {
        if (this.joint != null) {
            this.world.destroyJoint(this.joint);
            this.joint = null;
        }
        if (this.attached_object != null) {
            GrabableObject grabableObject = this.attached_object;
            grabableObject.num_hinges--;
            this.attached_body = null;
            this.attached_object = null;
            this.attached = false;
            this.num_hinges--;
        }
        Gdx.app.log("detach", "num hinges:" + this.num_hinges);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("oid")) {
            this.attached_id = (Integer) value;
        } else if (name.equals("attached")) {
            this.attached = (Boolean) value;
        } else if (name.equals("dir")) {
            this.dir = (Float) value;
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
        set_property("oid", this.attached_id);
        set_property("dir", this.dir);
        set_property("attached", this.attached ? Boolean.TRUE : Boolean.FALSE);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream s) throws IOException {
        update_properties();
        super.write_to_stream(s);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
    }

    public void set_input(float f, float g) {
        this.motor_speed = 8.0f * f;
        this.motor_torque = 9000.0f * g;
    }

    private void recreate_joint() {
        if (this.joint != null) {
            this.world.destroyJoint(this.joint);
        }
        _jd.initialize(this.body, this.attached_body, this.body.getPosition());
        this.joint = (RevoluteJoint) this.world.createJoint(_jd);
    }

    public void update() {
        if (this.attached) {
            if (this.motor_speed > 0.001f && this.motor_torque > 0.001f && this.cabled && ((this.cable.get_battery() != null && this.cable.get_battery().on) || this.cable.get_hub() != null)) {
                this.dir = Math.round(this.dir);
                this.joint.setMotorSpeed(this.motor_speed * this.dir);
                this.joint.setMaxMotorTorque(this.motor_torque);
                if (Game.mode == 3) {
                    this.joint.enableMotor(true);
                    return;
                }
                return;
            }
            this.joint.setMotorSpeed(0.0f);
            this.joint.setMaxMotorTorque(0.0f);
            this.joint.enableMotor(false);
        }
    }
}
