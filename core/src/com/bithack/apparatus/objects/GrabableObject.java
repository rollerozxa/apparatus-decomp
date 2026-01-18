package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GrabableObject extends BaseObject {
    private static Vector2 tmp = new Vector2();
    public Body body;
    public boolean cabled = false;
    protected Cable cable = null;
    public Vector2 _saved_pos = new Vector2();
    public float _saved_angle = 0.0f;
    private Vector2 _s_saved_pos = new Vector2();
    private float _s_saved_angle = 0.0f;
    private int _s_saved_layer = 0;
    public boolean grabbed = false;
    public int[] l_contacts = new int[2];
    public boolean sandbox_only = true;
    public int num_hinges = 0;
    public boolean fixed_layer = false;
    public boolean fixed_rotation = false;
    public boolean fixed_dynamic = false;
    public boolean culled = false;
    public BodyDef.BodyType ingame_type = BodyDef.BodyType.DynamicBody;
    public BodyDef.BodyType build_type = BodyDef.BodyType.DynamicBody;

    public abstract void reshape();

    public abstract void tja_translate(float f, float f2);

    public void pause() {
        this.body.getType();
        this.body.setType(BodyDef.BodyType.StaticBody);
        this.body.setTransform(this._saved_pos, this._saved_angle);
        this.body.setAngularDamping(Float.POSITIVE_INFINITY);
        this.body.setLinearDamping(Float.POSITIVE_INFINITY);
        this.body.setLinearVelocity(tmp.set(0.0f, 0.0f));
        this.body.setAngularVelocity(0.0f);
        if (this.fixed_dynamic) {
            this.body.setType(BodyDef.BodyType.StaticBody);
        } else {
            this.body.setType(this.build_type);
        }
    }

    public void set_active(boolean a) {
        if (this.body != null) {
            this.body.setActive(a);
        }
    }

    public void play() {
        this.body.setType(this.ingame_type);
        this._saved_angle = this.body.getAngle();
        this._saved_pos.set(this.body.getPosition());
        this.body.setLinearVelocity(tmp.set(0.0f, 0.0f));
        this.body.setAngularVelocity(0.0f);
        this.body.setAngularDamping(0.0f);
        this.body.setLinearDamping(0.0f);
        set_active(true);
        this.body.setAwake(true);
    }

    public void sandbox_save() {
        if (this.body != null) {
            this._s_saved_angle = this.body.getAngle();
            this._s_saved_pos.set(this.body.getPosition());
        }
        this._s_saved_layer = this.layer;
    }

    public void sandbox_load() {
        if (this.body != null) {
            this._saved_angle = this._s_saved_angle;
            this._saved_pos.set(this._s_saved_pos);
        }
        this.layer = this._s_saved_layer;
        pause();
    }

    @Override
    public void translate(float x, float y) {
        BodyDef.BodyType saved = this.body.getType();
        this.body.setType(BodyDef.BodyType.StaticBody);
        this.body.setTransform(tmp.set(x, y), get_angle());
        this.body.setType(saved);
        this._saved_pos.set(x, y);
    }

    @Override
    public void set_angle(float angle) {
        this._saved_angle = angle;
        BodyDef.BodyType saved = this.body.getType();
        this.body.setType(BodyDef.BodyType.StaticBody);
        this.body.setTransform(get_position(), angle);
        this.body.setType(saved);
    }

    @Override
    public float get_angle() {
        return this.body.getAngle();
    }

    public void grab() {
        this.ghost = true;
        if (this.num_hinges == 0) {
            this.body.setFixedRotation(true);
        }
        this.grabbed = true;
    }

    public void release() {
        this.ghost = false;
        this.body.setFixedRotation(false);
        tja_translate(get_position().x, get_position().y);
        this.grabbed = false;
    }

    @Override
    public void rotate(float a) {
        this._saved_angle = this.body.getAngle();
        BodyDef.BodyType saved = this.body.getType();
        this.body.setType(BodyDef.BodyType.StaticBody);
        float na = Math.round((0.0f + a) * 2.546479f) / 2.546479f;
        this.body.setTransform(this.body.getPosition(), na);
        this.body.setType(saved);
    }

    public void detach_cable() {
        if (this.cabled && this.cable != null) {
            this.cable.detach(this);
        }
    }

    @Override
    public void dispose(World world) {
        detach_cable();
        if (this.body != null) {
            world.destroyBody(this.body);
        }
    }
}
