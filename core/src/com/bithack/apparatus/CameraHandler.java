package com.bithack.apparatus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.objects.BaseObject;

public class CameraHandler {
    private final OrthographicCamera camera;
    public Vector3 camera_pos;
    private BaseObject target;
    public final Vector3 velocity = new Vector3(0.0f, 0.0f, 0.0f);
    private final Vector3 target_offs = new Vector3(0.0f, 0.0f, 0.0f);
    private final Vector3 saved_pos = new Vector3(0.0f, 0.0f, 0.0f);

    public CameraHandler(OrthographicCamera camera) {
        this.camera = camera;
        this.camera_pos = camera.position;
    }

    public void set_target(BaseObject target, Vector3 offs) {
        this.target = target;
        this.target_offs.set(offs.x, offs.y, 0.0f);
    }

    public void set_target(BaseObject target, float offs_x, float offs_y) {
        this.target = target;
        this.target_offs.set(offs_x, offs_y, 0.0f);
    }

    public void release_target() {
        this.target = null;
        this.target_offs.set(0.0f, 0.0f, 0.0f);
        this.velocity.set(0.0f, 0.0f, 0.0f);
    }

    public void tick() {
        this.velocity.mul(1.0f - (6.0f * G.delta));
        if (this.velocity.len() < 0.1f) {
            this.velocity.set(0.0f, 0.0f, 0.0f);
        }
        this.camera_pos.add(this.velocity.tmp().mul(G.delta));
        this.camera.update();
    }

    public void add_velocity(Vector2 vel) {
        this.velocity.add(vel.x, vel.y, 0.0f);
    }

    public void add_velocity(float x, float y) {
        this.velocity.add(x, y, 0.0f);
    }

    public void save() {
        this.saved_pos.set(this.camera_pos);
    }

    public void load() {
        this.camera_pos.set(this.saved_pos);
    }
}
