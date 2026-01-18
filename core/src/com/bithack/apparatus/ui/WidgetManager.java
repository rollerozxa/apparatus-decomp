package com.bithack.apparatus.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.TextureFactory;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class WidgetManager {
    private final Texture ui_texture;
    private WidgetValueCallback w_callback;
    public final List<Widget> widgets = new ArrayList();
    private Widget last_touch = null;
    private Widget[] last_touched = new Widget[20];
    private int tolerance = 16;

    public WidgetManager(String ui_texture_file, WidgetValueCallback callback) {
        this.w_callback = callback;
        this.ui_texture = TextureFactory.load("data/ui/" + ui_texture_file, Files.FileType.Internal, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    }

    public void set_tolerance(int t) {
        this.tolerance = t;
    }

    public void render_all(SpriteBatch batch) {
        G.batch.setProjectionMatrix(G.cam_p.combined);
        for (Widget w : this.widgets) {
            if (!w.disabled) {
                this.ui_texture.bind();
                w.render(this.ui_texture, null);
            }
        }
    }

    public void disable(Widget widget) {
        widget.disabled = true;
    }

    public void enable(Widget widget) {
        widget.disabled = false;
    }

    protected Widget query(int x, int y) {
        for (Widget w : this.widgets) {
            if (!w.disabled && x > w.x - this.tolerance && y > w.y - this.tolerance && x < w.x + w.width + this.tolerance && y < w.y + w.height + this.tolerance) {
                return w;
            }
        }
        return null;
    }

    public boolean touch_down(int x, int y, int ptr) {
        this.last_touched[ptr] = null;
        Widget w = query(x, y);
        if (w == null) {
            return false;
        }
        int x2 = x - w.x;
        if (x2 < 0) {
            x2 = 0;
        }
        if (x2 > w.width) {
            x2 = w.width;
        }
        int y2 = y - w.y;
        if (y2 < 0) {
            y2 = 0;
        }
        if (y2 > w.height) {
            y2 = w.height;
        }
        w.touch_down_local(x2, y2);
        this.last_touched[ptr] = w;
        return true;
    }

    public boolean touch_down(int x, int y) {
        this.last_touch = null;
        Widget w = query(x, y);
        if (w == null) {
            return false;
        }
        int x2 = x - w.x;
        if (x2 < 0) {
            x2 = 0;
        }
        if (x2 > w.width) {
            x2 = w.width;
        }
        int y2 = y - w.y;
        if (y2 < 0) {
            y2 = 0;
        }
        if (y2 > w.height) {
            y2 = w.height;
        }
        w.touch_down_local(x2, y2);
        this.last_touch = w;
        return true;
    }

    public boolean touch_drag(int x, int y, int ptr) {
        Widget w;
        if (this.last_touched[ptr] != null) {
            w = this.last_touched[ptr];
        } else {
            w = query(x, y);
        }
        if (w != null) {
            int x2 = x - w.x;
            if (x2 < 0) {
                x2 = 0;
            }
            if (x2 > w.width) {
                x2 = w.width;
            }
            int y2 = y - w.y;
            if (y2 < 0) {
                y2 = 0;
            }
            if (y2 > w.height) {
                y2 = w.height;
            }
            w.touch_drag_local(x2, y2);
            return true;
        }
        return false;
    }

    public boolean touch_drag(int x, int y) {
        Widget w;
        if (this.last_touch != null) {
            w = this.last_touch;
        } else {
            w = query(x, y);
        }
        if (w != null) {
            int x2 = x - w.x;
            if (x2 < 0) {
                x2 = 0;
            }
            if (x2 > w.width) {
                x2 = w.width;
            }
            int y2 = y - w.y;
            if (y2 < 0) {
                y2 = 0;
            }
            if (y2 > w.height) {
                y2 = w.height;
            }
            w.touch_drag_local(x2, y2);
            return true;
        }
        return false;
    }

    public boolean touch_up(int x, int y, int ptr) {
        Widget w;
        if (this.last_touched[ptr] != null) {
            w = this.last_touched[ptr];
        } else {
            w = query(x, y);
        }
        if (w != null) {
            int x2 = x - w.x;
            if (x2 < 0) {
                x2 = 0;
            }
            if (x2 > w.width) {
                x2 = w.width;
            }
            int y2 = y - w.y;
            if (y2 < 0) {
                y2 = 0;
            }
            if (y2 > w.height) {
                y2 = w.height;
            }
            w.touch_up_local(x2, y2);
            return true;
        }
        return false;
    }

    public boolean touch_up(int x, int y) {
        Widget w;
        if (this.last_touch != null) {
            w = this.last_touch;
        } else {
            w = query(x, y);
        }
        if (w != null) {
            int x2 = x - w.x;
            if (x2 < 0) {
                x2 = 0;
            }
            if (x2 > w.width) {
                x2 = w.width;
            }
            int y2 = y - w.y;
            if (y2 < 0) {
                y2 = 0;
            }
            if (y2 > w.height) {
                y2 = w.height;
            }
            w.touch_up_local(x2, y2);
            return true;
        }
        return false;
    }

    public void add_widget(Widget w, int x, int y) {
        w.callback = this.w_callback;
        w.x = x;
        w.y = y;
        this.widgets.add(w);
    }
}
