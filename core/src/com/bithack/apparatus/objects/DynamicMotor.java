package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.ObjectFactory;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;

public class DynamicMotor extends BaseMotor {
    static FixtureDef _fd;
    static PolygonShape _shape;
    public Fixture f;
    protected float last_angle;
    private Fixture[] sensors;
    protected Mesh silhouette_mesh;
    private static boolean initialized = false;
    private static final Vector2 tmp2 = new Vector2();
    private static Vector2[] sensor_pos = {new Vector2(-1.0f, 0.0f), new Vector2(1.0f, 0.0f), new Vector2(0.0f, 1.0f), new Vector2(0.0f, -1.0f)};

    private static void init() {
        if (!initialized) {
            initialized = true;
            _shape = new PolygonShape();
            _fd = new FixtureDef();
            _fd.isSensor = true;
            _fd.density = 0.0f;
            _fd.shape = _shape;
        }
    }

    private void create_sensors() {
        destroy_sensors();
        for (int x = 0; x < 4; x++) {
            _shape.setAsBox((Math.abs(sensor_pos[x].y) * 0.5f) + 0.25f, (Math.abs(sensor_pos[x].x) * 0.5f) + 0.25f, sensor_pos[x], 0.0f);
            this.sensors[x] = this.body.createFixture(_fd);
            this.sensors[x].setUserData(sensor_pos[x]);
        }
    }

    private void destroy_sensors() {
        for (int x = 0; x < 4; x++) {
            if (this.sensors[x] != null) {
                if (this.body.getFixtureList().contains(this.sensors[x], false)) {
                    this.body.destroyFixture(this.sensors[x]);
                }
                this.sensors[x] = null;
            }
        }
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
        if (this.cabled) {
            if (((CableEnd) this.cable.g1).attached_object == this) {
                ((CableEnd) this.cable.g1).update_pos();
            } else if (((CableEnd) this.cable.g2).attached_object == this) {
                ((CableEnd) this.cable.g2).update_pos();
            }
        }
    }

    @Override
    public void rotate(float ro) {
        super.rotate(ro);
        if (this.cabled) {
            Vector2 pos = get_position();
            Matrix3 rot = new Matrix3();
            rot.setToRotation((float) (get_angle() * 57.29577951308232d));
            Vector2 tmp = new Vector2();
            tmp.set(0.0f, -0.5f);
            tmp.mul(rot);
            tmp.add(pos.x, pos.y);
            CableEnd e1 = (CableEnd) this.cable.g1;
            CableEnd e2 = (CableEnd) this.cable.g2;
            if (e1.attached_object == this) {
                e1.translate(tmp.x, tmp.y);
            } else if (e2.attached_object == this) {
                e2.translate(tmp.x, tmp.y);
            }
        }
    }

    public DynamicMotor(World world) {
        super(world);
        this.last_angle = 4512.0f;
        this.layer = 0;
        this.sandbox_only = false;
        this.build_type = BodyDef.BodyType.DynamicBody;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
    }

    @Override
    protected void create_body() {
        init();
        this.sensors = new Fixture[4];
        this.body = ObjectFactory.create_rectangular_body(this.world, 0.6f, 0.6f, 1.0f, 0.2f, 0.1f);
        this.f = this.body.getFixtureList().get(0);
        this.body.setUserData(this);
        create_sensors();
    }

    @Override
    public void play() {
        destroy_sensors();
        this.body.setType(BodyDef.BodyType.DynamicBody);
        super.play();
    }

    @Override
    public void pause() {
        create_sensors();
        this.body.setType(BodyDef.BodyType.DynamicBody);
        super.pause();
    }

    public void render_deco() {
        Vector2 pos = get_state().position;
        float angle = get_state().angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, 1.27f + this.layer);
        G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
        G.gl.glScalef(this.dir * 0.4f, 0.4f, 1.0f);
        MiscRenderer.draw_textured_box();
        G.gl.glPopMatrix();
    }

    @Override
    public void render() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, 0.9f);
            G.gl.glScalef(0.5f, 0.5f, 0.1f);
            MiscRenderer.draw_cylinder();
            G.gl.glPopMatrix();
        }
    }

    public void render_box() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef(get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(1.2f, 1.2f, 0.5f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    public void render_hinge() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, 1.5f);
            G.gl.glScalef(0.1f, 0.1f, 1.0f);
            MiscRenderer.draw_smallcylinder();
            G.gl.glPopMatrix();
        }
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
