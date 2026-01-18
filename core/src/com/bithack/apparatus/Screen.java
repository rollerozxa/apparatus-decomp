package com.bithack.apparatus;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class Screen {
    public static final int STATE_PAUSED = 1;
    public static final int STATE_RUNNING = 0;
    protected ArrayList<Action> scheduled_actions = new ArrayList<>();

    public static abstract class Action {
        protected Screen parent;

        public abstract void perform();
    }

    public abstract boolean ready();

    public abstract void render();

    public abstract void resume();

    public abstract boolean screen_to_world(int i, int i2, Vector2 vector2);

    public abstract int tick();

    public void schedule_action(Action a) {
        this.scheduled_actions.add(a);
        a.parent = this;
    }

    public void pause() {
    }

    public void dispose() {
    }
}
