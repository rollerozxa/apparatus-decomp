package com.bithack.apparatus.objects;

import com.badlogic.gdx.physics.box2d.World;

public abstract class BaseCable extends BaseRope {
    public static final int ELEC = 2;
    public static final int PANEL = 1;
    int cable_type;

    public BaseCable(World world, float size) {
        super(world, size);
    }
}
