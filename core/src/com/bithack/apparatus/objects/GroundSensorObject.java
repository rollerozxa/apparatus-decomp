package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/* loaded from: classes.dex */
public abstract class GroundSensorObject extends BaseObject {
    private boolean is_on_ground = false;
    private int ground_contacts = 0;

    protected boolean is_on_ground() {
        return this.is_on_ground;
    }

    public void inc_contacts() {
        this.ground_contacts++;
        this.is_on_ground = true;
    }

    public void dec_contacts() {
        this.ground_contacts--;
        if (this.ground_contacts <= 0) {
            this.is_on_ground = false;
            this.ground_contacts = 0;
        }
    }

    public void attach_ground_sensor(Body body, float x, float y, float w, float h) {
        PolygonShape shape = new PolygonShape();
        FixtureDef fd = new FixtureDef();
        fd.isSensor = true;
        fd.shape = shape;
        fd.density = 0.001f;
        shape.setAsBox(x, y, new Vector2(w, h), 0.0f);
        body.createFixture(fd);
    }
}
