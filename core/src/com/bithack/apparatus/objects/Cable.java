package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import java.io.IOException;
import java.util.jar.JarOutputStream;

public class Cable extends BaseCable {
    static CircleShape _s;
    public static Color color = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static int _size = 18;

    public Cable(World world) {
        super(world, _size);
        this.rz = 1.55f;
        this.fixed_rotation = true;
        this.cable_type = 2;
    }

    public static void _init() {
        BaseRope._init();
        CableEnd._init();
    }

    @Override
    protected void create_ends(World world) {
        _bd.position.set(0.0f, 1.0f);
        this.g1 = new CableEnd(world, this);
        _bd.position.set(0.0f, -(this.size - 2.0f));
        this.g2 = new CableEnd(world, this);
    }

    @Override
    public void tick() {
        ((CableEnd) this.g1).tick();
        ((CableEnd) this.g2).tick();
        super.tick();
    }

    @Override
    public void play() {
        this.g1.play();
        this.g2.play();
    }

    @Override
    public void set_property(String name, Object value) {
        if (name.equals("e1oid")) {
            ((CableEnd) this.g1).saved_oid = (Integer) value;
        } else if (name.equals("e2oid")) {
            ((CableEnd) this.g2).saved_oid = (Integer) value;
        }
        super.set_property(name, value);
    }

    @Override
    public void write_to_stream(JarOutputStream o) throws IOException {
        set_property("e1oid", ((CableEnd) this.g1).saved_oid);
        set_property("e2oid", ((CableEnd) this.g2).saved_oid);
        super.write_to_stream(o);
    }

    public Battery get_battery() {
        if (((CableEnd) this.g1).attached_object instanceof Battery) {
            return (Battery) ((CableEnd) this.g1).attached_object;
        }
        if (((CableEnd) this.g2).attached_object instanceof Battery) {
            return (Battery) ((CableEnd) this.g2).attached_object;
        }
        return null;
    }

    public BaseMotor get_motor() {
        if (((CableEnd) this.g1).attached_object instanceof BaseMotor) {
            return (BaseMotor) ((CableEnd) this.g1).attached_object;
        }
        if (((CableEnd) this.g2).attached_object instanceof BaseMotor) {
            return (BaseMotor) ((CableEnd) this.g2).attached_object;
        }
        return null;
    }

    public void on_disconnect() {
        update();
    }

    public void on_connect() {
        update();
    }

    @Override
    public void draw_edges_shadow_volume(Vector3 light_pos) {
    }

    @Override
    public void render_edges() {
        ((CableEnd) this.g2).render(((this.angles[this.quality - 2] + this.angles[this.quality - 3]) / 2.0f) + 90.0f);
        ((CableEnd) this.g1).render(((this.angles[0] + this.angles[1]) / 2.0f) - 90.0f);
    }

    @Override
    public void dispose(World world) {
        CableEnd e1 = (CableEnd) this.g1;
        CableEnd e2 = (CableEnd) this.g2;
        e1.dispose(world);
        e2.dispose(world);
    }

    public void detach(GrabableObject o) {
        CableEnd e1 = (CableEnd) this.g1;
        CableEnd e2 = (CableEnd) this.g2;
        if (e1.attached_object == o) {
            e1.detach();
        }
        if (e2.attached_object == o) {
            e2.detach();
        }
    }

    public void update() {
        Hub h = get_hub();
        if (h != null) {
            h.update();
            return;
        }
        BaseMotor m = get_motor();
        Battery b = get_battery();
        if (m != null && b != null) {
            m.set_input(b.voltage * b.output, b.current * b.output);
            m.update();
        } else if (m != null) {
            m.set_input(0.0f, 0.0f);
            m.update();
        }
    }

    public Hub get_hub() {
        if (((CableEnd) this.g1).attached_object instanceof Hub) {
            return (Hub) ((CableEnd) this.g1).attached_object;
        }
        if (((CableEnd) this.g2).attached_object instanceof Hub) {
            return (Hub) ((CableEnd) this.g2).attached_object;
        }
        return null;
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
