package com.bithack.apparatus.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.bithack.apparatus.objects.BaseObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class Pipeline {
    public static final int BATCH = 4;
    public static final int COLOR = 2;
    public static final int CUSTOM = 3;
    public static final int SPRITE = 1;
    public static final int TEXTURE = 0;
    private HashMap<Integer, ArrayList> texture_pipeline = new HashMap<>();
    private HashMap<Integer, ArrayList> sprite_pipeline = new HashMap<>();
    private ArrayList<BaseObject> color_pipeline = new ArrayList<>();
    private ArrayList<BaseObject> custom_pipeline = new ArrayList<>();
    private ArrayList<BaseObject> batch_pipeline = new ArrayList<>();

    public void remove(BaseObject o) {
        switch (o.pipeline) {
            case 0:
                Integer id = o.texture_id;
                ArrayList<BaseObject> l = this.texture_pipeline.get(id);
                if (l != null) {
                    l.remove(o);
                    break;
                }
                break;
            case 1:
                Integer id2 = o.texture_id;
                ArrayList<BaseObject> l2 = this.sprite_pipeline.get(id2);
                if (l2 != null) {
                    l2.remove(o);
                    break;
                }
                break;
            case 2:
                this.color_pipeline.remove(o);
                break;
            case 4:
                this.batch_pipeline.remove(o);
                break;
        }
    }

    public void add(BaseObject o) {
        switch (o.pipeline) {
            case 0:
                Integer id = o.texture_id;
                ArrayList<BaseObject> l = this.texture_pipeline.get(id);
                if (l == null) {
                    l = new ArrayList<>();
                    this.texture_pipeline.put(id, l);
                }
                l.add(o);
                break;
            case 1:
                Integer id2 = o.texture_id;
                ArrayList<BaseObject> l2 = this.sprite_pipeline.get(id2);
                if (l2 == null) {
                    l2 = new ArrayList<>();
                    this.sprite_pipeline.put(id2, l2);
                }
                l2.add(o);
                break;
            case 2:
                this.color_pipeline.add(o);
                break;
            case 4:
                this.batch_pipeline.add(o);
                break;
        }
    }

    public void clear() {
        this.texture_pipeline.clear();
        this.sprite_pipeline.clear();
        this.color_pipeline.clear();
        this.custom_pipeline.clear();
        this.batch_pipeline.clear();
    }

    public void render_all() {
        G.gl.glEnable(3553);
        for (Map.Entry<Integer, ArrayList> entry : this.texture_pipeline.entrySet()) {
            Integer k = entry.getKey();
            ArrayList<BaseObject> v = entry.getValue();
            Texture t = TextureFactory.by_id.get(k).texture;
            t.bind();
            for (BaseObject o : v) {
                o.render();
            }
        }
        G.gl.glDisable(3553);
        if (!this.color_pipeline.isEmpty()) {
            for (BaseObject o2 : this.color_pipeline) {
                o2.render();
            }
        }
    }

    public void render_batch() {
        for (BaseObject o : this.batch_pipeline) {
            o.render();
        }
    }
}
