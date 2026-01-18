package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.graphics.G;

/* loaded from: classes.dex */
public class Mine extends Explosive {
    public Mine(World world) {
        super(world);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public final void render() {
        if (!this.culled && !this.triggered) {
            Vector2 pos = this.body.getPosition();
            float angle = get_angle();
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(1.0f, 0.5f, 0.5f);
            Weight._mesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
