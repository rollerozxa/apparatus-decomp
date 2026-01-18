package com.bithack.apparatus;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.objects.BaseObject;

/* loaded from: classes.dex */
public class PCameraHandler {
    final PerspectiveCamera camera;
    private BaseObject target;
    final Vector3 velocity = new Vector3(0.0f, 0.0f, 0.0f);
    private final Vector3 target_offs = new Vector3(0.0f, 0.0f, 0.0f);
    public final Vector3 camera_pos = new Vector3(0.0f, 0.0f, 0.0f);
    public final Vector3 camera_direction = new Vector3(0.0f, 0.0f, 0.0f);
    public final Vector3 tmp = new Vector3(0.0f, 0.0f, 0.0f);
    public final Vector3 tmp2 = new Vector3(0.0f, 0.0f, 0.0f);
    private final Vector3 saved_pos = new Vector3(0.0f, 0.0f, 0.0f);
    final Vector3 displacement = new Vector3(0.7f, -0.9f, 0.0f);

    public PCameraHandler(PerspectiveCamera camera) {
        this.camera = camera;
        this.camera_pos.set(camera.position);
        this.camera_direction.set(camera.direction);
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
        tick(G.delta, false);
    }

    public void tick(float delta) {
        tick(delta, false);
    }

    public void tick(float delta, boolean update) {
        if (this.target != null) {
            Vector2 tpos = this.target.get_position();
            this.velocity.mul(Math.max(1.0f - (((G.delta / 4.0f) * 18.0f) * ((100.0f - 10) / 100.0f)), 0.0f));
            if (this.velocity.len() < 0.1f) {
                this.velocity.set(0.0f, 0.0f, 0.0f);
            }
            this.target_offs.add(this.velocity.tmp().mul(delta));
            this.tmp.set(tpos.x, tpos.y, 0.0f).add(this.target_offs.x, this.target_offs.y, 0.0f);
            Ray r = G.p_cam.getPickRay(G.realwidth / 2, G.realheight / 2);
            Intersector.intersectRayPlane(r, Game.yaxis, this.tmp2);
            this.tmp.sub(this.tmp2.x, this.tmp2.y, 0.0f);
            this.camera_pos.add(this.tmp.x * Math.abs(this.tmp.x) * delta * delta, this.tmp.y * Math.abs(this.tmp.y) * delta * delta, 0.0f);
        } else {
            int smooth = Math.min(80, Game.camera_smoothness);
            this.velocity.mul(Math.max(1.0f - (((G.delta / 4.0f) * 18.0f) * ((100.0f - smooth) / 100.0f)), 0.0f));
            if (this.velocity.len() < 0.1f) {
                this.velocity.set(0.0f, 0.0f, 0.0f);
            }
            this.camera_pos.add(this.velocity.tmp().mul(delta));
        }
        this.tmp.set(this.camera_pos.x, this.camera_pos.y, this.camera_pos.z - 4.0f).sub(this.displacement).sub(this.camera_pos).nor();
        this.camera.direction.set(this.tmp);
        if (update) {
            this.camera.update();
        }
    }

    public void update() {
        this.camera.position.set(this.camera_pos);
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

    public void displace(Vector2 v) {
        this.displacement.add(v.x, v.y, 0.0f);
        if (this.displacement.x < -10.0f) {
            this.displacement.x = -10.0f;
        } else if (this.displacement.x > 10.0f) {
            this.displacement.x = 10.0f;
        }
        if (this.displacement.y < -10.0f) {
            this.displacement.y = -10.0f;
        } else if (this.displacement.y > 10.0f) {
            this.displacement.y = 10.0f;
        }
    }
}
