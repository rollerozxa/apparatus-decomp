package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Game;
import java.io.IOException;
import java.util.jar.JarOutputStream;

public class PanelCable extends BaseCable {
    static CircleShape _s;
    public static Color color = new Color(0.5f, 0.0f, 0.0f, 1.0f);
    public static int _size = 10;

    public PanelCable(World world) {
        super(world, _size);
        this.rz = 1.55f;
        this.cable_type = 1;
    }

    public static void _init() {
        BaseRope._init();
        CableEnd._init();
    }

    @Override
    protected void create_ends(World world) {
        _bd.position.set(0.0f, 1.0f);
        this.g1 = new PanelCableEnd(world, this);
        _bd.position.set(0.0f, -(this.size - 2.0f));
        this.g2 = new PanelCableEnd(world, this);
    }

    @Override
    public void tick() {
        ((PanelCableEnd) this.g1).tick();
        ((PanelCableEnd) this.g2).tick();
        super.tick();
    }

    @Override
    public void set_property(String name, Object value) {
        if (name.equals("e1oid")) {
            ((PanelCableEnd) this.g1).saved_oid = (Integer) value;
        } else if (name.equals("e2oid")) {
            ((PanelCableEnd) this.g2).saved_oid = (Integer) value;
        }
        super.set_property(name, value);
    }

    @Override
    public void write_to_stream(JarOutputStream o) throws IOException {
        set_property("e1oid", ((PanelCableEnd) this.g1).saved_oid);
        set_property("e2oid", ((PanelCableEnd) this.g2).saved_oid);
        super.write_to_stream(o);
    }

    public void on_disconnect() {
        if (Game.mode == 3) {
            boolean z = ((PanelCableEnd) this.g1).attached_object instanceof Panel;
            boolean z2 = ((PanelCableEnd) this.g2).attached_object instanceof Panel;
        }
    }

    public void on_connect() {
        if (Game.mode == 3) {
            boolean z = ((PanelCableEnd) this.g1).attached_object instanceof Panel;
            boolean z2 = ((PanelCableEnd) this.g2).attached_object instanceof Panel;
        }
    }

    @Override
    public void draw_edges_shadow_volume(Vector3 light_pos) {
    }

    @Override
    public void render_edges() {
        ((PanelCableEnd) this.g2).render(((this.angles[this.quality - 2] + this.angles[this.quality - 3]) / 2.0f) + 90.0f);
        ((PanelCableEnd) this.g1).render(((this.angles[0] + this.angles[1]) / 2.0f) - 90.0f);
    }

    @Override
    public void play() {
        this.g1.play();
        this.g2.play();
    }

    @Override
    public void dispose(World world) {
        PanelCableEnd e1 = (PanelCableEnd) this.g1;
        PanelCableEnd e2 = (PanelCableEnd) this.g2;
        e1.dispose(world);
        e2.dispose(world);
    }

    public void detach(GrabableObject o) {
        PanelCableEnd e1 = (PanelCableEnd) this.g1;
        PanelCableEnd e2 = (PanelCableEnd) this.g2;
        if (e1.attached_object == o) {
            e1.detach();
        }
        if (e2.attached_object == o) {
            e2.detach();
        }
    }

    public Battery get_battery() {
        PanelCableEnd e1 = (PanelCableEnd) this.g1;
        PanelCableEnd e2 = (PanelCableEnd) this.g2;
        if (e1.attached && e1.attached_type == 1) {
            return e1.attached_battery;
        }
        if (e2.attached && e2.attached_type == 1) {
            return e2.attached_battery;
        }
        return null;
    }

    public Panel get_panel() {
        PanelCableEnd e1 = (PanelCableEnd) this.g1;
        PanelCableEnd e2 = (PanelCableEnd) this.g2;
        if (e1.attached && e1.attached_type == 2) {
            return e1.attached_panel;
        }
        if (e2.attached && e2.attached_type == 2) {
            return e2.attached_panel;
        }
        return null;
    }

    public RocketEngine get_rengine() {
        PanelCableEnd e1 = (PanelCableEnd) this.g1;
        PanelCableEnd e2 = (PanelCableEnd) this.g2;
        if (e1.attached && e1.attached_type == 3) {
            return e1.attached_rengine;
        }
        if (e2.attached && e2.attached_type == 3) {
            return e2.attached_rengine;
        }
        return null;
    }

    public Hub get_hub() {
        PanelCableEnd e1 = (PanelCableEnd) this.g1;
        PanelCableEnd e2 = (PanelCableEnd) this.g2;
        if (e1.attached && e1.attached_type == 4) {
            return e1.attached_hub;
        }
        if (e2.attached && e2.attached_type == 4) {
            return e2.attached_hub;
        }
        return null;
    }

    public Button get_button() {
        PanelCableEnd e1 = (PanelCableEnd) this.g1;
        PanelCableEnd e2 = (PanelCableEnd) this.g2;
        if (e1.attached && e1.attached_type == 5) {
            return e1.attached_button;
        }
        if (e2.attached && e2.attached_type == 5) {
            return e2.attached_button;
        }
        return null;
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
