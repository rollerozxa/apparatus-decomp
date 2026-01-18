package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class Rope extends BaseRope {
    public static Color color = new Color(0.087f, 0.165f, 0.249f, 1.0f);
    public static int _size = 11;

    public Rope(World world) {
        super(world, _size);
        this.rz = 1.56f;
        this.fixed_rotation = true;
    }

    @Override
    protected void create_ends(World world) {
        _bd.position.set(0.0f, 1.0f);
        this.g1 = new RopeEnd(world, this);
        _bd.position.set(0.0f, -(this.size - 2.0f));
        this.g2 = new RopeEnd(world, this);
    }

    @Override
    public void draw_edges_shadow_volume(Vector3 light_pos) {
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
