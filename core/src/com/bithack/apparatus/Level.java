package com.bithack.apparatus;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.objects.BaseObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/* loaded from: classes.dex */
public class Level {
    public static final int TYPE_APPARATUS = 0;
    public static final int TYPE_CHALLENGE = 1;
    public static final int TYPE_CHALLENGE_INTERACTIVE = 2;
    private static final int VERSION = 8;
    public int background;
    public boolean challenge;
    public String community_id;
    public String description;
    private JarFile jar;
    public String min_version;
    public String name;
    public int num_planks;
    public int num_wheels;
    private JarEntry objectentry;
    private BaseObject[] objects;
    private ArrayList[] pipeline;
    public int size;
    public Vector2 start_anchor;
    public String tags;
    public String type;
    private World world;
    public static int version_override = -1;
    public static int version = 8;
    private static File tmpfile = null;

    public static Level open(World world, File file) {
        try {
            return new Level(world, new JarFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Level open(World world, String filename) {
        try {
            return new Level(world, new JarFile(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/" + filename + ".jar"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Level open_internal(World world, String filename) throws IOException {
        FileHandle h = Gdx.files.getFileHandle("data/level/" + filename + ".jar", Files.FileType.Internal);
        InputStream in = h.read();
        if (tmpfile != null) {
            tmpfile.delete();
            tmpfile = null;
        }
        File tmp = Settings.get_tmp_file();
        OutputStream out = new FileOutputStream(tmp);
        byte[] buf = new byte[1024];
        int tot = 0;
        while (true) {
            int len = in.read(buf);
            if (len > 0) {
                out.write(buf, 0, len);
                tot += len;
            } else {
                out.close();
                tmpfile = tmp;
                try {
                    Level l = new Level(world, new JarFile(tmp));
                    tmpfile.delete();
                    tmpfile = null;
                    return l;
                } catch (Exception e) {
                    e.printStackTrace();
                    tmpfile.delete();
                    tmpfile = null;
                    return null;
                }
            }
        }
    }

    public static Level create(World world) {
        return new Level(world);
    }

    public void set_objects(BaseObject[] objects) {
        this.objects = objects;
    }

    public BaseObject[] get_objects() {
        return this.objects;
    }

    private Level(World world, JarFile jar) throws Exception {
        this(world);
        this.jar = jar;
        Attributes a = jar.getManifest().getMainAttributes();
        this.min_version = a.getValue("Level-Min-Version");
        this.name = a.getValue("Level-Name");
        this.description = a.getValue("Level-Description");
        this.tags = a.getValue("Level-Tags");
        this.community_id = a.getValue("Level-ID");
        this.type = a.getValue("Level-type");
        if (this.type == null) {
            this.type = "challenge";
        }
        version = 0;
        if (this.min_version == null) {
            this.min_version = "0";
        } else {
            int i = Integer.parseInt(this.min_version);
            version = i;
            if (i > 8) {
                Settings.msg(L.get("update_required"));
                throw new Exception();
            }
        }
        String in_background = a.getValue("Level-Background");
        if (in_background == null) {
            this.background = -1;
        } else {
            this.background = Integer.parseInt(in_background);
            if (this.background > 11) {
                this.background = 11;
            }
            if (this.background < -1) {
                this.background = -1;
            }
        }
        if (version_override != -1) {
            version = version_override;
        }
        String start = a.getValue("Level-StartAnchor");
        if (start != null) {
            String[] sxy = start.split(" ");
            if (sxy.length == 2) {
                this.start_anchor.set(Float.parseFloat(sxy[0]), Float.parseFloat(sxy[1]));
            } else {
                this.start_anchor.set(0.0f, 0.0f);
            }
        } else {
            this.start_anchor.set(0.0f, 0.0f);
        }
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry e = entries.nextElement();
            if (e.getName().equals("objects")) {
                this.objects = ObjectFactory.read_from_stream(world, jar.getInputStream(e));
                this.objectentry = e;
            } else if (e.getName().equals("descr")) {
                this.description = "";
                InputStream in = jar.getInputStream(e);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    } else {
                        sb.append(line);
                    }
                }
                this.description = sb.toString();
                System.out.println(sb.toString());
                br.close();
            }
        }
    }

    public void reload_objects() throws IOException {
        for (BaseObject o : this.objects) {
            o.dispose(this.world);
        }
        this.objects = null;
        System.gc();
        if (this.objectentry != null) {
            this.objects = ObjectFactory.read_from_stream(this.world, this.jar.getInputStream(this.objectentry));
        }
    }

    private Level(World world) {
        this.objects = new BaseObject[0];
        this.pipeline = new ArrayList[3];
        this.min_version = "0";
        this.name = "";
        this.description = "";
        this.tags = "";
        this.type = "challenge";
        this.community_id = "";
        this.start_anchor = new Vector2(0.0f, 0.0f);
        this.jar = null;
        this.objectentry = null;
        this.size = 1;
        this.challenge = true;
        this.num_planks = 0;
        this.num_wheels = 0;
        this.name = "";
        this.description = "";
        this.tags = "";
        this.community_id = "";
        this.type = "challenge";
        version = 8;
        this.world = world;
    }

    public boolean save_jar(File file) throws IOException {
        File f;
        File f2 = new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels");
        f2.mkdirs();
        if (file == null) {
            f = new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/" + this.name.replace("/", "_") + ".jar");
        } else {
            f = file;
        }
        Manifest manifest = new Manifest();
        Attributes a = manifest.getMainAttributes();
        if (this.tags == null) {
            this.tags = "";
        }
        if (this.description == null) {
            this.description = "";
        }
        if (this.type == null) {
            this.type = "challenge";
        }
        if (this.community_id == null) {
            this.community_id = "";
        }
        if (this.name == null) {
            this.name = "";
        }
        a.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        a.putValue("Level-Name", this.name);
        a.putValue("Level-Description", "");
        a.putValue("Level-Tags", this.tags);
        a.putValue("Level-Background", Integer.toString(this.background));
        a.putValue("Level-Type", this.type);
        a.putValue("Level-ID", this.community_id);
        a.putValue("Level-Min-Version", Integer.toString(8));
        try {
            FileOutputStream fs = new FileOutputStream(f);
            JarOutputStream out = new JarOutputStream(fs, manifest);
            JarEntry entry = new JarEntry("objects");
            out.putNextEntry(entry);
            out.write((byte) ((this.objects.length >> 8) & 255));
            out.write((byte) (this.objects.length & 255));
            for (BaseObject o : this.objects) {
                o.write_to_stream(out);
            }
            out.closeEntry();
            if (this.description != null) {
                JarEntry entry2 = new JarEntry("descr");
                out.putNextEntry(entry2);
                out.write(this.description.getBytes());
                out.closeEntry();
            }
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
