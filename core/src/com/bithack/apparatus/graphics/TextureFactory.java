package com.bithack.apparatus.graphics;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class TextureFactory {
    public static final Map<String, TextureHandle> map = new HashMap();
    public static final Map<Integer, TextureHandle> by_id = new HashMap();
    static int texture_id = 0;

    public static class TextureHandle {
        public int id = TextureFactory.texture_id;
        public Texture texture;

        public TextureHandle(Texture texture) {
            this.texture = texture;
            TextureFactory.texture_id++;
        }
    }

    private static void add(String key, TextureHandle h) {
        map.put(key, h);
        by_id.put(new Integer(h.id), h);
    }

    private static TextureHandle load_handle(String filename, Files.FileType filetype, Texture.TextureFilter minfilter, Texture.TextureFilter maxfilter, Texture.TextureWrap wrap_x, Texture.TextureWrap wrap_y) {
        String key = String.valueOf(filename) + minfilter.toString() + maxfilter.toString() + wrap_x.toString() + wrap_y.toString();
        TextureHandle t = map.get(key);
        if (t != null) {
            return t;
        }
        Texture tx = new Texture(Gdx.files.getFileHandle(filename, filetype));
        tx.setFilter(minfilter, maxfilter);
        tx.setWrap(wrap_x, wrap_y);
        TextureHandle t2 = new TextureHandle(tx);
        add(key, t2);
        return t2;
    }

    private static TextureHandle load_mipmapped_handle(String filename, Files.FileType filetype, Texture.TextureFilter minfilter, Texture.TextureFilter maxfilter, Texture.TextureWrap wrap_x, Texture.TextureWrap wrap_y) {
        String key = String.valueOf(filename) + minfilter.toString() + maxfilter.toString() + wrap_x.toString() + wrap_y.toString();
        TextureHandle t = map.get(key);
        if (t != null) {
            return t;
        }
        Texture tx = new Texture(Gdx.files.getFileHandle(filename, filetype), true);
        tx.setFilter(minfilter, maxfilter);
        tx.setWrap(wrap_x, wrap_y);
        TextureHandle t2 = new TextureHandle(tx);
        add(key, t2);
        return t2;
    }

    public static Texture load(String filename, Files.FileType filetype, Texture.TextureFilter minfilter, Texture.TextureFilter maxfilter, Texture.TextureWrap wrap_x, Texture.TextureWrap wrap_y) {
        return load_handle(filename, filetype, minfilter, maxfilter, wrap_x, wrap_y).texture;
    }

    public static Texture load(String filename) {
        return load_handle(filename, Files.FileType.Internal, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear, Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat).texture;
    }

    public static Texture load_unfiltered(String filename) {
        return load_handle(filename, Files.FileType.Internal, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat).texture;
    }

    public static void unload(Texture t) {
    }

    public static Texture load_mipmapped(String filename) {
        return load_mipmapped_handle(filename, Files.FileType.Internal, Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest, Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat).texture;
    }
}
