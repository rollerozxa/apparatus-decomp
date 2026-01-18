package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.Pipeline;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.menu.LevelMenu;
import com.bithack.apparatus.objects.Bar;
import com.bithack.apparatus.objects.BaseMotor;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.BaseRope;
import com.bithack.apparatus.objects.BaseRopeEnd;
import com.bithack.apparatus.objects.Battery;
import com.bithack.apparatus.objects.Bucket;
import com.bithack.apparatus.objects.Button;
import com.bithack.apparatus.objects.Cable;
import com.bithack.apparatus.objects.CableEnd;
import com.bithack.apparatus.objects.ChristmasGift;
import com.bithack.apparatus.objects.Damper;
import com.bithack.apparatus.objects.DamperEnd;
import com.bithack.apparatus.objects.DynamicMotor;
import com.bithack.apparatus.objects.GrabableObject;
import com.bithack.apparatus.objects.Hinge;
import com.bithack.apparatus.objects.Hub;
import com.bithack.apparatus.objects.Knob;
import com.bithack.apparatus.objects.Marble;
import com.bithack.apparatus.objects.MetalBar;
import com.bithack.apparatus.objects.MetalCorner;
import com.bithack.apparatus.objects.MetalWheel;
import com.bithack.apparatus.objects.Mine;
import com.bithack.apparatus.objects.Panel;
import com.bithack.apparatus.objects.PanelCable;
import com.bithack.apparatus.objects.PanelCableEnd;
import com.bithack.apparatus.objects.Plank;
import com.bithack.apparatus.objects.RocketEngine;
import com.bithack.apparatus.objects.Rope;
import com.bithack.apparatus.objects.RopeEnd;
import com.bithack.apparatus.objects.StaticMotor;
import com.bithack.apparatus.objects.Weight;
import com.bithack.apparatus.objects.Wheel;
import com.bithack.apparatus.ui.HorizontalSliderWidget;
import com.bithack.apparatus.ui.Widget;
import com.bithack.apparatus.ui.WidgetManager;
import com.bithack.apparatus.ui.WidgetValueCallback;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Game extends Screen implements InputProcessor, WidgetValueCallback {
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_GRAB = 1;
    // no 2?
    public static final int MODE_PLAY = 3;
    public static final int MODE_PAUSE = 4;
    public static int mode = MODE_DEFAULT;

    static final int STATE_PLAYING = 0;
    static final int STATE_PAUSED = 1;
    static final int STATE_FINISHED = 2;
    static final int STATE_FAIL = 3;
    public int state = STATE_PLAYING;

    static final int HINGE_WRENCH = 0;
    static final int HINGE_HAMMER = 1;
    public int hinge_type = 0;

    private static final int WIDGET_SIZE = 0;
    private final HorizontalSliderWidget widget_size;
    private static final int WIDGET_VOLTAGE = 1;
    private final HorizontalSliderWidget widget_voltage;
    private static final int WIDGET_CURRENT = 2;
    private final HorizontalSliderWidget widget_current;
    private static final int WIDGET_DIR_CW = 3;
    private static final int WIDGET_DIR_CCW = 4;
    private static final int WIDGET_VELOCITY = 5;
    private static final int WIDGET_SIZEB = 6;
    private final HorizontalSliderWidget widget_sizeb;
    private static final int WIDGET_ELASTICITY = 7;
    private final HorizontalSliderWidget widget_elasticity;
    private static final int WIDGET_THRUST = 8;
    private final HorizontalSliderWidget widget_thrust;
    private static final int WIDGET_DSPEED = 9;
    private final HorizontalSliderWidget widget_dspeed;
    private static final int WIDGET_DFORCE = 10;
    private final HorizontalSliderWidget widget_dforce;
    private boolean[] widget = new boolean[10];
    private final WidgetManager widgets;

    private static final float T_EPSILON = 1.5f;

    static final float[] _def_material = {0.5f, 0.5f, 0.5f, 1.0f, 0.75f, 0.75f, 0.75f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 10.0f, 0.0f, 0.0f, 0.0f};
    static final float[] _metal_material = {0.0f, 0.0f, 0.0f, 1.0f, 0.2f, 0.2f, 0.2f, 1.0f, 0.8f, 0.8f, 0.8f, 1.0f, 3.0f, 0.0f, 0.0f, 0.0f};
    private static MouseJointDef _mjd = new MouseJointDef();
    private static boolean a_down = false;
    public static int autosave_id = -1;
    static Texture bgtex;
    static Texture bloomtex;
    public static boolean camera_reset = true;
    public static int camera_smoothness = 50;
    public static int camera_speed = 40;
    public static final ArrayList<ConnectAnim> connectanims = new ArrayList<>();
    private static boolean d_down = false;
    public static boolean do_connectanims = true;
    public static boolean draw_fps = false;
    public static boolean enable_bg = true;
    public static boolean enable_bloom = true;
    public static boolean enable_hqmeshes = true;
    public static boolean enable_menu = true;
    public static boolean enable_multithreading = false;
    public static boolean enable_music = false;
    public static boolean enable_reflections = true;
    public static boolean enable_shadows = true;
    public static boolean enable_sound = false;
    public static boolean fix_bottombar = false;
    public static boolean force_disable_shadows = false;
    public static boolean from_community = false;
    public static boolean from_game = true;
    public static boolean from_sandbox = false;
    private static boolean has_multitouch;
    static int id_counter = 0;
    public static int level_type = 1;
    public static final float[] light_pos = {-0.3f, 0.5f, 1.0f, 0.0f};

    static float[] nangle = new float[5];
    static Texture newbgtex;
    public static int physics_stability = 1;
    public static int rope_quality = 100;
    static Texture rotatetex;
    private static boolean s_down = false;
    public static boolean sandbox = false;
    public static boolean testing_challenge = false;
    static boolean tracing = false;
    private static boolean w_down = false;
    public static World world;
    static Plane yaxis = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 1.0f));
    static Plane yaxis0 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 0.0f));
    static Plane yaxis05 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 0.5f));
    private float _last_dist = 0.0f;
    private Vector2 _last_touch = new Vector2();
    private Vector2[] _last_vec = new Vector2[3];
    final FloatBuffer _light_pos = BufferUtils.newFloatBuffer(4);
    private final RevoluteJointDef _rjd = new RevoluteJointDef();
    private Vector2 _tmp = new Vector2();
    private Vector2 _tmp2 = new Vector2();
    private Vector2 _tmp3 = new Vector2();
    private Vector2 _tmpv = new Vector2();
    private Vector2[] _touch_vec = new Vector2[3];
    float accx = 0.0f;
    float accy = 0.0f;
    Panel active_panel = null;
    private boolean allow_swipe = true;
    private int autosave_interval = 30;
    public int background_n = -1;
    private final Texture btntex;
    public PCameraHandler camera_h;
    private boolean commit_next = false;
    ContactHandler contact_handler;
    Box2DDebugRenderer debug = new Box2DDebugRenderer();
    public boolean disable_undo = false;
    private boolean do_autosave = false;
    private boolean do_save = false;
    private BodyDef drag_bd;
    private Body drag_body;
    private Vector2 drag_body_target = new Vector2();
    private FixtureDef drag_fd;
    private boolean dragging = false;
    private boolean dragging_ghost = false;
    ContactFilter falsefilter = new ContactFilter() {
        /* class com.bithack.apparatus.Game.AnonymousClass4 */

        @Override
        public boolean shouldCollide(Fixture arg0, Fixture arg1) {
            return false;
        }
    };
    private float finished_fade = 0.0f;
    private Vector2 fpos = new Vector2();
    private long game_start = 0;
    GrabableObject ghost_object = null;
    private Vector2 grab_offs = new Vector2(0.0f, 0.0f);
    GrabableObject grabbed_object = null;
    private Body ground;
    private Joint ground_joint;
    private final Texture hammertex;

    private ContactHandler.FixturePair hingepair = null;
    protected final ArrayList<Hinge> hinges = new ArrayList<>();
    private boolean hingeselect = false;
    private Texture hingeselecttex;
    private SimpleContactHandler ingame_contact_handler = new SimpleContactHandler(this, null);
    private Vector3 iresult = new Vector3();
    private long last_autosave = 0;
    GrabableObject last_grabbed = null;
    private long last_touch_time = 0;
    private long last_zoom = 0;
    private int left_menu_cache_id;
    Level level;
    int level_category = -1;
    public String level_filename = null;
    private int level_id = -1;
    int level_n = 0;
    final FloatBuffer light_ambient = BufferUtils.newFloatBuffer(4);
    final FloatBuffer light_dark = BufferUtils.newFloatBuffer(4);
    final FloatBuffer light_diffuse = BufferUtils.newFloatBuffer(4);
    final FloatBuffer light_specular = BufferUtils.newFloatBuffer(4);
    Vector3 lightdir;
    private int lowfpscount = 0;
    private boolean lowfpsfixed = false;
    private Texture lvlcompletetex;
    private final SpriteCache menu_cache;
    private Mesh metalmesh = null;
    private boolean modified = false;
    private MouseJoint mousejoint;
    private String msg = null;
    private Texture nextleveltex;
    int num_balls = 0;
    int num_balls_in_goal = 0;
    private int num_touch_points = 0;
    private int object_menu_cache_id;
    public final ObjectManager om = new ObjectManager();
    private int open_animate_dir = 0;
    private Matrix4 open_animate_matrix = new Matrix4();
    private float open_animate_time = 0.0f;
    private int open_sandbox_category = -1;
    private boolean pending_follow = false;
    private boolean pending_ghost = false;
    private final Pipeline pipeline;
    private boolean prevent_nail = false;
    private GrabableObject query_check = null;
        QueryCallback query_find_object_exact = new QueryCallback() {
        /* class com.bithack.apparatus.Game.AnonymousClass1 */

        @Override
        public boolean reportFixture(Fixture fixt) {
            Body b = fixt.getBody();
            if (b.getUserData() instanceof GrabableObject) {
                if (Game.mode != MODE_PLAY && ((((GrabableObject) b.getUserData()).sandbox_only || ((GrabableObject) b.getUserData()).fixed_dynamic) && !Game.sandbox)) {
                    return true;
                }
                if (Game.this.query_input_layer == 1) {
                    if (((GrabableObject) b.getUserData()).layer >= 1) {
                        return true;
                    }
                } else if (Game.this.query_input_layer == 2) {
                    if (((GrabableObject) b.getUserData()).layer != 1) {
                        return true;
                    }
                } else if (Game.this.query_input_layer == 3 && ((GrabableObject) b.getUserData()).layer != 2) {
                    return true;
                }
                GrabableObject o = (GrabableObject) b.getUserData();
                if (fixt.isSensor() && (o instanceof Plank)) {
                    return true;
                }
                if (fixt.testPoint(Game.this.query_input_pos)) {
                    if (Game.mode == MODE_PLAY && !(o instanceof Plank) && !(o instanceof Panel) && !(o instanceof Wheel) && !(o instanceof ChristmasGift) && !(o instanceof Marble) && !(o instanceof Weight) && !(o instanceof RocketEngine)) {
                        return true;
                    }
                    Game.this.query_result_body = b;
                    return false;
                }
            }
            return true;
        }
    };
    QueryCallback query_find_object = new QueryCallback() {
        /* class com.bithack.apparatus.Game.AnonymousClass2 */

        @Override
        public boolean reportFixture(Fixture fixt) {
            Body b = fixt.getBody();
            if ((b.getUserData() instanceof GrabableObject) && ((Game.mode == MODE_PLAY || ((!((GrabableObject) b.getUserData()).sandbox_only && !((GrabableObject) b.getUserData()).fixed_dynamic) || Game.sandbox)) && (Game.this.query_input_layer != 1 ? Game.this.query_input_layer != 2 ? Game.this.query_input_layer != 3 || ((GrabableObject) b.getUserData()).layer == 2 : ((GrabableObject) b.getUserData()).layer == 1 : ((GrabableObject) b.getUserData()).layer < 1))) {
                GrabableObject o = (GrabableObject) b.getUserData();
                if ((!fixt.isSensor() || !(o instanceof Plank)) && (Game.mode != MODE_PLAY || (o instanceof Plank) || (o instanceof Panel) || (o instanceof Wheel) || (o instanceof ChristmasGift) || (o instanceof Marble) || (o instanceof Weight) || (o instanceof RocketEngine))) {
                    if (fixt.testPoint(Game.this.query_input_pos)) {
                        Game.this.query_result_body = b;
                        Game.this.query_result_dist2 = b.getPosition().dst2(Game.this.query_input_pos);
                    } else if ((Game.this.query_result_body == null || Game.this.query_result_dist2 > b.getPosition().dst2(Game.this.query_input_pos)) && (fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.9f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.9f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.9f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.9f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.45f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.45f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.45f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.45f)))) {
                        Game.this.query_result_body = b;
                        Game.this.query_result_dist2 = b.getPosition().dst2(Game.this.query_input_pos);
                    }
                }
            }
            return true;
        }
    };
    QueryCallback query_check_drag = new QueryCallback() {
        /* class com.bithack.apparatus.Game.AnonymousClass3 */

        @Override
        public boolean reportFixture(Fixture fixt) {
            Body b = fixt.getBody();
            if (b.getUserData() != Game.this.query_check) {
                return true;
            }
            if (!fixt.testPoint(Game.this.query_input_pos) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.9f, 0.0f)) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(0.9f, 0.0f)) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.9f)) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.9f)) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.45f, 0.0f)) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(0.45f, 0.0f)) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.45f)) && !fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.45f))) {
                return true;
            }
            Game.this.query_result_body = b;
            return true;
        }
    };
    private int query_input_layer = 1;
    private Vector2 query_input_pos = null;
    private int query_result = -1;
    private Body query_result_body = null;
    private float query_result_dist2 = 0.0f;
    public boolean ready = false;
    private int rotate_dir;
    private Vector2 rotate_point = new Vector2(0.0f, 0.0f);
    private boolean rotating = false;
    private int sandbox_categories_cache_id;
    private int[] sandbox_category_cache_id = new int[5];
    private Vector2 scroll_vec = new Vector2();
    private Mesh shadowmesh = null;
    private SimulationThread sim_thread = null;
    private int special_menu_cache_id;

    private long time_accum = 0;
    private long time_last = 0;
    private final Vector3 tmp3 = new Vector3();
    private final Vector3 tmp32 = new Vector3();
    private final ApparatusApp tp;
    public final UndoManager um = new UndoManager(this);
    private boolean undo_begun = false;
    private int undo_cache_id;

    private Vector2 wrench_anim_pos = new Vector2();
    private long wrench_anim_start = 0;
    private final Texture wrenchtex;
    private Plane yaxis2 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 1.5f));
    private Plane yaxis3 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 3.0f));

    public Game(ApparatusApp tp2) {
        this.tp = tp2;
        this.contact_handler = new ContactHandler(this);
        this.lightdir = new Vector3(light_pos);
        this.lightdir.mul(-1.0f);
        this.lightdir.nor();
        for (int x = 0; x < 3; x++) {
            this._touch_vec[x] = new Vector2();
        }
        for (int x2 = 0; x2 < 3; x2++) {
            this._last_vec[x2] = new Vector2();
        }
        world = new World(new Vector2(0.0f, -10.0f), true);
        world.setContinuousPhysics(false);
        world.setAutoClearForces(true);
        world.setContactFilter(this.contact_handler);
        world.setContactListener(this.contact_handler);
        this.light_dark.put(new float[]{0.35f, 0.35f, 0.35f, 1.0f});
        this.light_dark.rewind();
        this.light_ambient.put(new float[]{0.6f, 0.6f, 0.6f, 1.0f});
        this.light_ambient.rewind();
        this.light_diffuse.put(new float[]{0.9f, 0.9f, 0.9f, 1.0f});
        this.light_diffuse.rewind();
        this.light_specular.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        this.light_specular.rewind();
        Vector3 light = new Vector3(light_pos);
        light.nor();
        this._light_pos.put(new float[]{light.x, light.y, light.z, 0.0f});
        this._light_pos.rewind();
        this.drag_bd = new BodyDef();
        this.drag_bd.type = BodyDef.BodyType.DynamicBody;
        this.drag_fd = new FixtureDef();
        this.drag_fd.shape = new CircleShape();
        ((CircleShape) this.drag_fd.shape).setRadius(1.0f);
        this.pipeline = new Pipeline();
        this.camera_h = new PCameraHandler(G.p_cam);
        this.btntex = TextureFactory.load_unfiltered("data/btns.png");
        this.wrenchtex = TextureFactory.load("data/wrench.png");
        this.hammertex = TextureFactory.load("data/hammer.png");
        Plank._init();
        Cable._init();
        Weight._init();
        Marble._init();
        Hinge._init();
        BaseMotor._init();
        Wheel._init();
        Bar._init();
        MetalBar._init();
        Rope._init();
        Bucket._init();
        RocketEngine._init();
        IlluminationManager.init(this);
        this.menu_cache = new SpriteCache();
        generate_caches();

        this.widgets = new WidgetManager("uicontrols.png", this);

        HorizontalSliderWidget horizontalSliderWidget = new HorizontalSliderWidget(0, 180, 1.0f);
        this.widget_size = horizontalSliderWidget;
        this.widgets.add_widget(horizontalSliderWidget, G.width - 320, G.height - 48);

        HorizontalSliderWidget horizontalSliderWidget2 = new HorizontalSliderWidget(7, 64, 1.0f);
        this.widget_elasticity = horizontalSliderWidget2;
        this.widgets.add_widget(horizontalSliderWidget2, (G.width - 320) - 100, G.height - 48);

        HorizontalSliderWidget horizontalSliderWidget3 = new HorizontalSliderWidget(2, 180);
        this.widget_current = horizontalSliderWidget3;
        this.widgets.add_widget(horizontalSliderWidget3, G.width - 256, G.height - 48);

        HorizontalSliderWidget horizontalSliderWidget4 = new HorizontalSliderWidget(1, 180);
        this.widget_voltage = horizontalSliderWidget4;
        this.widgets.add_widget(horizontalSliderWidget4, ((G.width - 256) - 180) - 16, G.height - 48);

        HorizontalSliderWidget horizontalSliderWidget5 = new HorizontalSliderWidget(8, 180);
        this.widget_thrust = horizontalSliderWidget5;
        this.widgets.add_widget(horizontalSliderWidget5, G.width - 320, G.height - 48);

        HorizontalSliderWidget horizontalSliderWidget6 = new HorizontalSliderWidget(6, 64, 2.0f);
        this.widget_sizeb = horizontalSliderWidget6;
        this.widgets.add_widget(horizontalSliderWidget6, (G.width - 320) - 250, G.height - 48);

        HorizontalSliderWidget horizontalSliderWidget7 = new HorizontalSliderWidget(9, 180);
        this.widget_dspeed = horizontalSliderWidget7;
        this.widgets.add_widget(horizontalSliderWidget7, G.width - 320, G.height - 48);

        HorizontalSliderWidget horizontalSliderWidget8 = new HorizontalSliderWidget(10, 180);
        this.widget_dforce = horizontalSliderWidget8;
        this.widgets.add_widget(horizontalSliderWidget8, ((G.width - 320) - 180) - 16, G.height - 48);

        this.widgets.disable(this.widget_size);
        this.widgets.disable(this.widget_elasticity);
        this.widgets.disable(this.widget_sizeb);
        this.widgets.disable(this.widget_voltage);
        this.widgets.disable(this.widget_current);
        this.widgets.disable(this.widget_thrust);
        this.widgets.disable(this.widget_dspeed);
        this.widgets.disable(this.widget_dforce);
        String tmp = Settings.get("camerareset");
        camera_reset = tmp == null || tmp.equals("yes") || tmp.isEmpty();
        String tmp2 = Settings.get("camerasmoothness");
        camera_smoothness = (tmp2 == null || tmp2.isEmpty()) ? camera_smoothness : Integer.parseInt(tmp2);
        String tmp4 = Settings.get("cameraspeed");
        camera_speed = (tmp4 == null || tmp4.equals("")) ? camera_speed : Integer.parseInt(tmp4);
        newbgtex = TextureFactory.load("data/testnewbg.jpg");
        bloomtex = TextureFactory.load("data/bloom.png");
        rotatetex = TextureFactory.load("data/rotate.png");
        this.hingeselecttex = TextureFactory.load("data/hingeselect.png");
        this.nextleveltex = TextureFactory.load_unfiltered("data/nextlvl.png");
        set_bg(0);
        this.lvlcompletetex = TextureFactory.load_unfiltered("data/lvlcomplete.png");
    }

    private void generate_caches() {
        this.menu_cache.setProjectionMatrix(G.cam_p.combined);
        this.menu_cache.beginCache();
        for (int x = 0; x < 5; x++) {
            this.menu_cache.add(this.btntex, G.width - ((x + 1) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, (x + 16) * 48, 48, 48, false, false);
        }
        this.sandbox_categories_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 112, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 720, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 168, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, 0, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 224, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, 48, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 280, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, 96, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 336, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, Input.Keys.NUMPAD_0, 48, 48, false, false);
        this.sandbox_category_cache_id[0] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 112, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 288, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 168, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 336, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 224, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 672, 48, 48, false, false);
        this.sandbox_category_cache_id[1] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        for (int x2 = 8; x2 < 14; x2++) {
            this.menu_cache.add(this.btntex, G.width - (((x2 + 2) - 8) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, x2 * 48, 48, 48, false, false);
        }
        this.sandbox_category_cache_id[2] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        for (int x3 = 3; x3 < 6; x3++) {
            this.menu_cache.add(this.btntex, G.width - (((x3 + 2) - 3) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, x3 * 48, 48, 48, false, false);
        }
        this.sandbox_category_cache_id[3] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        for (int x4 = 0; x4 < 3; x4++) {
            this.menu_cache.add(this.btntex, G.width - ((x4 + 2) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, x4 * 48, 48, 48, false, false);
        }
        this.sandbox_category_cache_id[4] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, 8.0f, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 0, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 720, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 432, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 48, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((((G.height - 48) - 8) - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 768, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((((((G.height - 48) - 8) - 48) - 8) - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 816, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 56, G.height - 56, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 432, 48, 48, false, false);
        this.left_menu_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.setColor(Color.WHITE);
        this.menu_cache.add(this.btntex, 8.0f, (((G.height - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 0, 48, 48, false, false);
        this.menu_cache.setColor(1.0f, 1.0f, 1.0f, 0.4f);
        this.menu_cache.add(this.btntex, 8.0f, (((G.height - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 0, 48, 48, false, false);
        this.menu_cache.setColor(Color.WHITE);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 48, 48, 48, false, false);
        this.menu_cache.setColor(1.0f, 1.0f, 1.0f, 0.4f);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 48, 48, 48, false, false);
        this.menu_cache.setColor(Color.WHITE);
        this.undo_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        for (int x5 = 0; x5 < 2; x5++) {
            if (x5 == 0) {
                this.menu_cache.setColor(Color.WHITE);
            } else {
                this.menu_cache.setColor(1.0f, 1.0f, 1.0f, 0.2f);
            }
            this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 288, 48, 48, false, false);
            this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 240, 48, 48, false, false);
            this.menu_cache.add(this.btntex, (((G.width - 48) - 8) - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 96, 48, 48, false, false);
            if (x5 == 1) {
                this.menu_cache.setColor(Color.WHITE);
            }
            this.menu_cache.add(this.btntex, (G.width - 48) - 8, (((G.height - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 336, 48, 48, false, false);
        }
        this.object_menu_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 528, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 576, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (((((G.width - 48) - 8) - 48) - 8) - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 624, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (((((G.width - 48) - 8) - 48) - 8) - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 672, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 864, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 912, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 960, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 624, 48, 48, false, false);
        this.special_menu_cache_id = this.menu_cache.endCache();
    }

    public void create_level(int type) {
        String str;
        reset();
        this.level_n = -1;
        this.level_category = 0;
        this.level = Level.create(world);
        Level level2 = this.level;
        if (type == 1) {
            str = "challenge";
        } else {
            str = type == 2 ? "interactive" : "apparatus";
        }
        level2.type = str;
        this.level_filename = null;
        this.level_category = 0;
        level_type = type;
        world.setContactFilter(this.contact_handler);
        load();
        this.ready = true;
    }

    public void open_solution(File file, int category, int n) {
        reset();
        ArrayList<Integer> fixed = new ArrayList<>();
        ArrayList<Integer> fixed_dynamic = new ArrayList<>();
        ArrayList<String> fixed_hinge = new ArrayList<>();
        open(category, n);
        for (GrabableObject o : this.om.all) {
            if (o instanceof Rope) {
                Rope r = (Rope) o;
                if (((RopeEnd) r.g1).fixed_dynamic) {
                    fixed_dynamic.add(r.g1.__unique_id);
                }
                if (((RopeEnd) r.g2).fixed_dynamic) {
                    fixed_dynamic.add(r.g2.__unique_id);
                }
            } else if (o instanceof Damper) {
                Damper r2 = (Damper) o;
                if (r2.g1.fixed_dynamic) {
                    fixed_dynamic.add(r2.g1.__unique_id);
                }
                if (r2.g2.fixed_dynamic) {
                    fixed_dynamic.add(r2.g2.__unique_id);
                }
            } else {
                if (o.fixed_dynamic) {
                    fixed_dynamic.add(o.__unique_id);
                }
                if ((o instanceof BaseMotor) && ((BaseMotor) o).fixed) {
                    fixed.add(o.__unique_id);
                }
            }
        }
        int bg = this.level.background;
        for (Hinge h : this.hinges) {
            fixed_hinge.add(String.valueOf(h.body1_id) + "_" + h.body2_id);
        }
        reset();
        world.setContactFilter(this.falsefilter);
        Level.version_override = Level.version;
        this.level = Level.open(world, file);
        Level.version_override = -1;
        this.level_filename = null;
        if (this.level == null) {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
        }
        this.ready = true;
        level_type = 0;
        this.level_category = category;
        this.level.background = bg;
        load();
        level_type = 1;
        for (GrabableObject o2 : this.om.all) {
            if (o2 instanceof Rope) {
                Rope r3 = (Rope) o2;
                if (fixed_dynamic.contains(((RopeEnd) r3.g1).__unique_id)) {
                    ((RopeEnd) r3.g1).fixed_dynamic = true;
                }
                if (fixed_dynamic.contains(((RopeEnd) r3.g2).__unique_id)) {
                    ((RopeEnd) r3.g2).fixed_dynamic = true;
                }
            } else {
                if (fixed_dynamic.contains(o2.__unique_id)) {
                    o2.fixed_dynamic = true;
                }
                if ((o2 instanceof BaseMotor) && fixed.contains(o2.__unique_id)) {
                    ((BaseMotor) o2).fixed = true;
                }
            }
        }
        for (Hinge h2 : this.hinges) {
            h2.fixed = fixed_hinge.contains(String.valueOf(h2.body1_id) + "_" + h2.body2_id);
        }
        pause_world();
        world.setContactFilter(this.contact_handler);
    }

    public void open(File file) {
        int i = 0;
        reset();
        Gdx.app.log("OPEN", "OPEN");
        this.level_n = -1;
        world.setContactFilter(this.falsefilter);
        this.level = Level.open(world, file);
        this.level_filename = null;
        this.level_category = 0;
        if (this.level == null) {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
        }
        this.ready = true;
        if (!this.level.type.equals("apparatus")) {
            i = this.level.type.equals("interactive") ? 2 : 1;
        }
        level_type = i;
        load();
        world.setContactFilter(this.contact_handler);
    }

    public void open(String name) {
        int i = 0;
        reset();
        this.level_n = -1;
        this.level_category = 0;
        world.setContactFilter(this.falsefilter);
        this.level = Level.open(world, name);
        if (name.equals(".autosave")) {
            this.level_filename = null;
        } else {
            this.level_filename = name;
        }
        if (this.level != null) {
            this.ready = true;
            String[] wtf = name.split("/");
            if (wtf.length == 2) {
                this.level_category = Integer.parseInt(wtf[0]);
                this.level_id = Integer.parseInt(wtf[1]);
            }
        } else {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
            this.ready = true;
        }
        if (!this.level.type.equals("apparatus")) {
            i = this.level.type.equals("interactive") ? 2 : 1;
        }
        level_type = i;
        load();
        world.setContactFilter(this.contact_handler);
    }

    public void open_interactive(int n) {
        reset();
        this.level_filename = null;
        String name = "1/" + n;
        if (n >= ApparatusApp.num_levels) {
            ApparatusApp.instance.open_levelmenu();
            Settings.msg(L.get("nicejob"));
            return;
        }
        try {
            this.level = Level.open_internal(world, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        world.setContactFilter(this.falsefilter);
        if (this.level != null) {
            this.ready = true;
            String[] wtf = name.split("/");
            if (wtf.length == 2) {
                this.level_category = Integer.parseInt(wtf[0]);
                this.level_id = Integer.parseInt(wtf[1]);
            }
        } else {
            Gdx.app.log("CREATING", "CREATING");
            this.level = Level.create(world);
            this.ready = true;
        }
        level_type = 2;
        this.level_n = n;
        sandbox = false;
        load();
        world.setContactFilter(this.contact_handler);
        if (this.level_n == 0) {
            this.msg = L.get("ihelp1");
            return;
        }
        return;
    }

    public void open_autosave(int category, int n) {
        reset();
        ArrayList<Integer> fixed = new ArrayList<>();
        ArrayList<Integer> fixed_dynamic = new ArrayList<>();
        ArrayList<String> fixed_hinge = new ArrayList<>();
        open(category, n);
        Iterator<GrabableObject> it = this.om.all.iterator();
        while (it.hasNext()) {
            GrabableObject o = it.next();
            if (o instanceof Rope) {
                Rope r = (Rope) o;
                if (((RopeEnd) r.g1).fixed_dynamic) {
                    fixed_dynamic.add(r.g1.__unique_id);
                }
                if (((RopeEnd) r.g2).fixed_dynamic) {
                    fixed_dynamic.add(r.g2.__unique_id);
                }
            } else if (o instanceof Damper) {
                Damper r2 = (Damper) o;
                if (r2.g1.fixed_dynamic) {
                    fixed_dynamic.add(r2.g1.__unique_id);
                }
                if (r2.g2.fixed_dynamic) {
                    fixed_dynamic.add(r2.g2.__unique_id);
                }
            } else {
                if (o.fixed_dynamic) {
                    fixed_dynamic.add(o.__unique_id);
                }
                if ((o instanceof BaseMotor) && ((BaseMotor) o).fixed && ((BaseMotor) o).attached_object != null) {
                    fixed.add(o.__unique_id);
                }
            }
        }
        int bg = this.level.background;
        for (Hinge h : this.hinges) {
            fixed_hinge.add(String.valueOf(h.body1_id) + "_" + h.body2_id);
        }
        reset();
        this.level_filename = null;
        String str = String.valueOf(category) + "/" + n;
        world.setContactFilter(this.falsefilter);
        Level.version_override = Level.version;
        this.level = Level.open(world, ".autosave_" + n + (this.level_category == 2 ? "_2" : ""));
        Level.version_override = -1;
        this.level_filename = null;
        if (this.level != null) {
            this.ready = true;
        } else {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
            this.ready = true;
        }
        this.level_n = n;
        this.level.background = bg;
        this.level_category = category;
        level_type = 0;
        load();
        level_type = 1;
        for (GrabableObject o2 : this.om.all) {
            if (o2 instanceof Rope) {
                Rope r3 = (Rope) o2;
                ((RopeEnd) r3.g1).fixed_dynamic = fixed_dynamic.contains(((RopeEnd) r3.g1).__unique_id);
                ((RopeEnd) r3.g2).fixed_dynamic = fixed_dynamic.contains(((RopeEnd) r3.g2).__unique_id);
            } else {
                o2.fixed_dynamic = fixed_dynamic.contains(o2.__unique_id);
                if (o2 instanceof BaseMotor) {
                    ((BaseMotor) o2).fixed = fixed.contains(o2.__unique_id);
                }
            }
        }
        for (Hinge h2 : this.hinges) {
            h2.fixed = fixed_hinge.contains(String.valueOf(h2.body1_id) + "_" + h2.body2_id);
        }
        world.setContactFilter(this.contact_handler);
        helpify();
        pause_world();
        System.gc();
    }

    private void helpify() {
        if (this.level_category == 0) {
            if (this.level_n == 1) {
                this.msg = has_multitouch ? L.get("help2_1") : L.get("help2_2");
                return;
            }
            this.msg = L.get("help" + (this.level_n + 1));
            if (this.msg.isEmpty()) {
                this.msg = null;
                return;
            } else if (this.msg.trim().equals("null")) {
                this.msg = null;
                return;
            } else {
                return;
            }
        } else if (this.level_category == 2) {
            switch (this.level_n) {
                case 0:
                    this.msg = "Welcome to the christmas level pack!\nBefore attempting these levels, you should play some of the main challenge levels to learn the basics.\nDeliver the christmas gifts to santa's basket! All gifts must be placed in the basket to complete the level.\n";
                    return;
                case 1:
                    this.msg = "There is a new object in this level - a shock absorber. The shock absorber dampens forces acting upon it, and will act as a spring if compressed enough. Experiment with it!";
                    return;
                default:
                    return;
            }
        }
    }

    public void open(int category, int n) {
        if (this.do_save && this.level_n != -1 && level_type == 1) {
            this.level_filename = ".lvl" + this.level_n + (this.level_category != 0 ? "_" + this.level_category : "");
            this.level.type = "apparatus";
            pause_world();
            this.do_save = false;
            save();
        }
        reset();
        this.level_filename = null;
        this.level_category = category;
        String name = String.valueOf(category) + "/" + n;
        if (n >= LevelMenu.num_levels) {
            ApparatusApp.instance.open_levelmenu();
            Settings.msg(L.get("nicejob"));
            return;
        }
        try {
            this.level = Level.open_internal(world, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        world.setContactFilter(this.falsefilter);
        if (this.level != null) {
            this.ready = true;
            String[] wtf = name.split("/");
            if (wtf.length == 2) {
                this.level_category = Integer.parseInt(wtf[0]);
                this.level_id = Integer.parseInt(wtf[1]);
            }
        } else {
            this.level = Level.create(world);
            this.ready = true;
        }
        level_type = 1;
        this.level_n = n;
        sandbox = false;
        load();
        world.setContactFilter(this.contact_handler);
        helpify();
    }

    @Override
    public int tick() {
        if (!this.ready) {
            return 0;
        }
        if (mode != MODE_PLAY) {
            if (this.do_autosave && !this.hingeselect) {
                autosave();
                this.do_autosave = false;
            }
            if (sandbox && System.currentTimeMillis() - this.last_autosave > ((long) (this.autosave_interval * 1000L))) {
                this.last_autosave = System.currentTimeMillis();
                if (this.modified) {
                    this.do_autosave = true;
                    return 1;
                }
            }
        }
        if (!this.lowfpsfixed) {
            if (Gdx.graphics.getFramesPerSecond() < 10) {
                this.lowfpscount++;
            } else {
                this.lowfpscount = 0;
            }
            if (this.lowfpscount > 300) {
                Settings.msg(L.get("gfxadjust"));
                rope_quality = 40;
                enable_shadows = false;
                this.lowfpsfixed = true;
                update_ropes();
            }
        }
        if (this.num_touch_points == 2 && !this.widget[0] && !this.widget[1] && !this.rotating && mode != MODE_GRAB && has_multitouch) {
            float dist = this._last_vec[0].dst(this._last_vec[1]);
            float diff = dist - this._last_dist;
            if (Math.abs(diff) > 4.0f) {
                this.camera_h.camera_pos.z -= diff / 30.0f;
                this.last_zoom = System.currentTimeMillis();
            }
            this._last_dist = dist;
        }
        if (this.camera_h.camera_pos.z > 70.0f) {
            this.camera_h.camera_pos.z = 70.0f;
        } else if (this.camera_h.camera_pos.z < 10.0f) {
            this.camera_h.camera_pos.z = 10.0f;
        }
        G.p_cam.far = this.camera_h.camera_pos.z * 5.0f;
        G.p_cam.near = this.camera_h.camera_pos.z / 3.0f;
        long now = System.nanoTime();
        for (long delta = (now - this.time_last) + this.time_accum; delta >= 8000000; delta -= 8000000) {
            this.camera_h.tick(0.08f, false);
        }
        if (this.state == STATE_PLAYING) {
            if (mode == MODE_PLAY) {
                SoundManager.tick();
                if (!enable_multithreading) {
                    if (this.time_accum > 100000000) {
                        this.time_accum = 20000000;
                    }
                    long delta2 = (now - this.time_last) + this.time_accum;
                    if (delta2 > 100000000) {
                        delta2 = 40000000;
                    }
                    while (delta2 >= 8000000) {
                        int iterations = physics_stability == 1 ? 10 : physics_stability == 0 ? 1 : 64;
                        world.step(0.011f, iterations, iterations);
                        for (Hinge hinge : this.hinges) {
                            hinge.tick();
                        }
                        for (RocketEngine rocketEngine : this.om.rocketengines) {
                            rocketEngine.step(0.008f);
                        }
                        if (Level.version >= 7) {
                            for (Rope rope : this.om.ropes)
                                rope.tick();

                            for (PanelCable panelCable : this.om.pcables)
                                panelCable.tick();

                            for (Cable cable : this.om.cables)
                                cable.tick();
                        }
                        delta2 -= 8000000;
                    }
                    this.time_accum = delta2;
                }
                if ((level_type == 1 || level_type == 2) && this.num_balls_in_goal == this.num_balls && this.num_balls != 0 && !from_sandbox && !sandbox) {
                    win();
                }
            } else {
                this.contact_handler.clean();
                world.step(0.001f, 160, 160);
                if (this.commit_next) {
                    this.um.commit_step();
                    this.commit_next = false;
                }
            }
        } else if (this.state == STATE_FINISHED) {
            if (this.time_accum > 100000000) {
                this.time_accum = 20000000;
            }
            long delta3 = (now - this.time_last) + this.time_accum;
            if (delta3 > 100000000) {
                delta3 = 40000000;
            }
            while (delta3 >= 8000000) {
                world.step(0.011f, 10, 10);
                for (Hinge hinge : this.hinges)
                    hinge.tick();

                delta3 -= 8000000;
            }
            this.time_accum = delta3;
        }
        this.time_last = now;
        if (mode != MODE_PLAY || Level.version < 7) {
            for (Rope rope : this.om.ropes)
                rope.tick();

            for (PanelCable panelCable : this.om.pcables)
                panelCable.tick();

            for (Cable cable : this.om.cables)
                cable.tick();
        }
        if (sandbox && mode != MODE_PLAY) {
            for (MetalBar bar : this.om.layer0.bars)
                bar.tick();

            for (MetalBar metalBar : this.om.layer1.bars)
                metalBar.tick();
        }
        return 0;
    }

    private void win() {
        this.state = STATE_FINISHED;
        this.finished_fade = 0.0f;
        Settings.set(String.valueOf(LevelMenu.lvl_prefix) + "0/" + this.level_n, "1");
        SoundManager.stop_all();
        SoundManager.play_lvlcomplete();
    }

    void render_scene(boolean light) {
        G.gl.glMatrixMode(GL10.GL_PROJECTION);
        G.gl.glLoadIdentity();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glLoadIdentity();
        G.gl.glDisable(3042);
        G.gl.glEnable(3553);
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.p_cam.apply(G.gl);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        G.gl.glMatrixMode(5890);
        G.gl.glScalef(0.2f, 46.0f, 1.0f);
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glEnable(GL10.GL_LIGHTING);
        G.gl.glEnable(16384);
        if (light) {
            G.gl.glLightfv(16384, GL10.GL_AMBIENT, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_SPECULAR, this.light_specular);
            G.gl.glLightfv(16384, GL10.GL_DIFFUSE, this.light_diffuse);
            G.gl.glLightfv(16384, GL10.GL_POSITION, this._light_pos);
        } else {
            G.gl.glLightfv(16384, GL10.GL_AMBIENT, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_SPECULAR, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_DIFFUSE, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_POSITION, this._light_pos);
        }
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, _def_material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _def_material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _def_material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _def_material, 12);
        if (this.ghost_object != null) {
            G.gl.glEnable(3042);
            G.gl.glDisable(GL10.GL_LIGHTING);
            G.gl.glDisable(3553);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            if (this.ghost_object instanceof BaseRope) {
                ((BaseRope) this.ghost_object).render_edges();
            } else {
                this.ghost_object.render();
            }
            G.gl.glEnable(GL10.GL_LIGHTING);
            G.gl.glEnable(3553);
            G.gl.glDisable(3042);
        }
    }

    private void render_static_shadows(Vector3 lightdir, boolean front) {
    }

    private void render_shadows(Vector3 lightdir2, boolean front) {
        render_static_shadows(lightdir2, front);
    }

    private void boundscheck_camera() {
        if (this.camera_h.camera_pos.y > 40.0f) {
            Intersector.intersectRayPlane(G.p_cam.getPickRay(0.0f, 0.0f), yaxis, this.tmp3);
            if (this.tmp3.y > 120.0f) {
                this.camera_h.camera_pos.y -= this.tmp3.y - 120.0f;
                this.camera_h.release_target();
                this.camera_h.velocity.y = 0.0f;
            }
        } else if (this.camera_h.camera_pos.y < -40.0f) {
            Intersector.intersectRayPlane(G.p_cam.getPickRay(0.0f, (float) G.realheight), yaxis, this.tmp3);
            if (this.tmp3.y < -120.0f) {
                this.camera_h.camera_pos.y -= this.tmp3.y + 120.0f;
                this.camera_h.velocity.y = 0.0f;
                this.camera_h.release_target();
            }
        }
        if (this.camera_h.camera_pos.x > 350.0f) {
            Intersector.intersectRayPlane(G.p_cam.getPickRay((float) G.realwidth, 0.0f), yaxis, this.tmp3);
            if (this.tmp3.x > 510.0f) {
                this.camera_h.camera_pos.x -= this.tmp3.x - 510.0f;
                this.camera_h.release_target();
                this.camera_h.velocity.x = 0.0f;
            }
        } else if (this.camera_h.camera_pos.x < -350.0f) {
            Intersector.intersectRayPlane(G.p_cam.getPickRay(0.0f, 0.0f), yaxis, this.tmp3);
            if (this.tmp3.x < -510.0f) {
                this.camera_h.camera_pos.x -= this.tmp3.x + 510.0f;
                this.camera_h.release_target();
                this.camera_h.velocity.x = 0.0f;
            }
        }
    }

    private void cull_objects() {
        ArrayList<GrabableObject> objects = this.om.all;
        int sz = objects.size();
        int hsz = this.hinges.size();
        float z = this.camera_h.camera_pos.z * T_EPSILON;
        for (int x = 0; x < sz; x++) {
            GrabableObject o = objects.get(x);
            o.culled = false;
            Vector2 pos = o.get_state().position;
            if (pos != null) {
                float dx = pos.x - this.camera_h.camera_pos.x;
                float dy = pos.y - this.camera_h.camera_pos.y;
                if (dx < (-20.0f) - z || dx > 20.0f + z || dy < (-5.0f) - z || dy > 5.0f + z) {
                    o.culled = true;
                }
            }
        }
        for (int x2 = 0; x2 < hsz; x2++) {
            Hinge o2 = this.hinges.get(x2);
            o2.culled = false;
            Vector2 pos2 = o2.get_state().position;
            if (pos2 != null) {
                float dx2 = pos2.x - this.camera_h.camera_pos.x;
                float dy2 = pos2.y - this.camera_h.camera_pos.y;
                if (dx2 < (-15.0f) - z || dx2 > 15.0f + z || dy2 < (-5.0f) - z || dy2 > 5.0f + z) {
                    o2.culled = true;
                }
            }
        }
    }

    private void cull_and_swap() {
        Vector2 pos;
        ArrayList<GrabableObject> objects = this.om.all;
        int sz = objects.size();
        int hsz = this.hinges.size();
        float z = this.camera_h.camera_pos.z * T_EPSILON;
        for (int x = 0; x < sz; x++) {
            GrabableObject o = objects.get(x);
            o.culled = false;
            o.save_state();
            if (!(o instanceof BaseRope) && (pos = o.get_state().position) != null) {
                float dx = pos.x - this.camera_h.camera_pos.x;
                float dy = pos.y - this.camera_h.camera_pos.y;
                if (dx < (-15.0f) - z || dx > 15.0f + z || dy < (-5.0f) - z || dy > 5.0f + z) {
                    o.culled = true;
                }
            }
        }
        for (int x2 = 0; x2 < hsz; x2++) {
            Hinge o2 = this.hinges.get(x2);
            o2.culled = false;
            if (o2.joint != null) {
                o2.save_state();
                Vector2 pos2 = o2.get_state().position;
                if (pos2 != null) {
                    float dx2 = pos2.x - this.camera_h.camera_pos.x;
                    float dy2 = pos2.y - this.camera_h.camera_pos.y;
                    if (dx2 < (-15.0f) - z || dx2 > 15.0f + z || dy2 < (-5.0f) - z || dy2 > 5.0f + z) {
                        o2.culled = true;
                    }
                }
            }
        }
    }

    @Override
    public void render() {
        if (this.ready) {
            G.set_clear_color(0.3f, 0.3f, 0.3f);
            G.gl.glClearStencil(5);
            if (enable_reflections) {
                G.gl.glClear(1280);
            } else {
                G.gl.glClear(256);
            }
            if (!enable_bg) {
                G.gl.glClear(16384);
            }
            G.gl.glDisable(3042);
            G.gl.glEnable(2929);
            G.gl.glEnable(2884);
            G.gl.glCullFace(1029);
            G.gl.glDepthFunc(513);
            G.gl.glDepthMask(true);
            boundscheck_camera();
            this.camera_h.update();
            if (mode != MODE_PLAY || !enable_multithreading) {
                cull_and_swap();
            } else {
                this.sim_thread.swap_state_buffers();
                cull_objects();
            }
            G.gl.glEnable(16384);
            G.gl.glLightfv(16384, GL10.GL_AMBIENT, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_SPECULAR, this.light_specular);
            G.gl.glLightfv(16384, GL10.GL_DIFFUSE, this.light_diffuse);
            G.gl.glLightfv(16384, GL10.GL_POSITION, this._light_pos);
            G.p_cam.apply(G.gl);
            IlluminationManager.render_scene(G.p_cam, this.hinges, this.om, enable_bg);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (enable_reflections) {
                IlluminationManager.render_reflections(this.hinges, this.om);
            }
            G.gl.glDisable(GL10.GL_LIGHTING);
            G.gl.glDisable(3042);
            G.gl.glDisable(2960);
            G.gl.glDisable(2884);
            G.gl.glDisable(GL10.GL_NORMALIZE);
            G.gl.glDisable(GL10.GL_LIGHTING);
            G.gl.glCullFace(1029);
            G.gl.glDepthMask(true);
            G.p_cam.apply(G.gl);
            G.gl.glDisable(2884);
            G.gl.glMatrixMode(GL10.GL_PROJECTION);
            G.gl.glLoadMatrixf(G.p_cam.combined.val, 0);
            G.gl.glMatrixMode(GL10.GL_MODELVIEW);
            G.gl.glLoadIdentity();
            if (enable_bloom) {
                IlluminationManager.render_bloom(this.camera_h, this.om);
            }
            G.gl.glDisable(2929);
            G.gl.glDepthMask(true);
            G.gl.glBlendFunc(770, 771);
            G.gl.glDisable(3553);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (enable_menu && !connectanims.isEmpty()) {
                Iterator<ConnectAnim> i = connectanims.iterator();
                while (i.hasNext()) {
                    ConnectAnim x = i.next();
                    if (!x.render()) {
                        i.remove();
                    }
                }
            }
            if (!(mode == MODE_PLAY || this.grabbed_object == null)) {
                G.gl.glDisable(3553);
                G.gl.glEnable(3042);
                G.gl.glPushMatrix();
                G.gl.glLoadIdentity();
                GrabableObject o = this.grabbed_object;
                G.gl.glTranslatef(o.get_state().position.x, o.get_state().position.y, (float) (o.layer + 1));
                G.gl.glRotatef((float) (((double) o.get_state().angle) * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                if (!this.grabbed_object.fixed_rotation && !(this.grabbed_object instanceof Wheel)) {
                    G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                    G.gl.glPushMatrix();
                    float size = 2.0f;
                    if (this.grabbed_object instanceof Bar) {
                        size = (((Bar) this.grabbed_object).size.x / 2.0f) + 2.0f;
                    }
                    MiscRenderer.draw_line(0.0f, 0.0f, size, 0.0f);
                    G.gl.glPopMatrix();
                    G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    G.gl.glPushMatrix();
                    G.gl.glTranslatef(0.5f + size, 0.0f, 0.0f);
                    G.gl.glScalef(0.5f, 0.5f, 1.0f);
                    G.gl.glEnable(3553);
                    rotatetex.bind();
                    MiscRenderer.draw_textured_box();
                    G.gl.glDisable(3553);
                    G.gl.glPopMatrix();
                }
                G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.2f);
                if (o instanceof Bar) {
                    G.gl.glScalef(((Bar) o).size.x + 0.2f, ((Bar) o).size.y + 0.2f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof Wheel) {
                    G.gl.glScalef(((Wheel) o).size + 0.1f, ((Wheel) o).size + 0.1f, 1.0f);
                    MiscRenderer.draw_colored_ball();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_circle();
                } else if ((o instanceof RopeEnd) || (o instanceof CableEnd) || (o instanceof PanelCableEnd) || (o instanceof Marble)) {
                    G.gl.glScalef(0.65f, 0.65f, 1.0f);
                    MiscRenderer.draw_colored_ball();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_circle();
                } else if (o instanceof StaticMotor) {
                    G.gl.glScalef(0.8f, 0.8f, 1.0f);
                    MiscRenderer.draw_colored_ball();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_circle();
                } else if (o instanceof Bucket) {
                    G.gl.glScalef(5.0f, 3.0f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof Battery) {
                    if (((Battery) o).size == 1) {
                        G.gl.glScalef(1.0f, 1.0f, 1.0f);
                        MiscRenderer.draw_colored_box();
                        G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                        MiscRenderer.draw_colored_square();
                    } else {
                        G.gl.glScalef(2.5f, 2.1f, 1.0f);
                        MiscRenderer.draw_colored_box();
                        G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                        MiscRenderer.draw_colored_square();
                    }
                } else if (o instanceof Weight) {
                    G.gl.glScalef(2.0f, 1.5f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof DynamicMotor) {
                    G.gl.glScalef(1.5f, 1.5f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof RocketEngine) {
                    G.gl.glScalef(1.0f, 2.0f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof Panel) {
                    G.gl.glScalef(2.3f, 1.5f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                }
                G.gl.glPopMatrix();
            }
            if (this.ghost_object != null) {
                G.gl.glEnable(3042);
                G.gl.glDisable(GL10.GL_LIGHTING);
                G.gl.glDisable(3553);
                G.gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
                if (this.ghost_object instanceof BaseRope) {
                    ((BaseRope) this.ghost_object).render_edges();
                } else {
                    this.ghost_object.render();
                }
                G.gl.glDisable(3042);
            } else if (this.active_panel != null) {
                G.gl.glEnable(3042);
                G.gl.glDisable(3553);
                G.gl.glColor4f(0.0f, 1.0f, 0.0f, 0.3f);
                this.active_panel.render();
                G.gl.glDisable(3042);
            }
            G.gl.glLoadIdentity();
            G.gl.glEnable(3553);
            render_menu();
            G.batch.setProjectionMatrix(G.cam_p.combined);
            if (draw_fps) {
                G.batch.begin();
                G.font.draw(G.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0.0f, 24.0f);
                G.batch.end();
            }
            if (this.do_autosave) {
                G.batch.begin();
                G.font.draw(G.batch, "Auto-saving...", 0.0f, 24.0f);
                G.batch.end();
            } else if (from_sandbox && mode != MODE_PLAY) {
                G.batch.begin();
                G.font.draw(G.batch, L.get("testingchallenge"), 0.0f, 24.0f);
                G.batch.end();
            }
            if (this.pending_follow && mode == MODE_PLAY) {
                G.batch.begin();
                G.font.draw(G.batch, "Click on an object to follow it, or on the background to cancel.", 0.0f, 24.0f);
                this.state = STATE_PAUSED;
                G.batch.end();
            }
            if ((!this.pending_follow && this.state != STATE_PLAYING) || this.finished_fade > 0.0f) {
                G.gl.glEnable(3042);
                G.gl.glMatrixMode(GL10.GL_PROJECTION);
                G.gl.glPushMatrix();
                G.gl.glLoadIdentity();
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glLoadIdentity();
                G.color(0.0f, 0.0f, 0.0f, 0.7f * this.finished_fade);
                MiscRenderer.boxmesh.render(6);
                G.gl.glMatrixMode(GL10.GL_PROJECTION);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPopMatrix();
                if (this.state != STATE_PLAYING) {
                    this.finished_fade += G.delta * 4.0f;
                    if (this.finished_fade >= 1.0f) {
                        this.finished_fade = 1.0f;
                    }
                } else if (this.state == STATE_PLAYING) {
                    this.finished_fade -= G.delta * 4.0f;
                    if (this.finished_fade <= 0.0f) {
                        this.finished_fade = 0.0f;
                    }
                }
            }
            G.batch.begin();
            if (this.finished_fade > 0.0f) {
                G.batch.setColor(1.0f, 1.0f, 1.0f, this.finished_fade);
                G.batch.draw(this.lvlcompletetex, 0.0f, 0.0f, 0.0f, 0.0f, G.width, G.height, 1.0f, 1.0f, 0.0f, 0, 0, this.lvlcompletetex.getWidth(), this.lvlcompletetex.getHeight(), false, false);
                G.batch.setColor(Color.WHITE);
            }
            G.batch.end();
            Gdx.app.getGraphics().getType();
            Graphics.GraphicsType graphicsType = Graphics.GraphicsType.AndroidGL;
        }
    }

    private void render_menu() {
        G.cam_p.apply(G.gl);
        G.batch.setColor(Color.WHITE);
        G.batch.setProjectionMatrix(G.cam_p.combined);
        G.batch.begin();
        for (int x = 0; x < ContactHandler.num_fixture_pairs; x++) {
            ContactHandler.FixturePair fp = ContactHandler.fixture_pairs[x];
            Vector2 point = fp.get_intersection_point();
            if (point != null && (fp.a.getBody().getUserData() == this.grabbed_object || fp.b.getBody().getUserData() == this.grabbed_object)) {
                this.tmp3.set(point.x, point.y, 3.0f);
                G.p_cam.project(this.tmp3);
                this.tmp3.x *= ((float) G.width) / ((float) G.realwidth);
                this.tmp3.y *= ((float) G.height) / ((float) G.realheight);
                if (fp.same_layer) {
                    if (fp.a.getBody().getUserData() instanceof Plank) {
                        G.batch.draw(this.hammertex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 32.0f, 112.0f, 64.0f, 128.0f, 1.0f, 1.0f, ((fp.a == ((Plank) fp.a.getBody().getUserData()).fa ? 0 : 1) * 180) + (((float) (fp.a.getBody().getAngle() * 57.29577951308232d)) - 45.0f), 0, 0, 64, 128, false, false);
                    } else if ((fp.a.getBody().getUserData() instanceof DynamicMotor) || (fp.a.getBody().getUserData() instanceof Panel) || (fp.a.getBody().getUserData() instanceof RocketEngine) || (fp.a.getBody().getUserData() instanceof Hub) || (fp.a.getBody().getUserData() instanceof DamperEnd)) {
                        G.batch.draw(this.hammertex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 32.0f, 112.0f, 64.0f, 128.0f, 1.0f, 1.0f, 45.0f, 0, 0, 64, 128, false, false);
                    }
                } else if (this.hinge_type == HINGE_HAMMER) {
                    G.batch.draw(this.hammertex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 14.0f, 112.0f, 64.0f, 128.0f, 1.0f, 1.0f, 45.0f, 0, 0, 64, 128, false, false);
                } else {
                    G.batch.draw(this.wrenchtex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 14.0f, 112.0f, 32.0f, 128.0f, 1.0f, 1.0f, 45.0f, 0, 0, 32, 128, false, false);
                }
            }
        }
        if (this.hingeselect) {
            this.tmp3.set(this.wrench_anim_pos.x, this.wrench_anim_pos.y, 1.0f);
            G.p_cam.project(this.tmp3);
            this.tmp3.x *= ((float) G.width) / ((float) G.realwidth);
            this.tmp3.y *= ((float) G.height) / ((float) G.realheight);
            G.batch.draw(this.hingeselecttex, this.tmp3.x, this.tmp3.y, 0.0f, 0.0f, 128.0f, 64.0f, 1.0f, 1.0f, 0.0f, 0, 64, 128, 64, false, false);
        }
        long now = System.currentTimeMillis();
        if (now - this.wrench_anim_start < 400) {
            this.tmp3.set(this.wrench_anim_pos.x, this.wrench_anim_pos.y, 3.0f);
            G.p_cam.project(this.tmp3);
            this.tmp3.x *= ((float) G.width) / ((float) G.realwidth);
            this.tmp3.y *= ((float) G.height) / ((float) G.realheight);
            G.batch.draw(this.wrenchtex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 14.0f, 112.0f, 32.0f, 128.0f, 1.0f, 1.0f, 45.0f - (80.0f * (((float) (now - this.wrench_anim_start)) / 400.0f)), 0, 0, 32, 128, false, false);
        }
        G.batch.end();
        G.cam_p.apply(G.gl);
        if (this.active_panel != null && mode == MODE_PLAY) {
            this.active_panel.widgets.render_all(G.batch);
        }
        if (enable_menu) {
            G.gl.glEnable(3042);
            G.gl.glBlendFunc(770, 771);
            this.menu_cache.begin();
            if (mode != MODE_PLAY) {
                if (this.msg != null) {
                    this.menu_cache.draw(this.left_menu_cache_id, 0, 2);
                } else {
                    this.menu_cache.draw(this.left_menu_cache_id, 0, 1);
                }
                this.menu_cache.draw(this.undo_cache_id, (this.um.can_undo() ? 0 : 1), 1);
                if (sandbox) {
                    if (this.open_animate_dir == -1) {
                        this.open_animate_time -= G.delta;
                        if (this.open_animate_time < 0.0f) {
                            this.open_animate_dir = 0;
                            this.open_animate_time = 0.0f;
                            this.open_sandbox_category = -1;
                        }
                    } else if (this.open_animate_dir == 1) {
                        this.open_animate_time += G.delta;
                        if (this.open_animate_time > 0.2f) {
                            this.open_animate_dir = 0;
                            this.open_animate_time = 0.2f;
                        }
                    }
                    if (this.open_animate_dir != 0) {
                        this.menu_cache.end();
                        this.menu_cache.setTransformMatrix(this.open_animate_matrix.setToTranslation(0.0f, (-56.0f) * (this.open_animate_time / 0.2f), 0.0f));
                        this.menu_cache.begin();
                        this.menu_cache.draw(this.sandbox_categories_cache_id);
                        this.menu_cache.end();
                        this.menu_cache.setTransformMatrix(this.open_animate_matrix.setToTranslation(0.0f, (-56.0f) + (56.0f * (this.open_animate_time / 0.2f)), 0.0f));
                        this.menu_cache.begin();
                        this.menu_cache.draw(this.sandbox_category_cache_id[this.open_sandbox_category]);
                        this.menu_cache.end();
                        this.menu_cache.setTransformMatrix(this.open_animate_matrix.idt());
                        this.menu_cache.begin();
                    } else if (this.open_sandbox_category != -1) {
                        this.menu_cache.draw(this.sandbox_category_cache_id[this.open_sandbox_category]);
                    } else {
                        this.menu_cache.draw(this.sandbox_categories_cache_id);
                    }
                }
                if (this.grabbed_object != null) {
                    if (!(this.grabbed_object instanceof Battery)) {
                        if (sandbox && (this.grabbed_object instanceof Plank)) {
                            this.menu_cache.draw(this.special_menu_cache_id, this.grabbed_object.layer + 4, 1);
                            this.menu_cache.draw(this.object_menu_cache_id, ((this.grabbed_object.num_hinges == 0 ? 1 : 0) * 4) + 2, 1);
                        } else if (!sandbox || !(this.grabbed_object instanceof Panel)) {
                            this.menu_cache.draw(this.object_menu_cache_id, (this.grabbed_object.layer == 1 ? 1 : 0) + (((this.grabbed_object.fixed_layer || !(this.level_n >= 7 || this.level_n == -1 || this.level_category == 2)) ? 1 : 0) * 4), 1);
                            this.menu_cache.draw(this.object_menu_cache_id, ((this.grabbed_object.num_hinges == 0 ? 1 : 0) * 4) + 2, 1);
                        } else {
                            this.menu_cache.draw(this.special_menu_cache_id, 7, 1);
                            this.menu_cache.draw(this.object_menu_cache_id, ((this.grabbed_object.num_hinges == 0 ? 1 : 0) * 4) + 2, 1);
                        }
                        if (this.grabbed_object instanceof BaseMotor) {
                            this.menu_cache.draw(this.special_menu_cache_id, (((BaseMotor) this.grabbed_object).dir > 0.0f ? 1 : 0) + 2, 1);
                        }
                        if (sandbox) {
                            this.menu_cache.draw(this.object_menu_cache_id, 3, 1);
                        }
                    } else {
                        this.menu_cache.draw(this.object_menu_cache_id, 3, 1);
                        this.menu_cache.draw(this.special_menu_cache_id, (((Battery) this.grabbed_object).real_on ? 0 : 1), 1);
                    }
                }
            } else {
                this.menu_cache.draw(this.left_menu_cache_id, 3, 1);
                this.menu_cache.draw(this.left_menu_cache_id, 6, 1);
                if (level_type == 2 && this.msg != null) {
                    this.menu_cache.draw(this.left_menu_cache_id, 1, 2);
                }
            }
            if (!has_multitouch) {
                this.menu_cache.draw(this.left_menu_cache_id, 4, 2);
            }
            this.menu_cache.end();
            if (mode != MODE_PLAY) {
                G.batch.begin();
                if (from_game) {
                    G.batch.draw(this.nextleveltex, 80.0f, G.realheight - 48, 0.0f, 0.0f, 128.0f, 32.0f, 1.0f, 1.0f, 0.0f, 0, 0, 128, 32, false, false);
                }
                if (sandbox && this.grabbed_object != null) {
                    if (this.grabbed_object instanceof RocketEngine) {
                        G.font.draw(G.batch, "Thrust", (float) ((G.width - 256) + 70), (float) (G.height - 56));
                    } else if (this.grabbed_object instanceof Battery) {
                        G.font.draw(G.batch, L.get("current"), (G.width - 256) + 70, G.height - 56);
                        G.font.draw(G.batch, L.get("voltage"), ((G.width - 256) - 180) + 46, G.height - 56);
                        G.font.draw(G.batch, L.get("size"), (G.width - 256) - 300, G.height - 56);
                    } else if (this.grabbed_object instanceof DamperEnd) {
                        G.font.draw(G.batch, "Speed", (float) ((G.width - 320) + 70), (float) (G.height - 56));
                        G.font.draw(G.batch, "Force", (float) (((G.width - 320) - 180) + 46), (float) (G.height - 56));
                    } else if ((this.grabbed_object instanceof Wheel) || (this.grabbed_object instanceof Plank) || (this.grabbed_object instanceof MetalBar)) {
                        G.font.draw(G.batch, L.get("size"), (float) ((G.width - 320) + 75), (float) (G.height - 56));
                    } else if (this.grabbed_object instanceof BaseRopeEnd) {
                        G.font.draw(G.batch, L.get("size"), (float) ((G.width - 320) + 75), (float) (G.height - 56));
                        if (this.grabbed_object instanceof RopeEnd) {
                            G.font.draw(G.batch, "Elasticity", (float) ((G.width - 256) - 162), (float) (G.height - 56));
                        }
                    }
                }
                G.batch.end();
                if (sandbox) {
                    this.widgets.render_all(G.batch);
                }
            }
        }
    }

    private void create_metal_cache() {
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        release_object();
        this.msg = null;
        this.hingeselect = false;
        this.num_touch_points = 0;
        this.num_balls_in_goal = 0;
        this.num_balls = 0;
        this.state = STATE_PLAYING;
        this.last_autosave = System.currentTimeMillis();
        world.setContactFilter(null);
        world.setContactListener(null);
        this.um.reset();
        this.om.clear();
        this.hinges.clear();
        mode = MODE_PAUSE;
        this.grabbed_object = null;
        this.last_grabbed = null;
        this.contact_handler.reset();
        this.camera_h.camera_pos.set(0.0f, 0.0f, 0.0f);
        G.cam.position.set(0.0f, 0.0f, 0.0f);
        G.cam.update();
        Array<Joint> joints = new Array<>();
        world.getJoints(joints);
        Iterator<Joint> i = joints.iterator();
        while (i.hasNext()) {
            try {
                world.destroyJoint(i.next());
            } catch (Exception e) {
                Gdx.app.log("EXCEPTION", "while removing joint");
            }
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body b : bodies) {
            if (b == null) {
                Gdx.app.log("WTFFFFFFFFFFFFFFFFFFFFFFF", "NULL?");
            } else {
                try {
                    world.destroyBody(b);
                } catch (NullPointerException e2) {
                    Gdx.app.log("null EXCEPTION", "while destroying body");
                } catch (Exception e3) {
                    Gdx.app.log("EXCEPTION", "while destroying body");
                }
            }
        }
        world.setContactFilter(this.contact_handler);
        world.setContactListener(this.contact_handler);
    }

    private void load() {
        Vector2 pos;
        Gdx.app.log("LOAD", "LOAD");
        set_mode(MODE_PAUSE);
        this.contact_handler.reset();
        this.modified = false;
        this.lowfpsfixed = false;
        from_game = false;
        id_counter = 0;
        Vector2 avgpos = new Vector2();
        int num_pos = 0;
        BaseObject[] baseObjectArr = this.level.get_objects();
        for (BaseObject o : baseObjectArr) {
            if (o instanceof Hinge) {
                this.hinges.add((Hinge) o);
            } else if (o instanceof GrabableObject) {
                if (!((o instanceof BaseRope) || ((GrabableObject) o).body == null || (pos = ((GrabableObject) o).body.getPosition()) == null)) {
                    avgpos.add(pos);
                    num_pos++;
                }
                this.om.add((GrabableObject) o);
            }
            o.set_game(this);
            if (o.__unique_id > id_counter) {
                id_counter = o.__unique_id;
            }
        }
        if (num_pos != 0) {
            avgpos.x /= (float) num_pos;
            avgpos.y /= (float) num_pos;
        } else {
            avgpos.set(0.0f, 0.0f);
        }
        this.camera_h.camera_pos.set(avgpos.x + 7.0f, avgpos.y - 5.0f, 15.0f);
        this.camera_h.save();
        id_counter++;
        for (StaticMotor staticMotor : this.om.static_motors) {
            staticMotor.load(this.om.all);
        }
        for (DynamicMotor dynamicMotor : this.om.layer0.dynamicmotors) {
            dynamicMotor.load(this.om.all);
        }
        for (DynamicMotor dynamicMotor : this.om.layer1.dynamicmotors) {
            dynamicMotor.load(this.om.all);
        }
        Gdx.app.log("loaded motors", "ja");
        Iterator<Hinge> i = this.hinges.iterator();
        while (i.hasNext()) {
            Hinge h = i.next();
            GrabableObject o1 = null;
            GrabableObject o2 = null;
            int n1 = h.body1_id;
            int n2 = h.body2_id;
            for (GrabableObject o3 : this.om.all) {
                if (o3.__unique_id == n1) {
                    o1 = o3;
                } else if (o3.__unique_id == n2) {
                    o2 = o3;
                } else if (o3 instanceof Rope) {
                    Rope r = (Rope) o3;
                    if (r.g1.__unique_id == n1) {
                        o1 = r.g1;
                    }
                    if (r.g1.__unique_id == n2) {
                        o2 = r.g1;
                    }
                    if (r.g2.__unique_id == n1) {
                        o1 = r.g2;
                    }
                    if (r.g2.__unique_id == n2) {
                        o2 = r.g2;
                    }
                } else if (o3 instanceof Damper) {
                    Damper r2 = (Damper) o3;
                    if (r2.g1.__unique_id == n1) {
                        o1 = r2.g1;
                    }
                    if (r2.g1.__unique_id == n2) {
                        o2 = r2.g1;
                    }
                    if (r2.g2.__unique_id == n1) {
                        o1 = r2.g2;
                    }
                    if (r2.g2.__unique_id == n2) {
                        o2 = r2.g2;
                    }
                }
                if (!(o1 == null || o2 == null)) {
                    break;
                }
            }
            if (o1 == null || o2 == null) {
                Gdx.app.log("ERROR", "invalid hinge object ids");
                h.dispose(world);
                i.remove();
            } else {
                if (!sandbox && level_type == 1) {
                    o1.fixed_dynamic = true;
                    o2.fixed_dynamic = true;
                }
                h.setup(o1, o2, h.anchor);
            }
        }
        Gdx.app.log("TJATJAJTA", "TJATJA");
        this.ground = ObjectFactory.create_anchor_body(world);
        this.ground.setTransform(new Vector2(-10000.0f, 0.0f), 0.0f);
        pause_world();
        from_sandbox = false;
        this.lowfpscount = 0;
        if (from_community && !this.om.layer0.controllers.isEmpty()) {
            this.msg = L.get("hascontrolpanel");
        }
        MiscRenderer.update_quality();
        if (this.level_category == 2) {
            set_bg(10);
        } else {
            set_bg(this.level.background);
        }
        Gdx.app.log("LOADED", "LOADED");
    }

    private void resolve_cable_connections() {
        if (Level.version >= 3) {
            for (PanelCable p : this.om.pcables) {
                PanelCableEnd e1 = (PanelCableEnd) p.g1;
                PanelCableEnd e2 = (PanelCableEnd) p.g2;
                for (GrabableObject o : this.om.all) {
                    if (e1.saved_oid != -1 && o.__unique_id == e1.saved_oid) {
                        if (o instanceof Battery) {
                            e1.attach_to_battery((Battery) o);
                        } else if (o instanceof Panel) {
                            e1.attach_to_panel((Panel) o);
                        } else if (o instanceof Hub) {
                            e1.attach_to_hub((Hub) o);
                        } else if (o instanceof RocketEngine) {
                            e1.attach_to_rengine((RocketEngine) o);
                        } else if (o instanceof Button) {
                            e1.attach_to_button((Button) o);
                        }
                    }
                    if (e2.saved_oid != -1 && o.__unique_id == e2.saved_oid) {
                        if (o instanceof Battery) {
                            e2.attach_to_battery((Battery) o);
                        } else if (o instanceof Panel) {
                            e2.attach_to_panel((Panel) o);
                        } else if (o instanceof Hub) {
                            e2.attach_to_hub((Hub) o);
                        } else if (o instanceof RocketEngine) {
                            e2.attach_to_rengine((RocketEngine) o);
                        } else if (o instanceof Button) {
                            e2.attach_to_button((Button) o);
                        }
                    }
                }
                p.tick();
            }
            for (Cable p2 : this.om.cables) {
                CableEnd e12 = (CableEnd) p2.g1;
                CableEnd e22 = (CableEnd) p2.g2;
                for (GrabableObject o2 : this.om.all) {
                    if (e12.saved_oid != -1 && o2.__unique_id == e12.saved_oid) {
                        if (o2 instanceof Hub) {
                            e12.attach_to_hub((Hub) o2);
                        } else {
                            e12.attach_to(o2);
                        }
                    }
                    if (e22.saved_oid != -1 && o2.__unique_id == e22.saved_oid) {
                        if (o2 instanceof Hub) {
                            e22.attach_to_hub((Hub) o2);
                        } else {
                            e22.attach_to(o2);
                        }
                    }
                }
                p2.tick();
            }
        }
    }

    private void setup_corners(ObjectManager.Layer layer) {
        Iterator<MetalCorner> i = layer.metalcorners.iterator();
        while (i.hasNext()) {
            MetalCorner c = i.next();
            MetalBar o1 = null;
            MetalBar o2 = null;
            int n1 = c.b1_id;
            int n2 = c.b2_id;
            for (MetalBar o : layer.bars) {
                if (o.__unique_id == n1) {
                    o1 = o;
                } else if (o.__unique_id == n2) {
                    o2 = o;
                }
                if (o1 != null && o2 != null) {
                    break;
                }
            }
            if (o1 == null || o2 == null || !c.setup(world, o1, o2)) {
                Gdx.app.log("ERROR", "invalid metal corner object ids");
                try {
                    c.dispose(world);
                } catch (Exception e) {
                }
                i.remove();
            }
        }
    }

    private void reload() {
        try {
            this.level.reload_objects();
            load();
        } catch (IOException e) {
            Gdx.app.log("FATAL", "Could not reload objects");
        }
    }

    public void update_ropes() {
        if (rope_quality > 100) {
            rope_quality = 100;
        }
        if (rope_quality < 0) {
            rope_quality = 0;
        }
        if (!this.om.ropes.isEmpty()) {
            for (Rope rope : this.om.ropes) {
                rope.create_rope();
            }
        }
        if (!this.om.cables.isEmpty()) {
            for (Cable cable : this.om.cables) {
                cable.create_rope();
            }
        }
        if (!this.om.pcables.isEmpty()) {
            for (PanelCable panelCable : this.om.pcables) {
                panelCable.create_rope();
            }
        }
        for (Rope rope : this.om.ropes) {
            rope.stabilize();
        }
        for (Cable cable : this.om.cables) {
            cable.stabilize();
        }
        for (PanelCable panelCable : this.om.pcables) {
            panelCable.stabilize();
        }
    }

    @Override
    public void resume() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = false;
        Gdx.input.setInputProcessor(this);
        G.set_clear_color(0.1f, 0.1f, 0.1f);
        System.gc();
        this.lowfpscount = 0;
        this.finished_fade = 0.0f;
        this.disable_undo = false;
        this.commit_next = false;
        this.grabbed_object = null;
        this.widgets.disable(this.widget_elasticity);
        this.widgets.disable(this.widget_thrust);
        this.widgets.disable(this.widget_size);
        this.widgets.disable(this.widget_current);
        this.widgets.disable(this.widget_sizeb);
        this.widgets.disable(this.widget_voltage);
        this.widgets.disable(this.widget_dspeed);
        this.widgets.disable(this.widget_dforce);
        if (force_disable_shadows) {
            enable_shadows = false;
        } else {
            String tmp = Settings.get("enableshadows");
            enable_shadows = tmp.isEmpty() || tmp.equals("yes");
        }
        draw_fps = Settings.get("drawfps").equals("yes");
        String tmp2 = Settings.get("ropequality");
        rope_quality = (tmp2 == null || tmp2.isEmpty()) ? 100 : Integer.parseInt(tmp2);
        String tmp4 = Settings.get("bloom");
        if (tmp4.isEmpty() || tmp4.equals("yes")) {
            z = true;
        } else {
            z = false;
        }
        enable_bloom = z;
        String tmp5 = Settings.get("reflection");
        if (tmp5.isEmpty() || tmp5.equals("yes")) {
            z2 = true;
        } else {
            z2 = false;
        }
        enable_reflections = z2;
        String tmp6 = Settings.get("hqmeshes");
        if (tmp6.isEmpty() || tmp6.equals("yes")) {
            z3 = true;
        } else {
            z3 = false;
        }
        enable_hqmeshes = z3;
        String tmp7 = Settings.get("enablebg");
        if (tmp7.isEmpty() || tmp7.equals("yes")) {
            z4 = true;
        }
        enable_bg = z4;
        this.camera_h.camera_pos.z = 17.0f;
        update_ropes();
        has_multitouch = Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
        G.batch.setColor(Color.WHITE);
        if (level_type != 2) {
            set_mode(MODE_PAUSE);
        } else if (!sandbox) {
            pause_world();
            resume_world();
            set_mode(MODE_PLAY);
        }
        MiscRenderer.update_quality();
    }

    @Override
    public boolean screen_to_world(int x, int y, Vector2 out) {
        return false;
    }

    @Override
    public boolean ready() {
        return this.ready;
    }

    public void autosave() {
        prepare_save();
        try {
            this.level.save_jar(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/.autosave.jar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public void autosave_challenge() {
        if (this.level_n != -1) {
            Gdx.app.log("autosave", "Autosaving challenge " + this.level_n + "/" + this.level_id);
            prepare_save();
            try {
                this.level.save_jar(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/.autosave_" + this.level_n + (this.level_category == 2 ? "_2" : "") + ".jar"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
    }

    public void save() {
        if (this.level_filename != null) {
            prepare_save();
            try {
                this.level.save_jar(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/" + this.level_filename.replace("/", "_") + ".jar"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
            this.modified = false;
        }
    }

    public void prepare_save() {
        BaseObject[] objects = (BaseObject[]) this.om.all.toArray(new BaseObject[(this.om.all.size() + this.hinges.size())]);
        for (int x = this.om.all.size(); x < this.om.all.size() + this.hinges.size(); x++) {
            objects[x] = this.hinges.get(x - this.om.all.size());
        }
        this.level.set_objects(objects);
    }

    private void toggle_tracing() {
        if (tracing) {
            Settings.stop_tracing();
        } else {
            Settings.start_tracing();
        }
        tracing = !tracing;
    }

    @Override
    public boolean keyDown(int key) {
        Widget w;
        Widget w2;
        boolean z = true;
        switch (key) {
            case 4:
            case 30:
                if (from_sandbox) {
                    end_challenge_testing();
                } else if (sandbox) {
                    if (this.modified) {
                        ApparatusApp.backend.open_ingame_sandbox_back_menu();
                    } else {
                        this.tp.open_mainmenu();
                    }
                } else if (from_community) {
                    ApparatusApp.backend.open_ingame_back_community_menu();
                } else {
                    if (this.state == STATE_FINISHED) {
                        this.level_filename = ".lvl" + this.level_n + (this.level_category != 0 ? "_" + this.level_category : "");
                        pause_world();
                        save();
                        this.level_filename = null;
                    } else if (this.modified) {
                        if (mode == MODE_PLAY) {
                            pause_world();
                        }
                        autosave_challenge();
                        this.level_filename = null;
                    }
                    ApparatusApp.backend.open_ingame_back_menu();
                }
                Settings.save();
                break;
            case 7:
                set_bg(10);
                this.level_category = 2;
                break;
            case 8:
                set_bg(1);
                break;
            case 9:
                set_bg(2);
                break;
            case 10:
                set_bg(3);
                break;
            case 11:
                set_bg(4);
                break;
            case 12:
                set_bg(5);
                break;
            case 13:
                set_bg(6);
                break;
            case 14:
                set_bg(7);
                break;
            case 15:
                set_bg(8);
                break;
            case 16:
                set_bg(9);
                break;
            case 19:
            case 51:
                this.camera_h.add_velocity(0.0f, 1.0f);
                break;
            case 20:
            case 47:
                this.camera_h.add_velocity(0.0f, -1.0f);
                break;
            case 21:
                this.camera_h.add_velocity(-1.0f, 0.0f);
                break;
            case 22:
                this.camera_h.add_velocity(1.0f, 0.0f);
                break;
            case 29:
                if (!(a_down || this.active_panel == null || (w2 = this.active_panel.find(0)) == null)) {
                    w2.touch_down_local(0, 0);
                }
                a_down = true;
                break;
            case 32:
                if (!(d_down || this.active_panel == null || (w = this.active_panel.find(2)) == null)) {
                    w.touch_down_local(0, 0);
                }
                d_down = true;
                break;
            case 33:
                if (enable_bg) {
                    z = false;
                }
                enable_bg = z;
                break;
            case 34:
                physics_stability = 2;
                break;
            case 35:
                physics_stability = 1;
                break;
            case 41:
                this.camera_h.camera_pos.z += 1.0f;
                break;
            case 42:
                this.camera_h.camera_pos.z -= 1.0f;
                break;
            case 43:
                open(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/" + "TESTLEVEL.jar"));
                break;
            case 44:
                this.um.undo();
                break;
            case 45:
                if (enable_menu) {
                    z = false;
                }
                enable_menu = z;
                break;
            case 46:
                if (enable_shadows) {
                    z = false;
                }
                enable_shadows = z;
                break;
            case 48:
                toggle_tracing();
                break;
            case 49:
                this.level_filename = "TESTLEVEL";
                save();
                break;
            case 52:
                BaseObject o = ObjectFactory.adapter.create(world, 2, 18);
                Damper _d = (Damper) o;
                _d.g1.__unique_id = _d.__unique_id * 10000;
                _d.g2.__unique_id = _d.__unique_id * 10000000;
                this.om.add((GrabableObject) o);
                break;
            case 54:
                ChristmasGift oo = (ChristmasGift) ObjectFactory.adapter.create(world, 2, 17);
                oo.set_property("sx", 0.25f + (0.125f * ((float) new Random().nextInt(4))));
                oo.set_property("sy", 0.25f + (0.125f * ((float) new Random().nextInt(4))));
                Gdx.app.log("adding", "git");
                this.om.add(oo);
                break;
            case 82:
                if (!sandbox) {
                    ApparatusApp.backend.open_ingame_menu();
                    break;
                } else {
                    ApparatusApp.backend.open_ingame_sandbox_menu();
                    break;
                }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char arg0) {
        return false;
    }

    @Override
    public boolean keyUp(int key) {
        Widget w;
        Widget w2;
        switch (key) {
            case 29:
                a_down = false;
                if (!(this.active_panel == null || (w2 = this.active_panel.find(0)) == null)) {
                    w2.touch_up_local(0, 0);
                    break;
                }
            case 32:
                d_down = false;
                if (!(this.active_panel == null || (w = this.active_panel.find(2)) == null)) {
                    w.touch_up_local(0, 0);
                    break;
                }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void set_mode(int mode2) {
        switch (mode2) {
            case 0:
                mode = MODE_DEFAULT;
                return;
            case 1:
                mode = MODE_GRAB;
                return;
            case 2:
            default:
                return;
            case 3:
                mode = MODE_PLAY;
                return;
            case 4:
                mode = MODE_PAUSE;
                return;
        }
    }

    private void destroy_drag_body() {
        if (this.drag_body != null) {
            world.destroyBody(this.drag_body);
            this.drag_body = null;
        }
        if (this.ground_joint != null) {
            world.destroyJoint(this.ground_joint);
            this.ground_joint = null;
        }
    }

    private void create_drag_body(float x, float y) {
        if (this.drag_body != null) {
            destroy_drag_body();
        }
        if (this.grabbed_object != null && this.grabbed_object.num_hinges == 0) {
            this._rjd.initialize(this.ground, this.grabbed_object.body, this.grabbed_object.get_position());
            this.ground_joint = world.createJoint(this._rjd);
        }
        this.drag_body = world.createBody(this.drag_bd);
        this.drag_body.createFixture(this.drag_fd);
        this.drag_body_target.set(x, y);
        this.drag_body.setType(BodyDef.BodyType.StaticBody);
        this.drag_body.setTransform(this.drag_body_target, 0.0f);
        this.drag_body.setType(BodyDef.BodyType.DynamicBody);
        this.drag_body.setUserData(this);
    }

    /* access modifiers changed from: package-private */
    public void pause_world() {
        this.state = STATE_PLAYING;
        this.lowfpscount = 0;
        if (enable_multithreading && this.sim_thread != null) {
            this.sim_thread.terminate();
            this.sim_thread = null;
        }
        world.setGravity(new Vector2(0.0f, 0.0f));
        this.active_panel = null;
        this.camera_h.release_target();
        if (camera_reset && level_type != 2) {
            this.camera_h.load();
        }
        this.camera_h.velocity.set(0.0f, 0.0f, 0.0f);
        world.setContactFilter(this.contact_handler);
        world.setContactListener(this.contact_handler);
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(false);
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.pause();
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(false);
        }
        for (StaticMotor staticMotor : this.om.static_motors) {
            staticMotor.joint_pause();
        }
        for (DynamicMotor dynamicMotor : this.om.layer0.dynamicmotors) {
            dynamicMotor.joint_pause();
        }
        for (DynamicMotor dynamicMotor : this.om.layer1.dynamicmotors) {
            dynamicMotor.joint_pause();
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(false);
        }
        for (Hinge hinge : this.hinges) {
            hinge.recreate(world);
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.pause();
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(true);
        }
        if (!sandbox) {
            create_metal_cache();
        }
        for (Rope rope : this.om.ropes) {
            rope.stabilize();
        }
        for (PanelCable panelCable : this.om.pcables) {
            panelCable.stabilize();
        }
        Iterator<Cable> it13 = this.om.cables.iterator();
        while (it13.hasNext()) {
            it13.next().stabilize();
        }
        this.contact_handler.clean();
        set_mode(MODE_PAUSE);
        do_connectanims = false;
        if (Level.version >= 3) {
            resolve_cable_connections();
        } else {
            for (Cable r : this.om.cables) {
                r.g1.body.setActive(true);
                r.g1.body.setAwake(true);
                r.g2.body.setActive(true);
                r.g2.body.setAwake(true);
                r.g1.body.setTransform(this._tmp.set(r.g1.get_position().x + 1.0E-11f, r.g1.get_position().y), 0.0f);
                r.g2.body.setTransform(this._tmp.set(r.g2.get_position().x + 1.0E-11f, r.g2.get_position().y), 0.0f);
                r.g1.translate(r.g1.get_position().x, r.g1.get_position().y);
                r.g2.translate(r.g2.get_position().x, r.g2.get_position().y);
                r.g1.body.setFixedRotation(true);
                r.g2.body.setFixedRotation(false);
            }
            for (PanelCable r2 : this.om.pcables) {
                r2.g1.body.setActive(true);
                r2.g1.body.setAwake(true);
                r2.g2.body.setActive(true);
                r2.g2.body.setAwake(true);
                r2.g1.body.setTransform(this._tmp.set(r2.g1.get_position().x + 1.0E-11f, r2.g1.get_position().y), 0.0f);
                r2.g2.body.setTransform(this._tmp.set(r2.g2.get_position().x + 1.0E-11f, r2.g2.get_position().y), 0.0f);
                r2.g1.translate(r2.g1.get_position().x, r2.g1.get_position().y);
                r2.g2.translate(r2.g2.get_position().x, r2.g2.get_position().y);
                r2.g1.body.setFixedRotation(true);
                r2.g2.body.setFixedRotation(false);
            }
            for (Panel p : this.om.layer0.controllers) {
                p.body.setFixedRotation(false);
                p.translate(p.get_position().x, p.get_position().y);
            }
        }
        do_connectanims = true;
        this.num_balls_in_goal = 0;
        this.num_balls = 0;
        SoundManager.stop_all();
        this.time_last = System.nanoTime();
        this.time_accum = 0;
        if (!sandbox && level_type == 2) {
            resume_world();
        }
        if (sandbox && this.modified) {
            autosave();
            this.last_autosave = System.currentTimeMillis();
        }
        world.step(1.0f, 20, 20);
    }

    public void resume_world() {
        this.state = STATE_PLAYING;
        this.pending_follow = false;
        set_mode(MODE_PLAY);
        this.lowfpscount = 0;
        this.contact_handler.reset();
        this.camera_h.velocity.set(0.0f, 0.0f, 0.0f);
        this.num_balls_in_goal = 0;
        this.num_balls = this.om.layer0.marbles.size() + this.om.layer1.marbles.size() + this.om.christmasgifts.size();
        world.setContactFilter(this.ingame_contact_handler);
        world.setContactListener(this.ingame_contact_handler);
        world.setGravity(new Vector2(0.0f, -9.8f));
        this.camera_h.save();
        for (Hinge hinge : this.hinges) {
            hinge.save();
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(false);
        }
        world.step(1.0f, 500, 500);
        for (StaticMotor staticMotor : this.om.static_motors) {
            staticMotor.joint_play();
        }
        for (DynamicMotor dynamicMotor : this.om.layer0.dynamicmotors) {
            dynamicMotor.joint_play();
        }
        for (DynamicMotor dynamicMotor : this.om.layer1.dynamicmotors) {
            dynamicMotor.joint_play();
        }
        for (Hinge hinge : this.hinges) {
            hinge.recreate(world);
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.play();
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(true);
        }
        System.gc();
        this.time_last = System.nanoTime();
        this.time_accum = 0;
        this.game_start = System.currentTimeMillis();
        if (enable_multithreading) {
            this.sim_thread = new SimulationThread(this, null);
            this.sim_thread.setPriority(5);
            this.sim_thread.start();
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(false);
        }
        world.step(0.001f, 1, 1);
        for (StaticMotor staticMotor : this.om.static_motors) {
            staticMotor.joint_play();
        }
        for (DynamicMotor dynamicMotor : this.om.layer0.dynamicmotors) {
            dynamicMotor.joint_play();
        }
        for (DynamicMotor dynamicMotor : this.om.layer1.dynamicmotors) {
            dynamicMotor.joint_play();
        }
        for (Hinge hinge : this.hinges) {
            hinge.recreate(world);
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.play();
        }
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.set_active(true);
        }
    }

    public void grab_object(GrabableObject o) {
        int i = -1;
        int i2 = 1;
        if (this.grabbed_object != null) {
            release_object();
        }
        this.grabbed_object = o;
        set_mode(MODE_GRAB);
        if (sandbox) {
            if (o instanceof Bar) {
                Bar b = (Bar) o;
                this.widgets.enable(this.widget_size);
                if (b.size.x / 2.0f <= 3.99f) {
                    i2 = b.size.x / 2.0f > 1.99f ? 0 : -1;
                }
                this.widget_size.value = (float) i2;
            } else if (o instanceof Wheel) {
                Wheel w = (Wheel) o;
                this.widgets.enable(this.widget_size);
                if (w.size <= 1.99f) {
                    i2 = w.size > 0.99f ? 0 : -1;
                }
                this.widget_size.value = (float) i2;
            } else if (o instanceof BaseRopeEnd) {
                BaseRopeEnd b2 = (BaseRopeEnd) o;
                this.widgets.enable(this.widget_size);
                this.widget_size.value = ((b2.get_baserope().size / 5.0f) - 1.0f) - 4.0f;
                this.widget_size.value = Math.min(1.0f, this.widget_size.value);
                if (o instanceof RopeEnd) {
                    this.widgets.enable(this.widget_elasticity);
                    if (!b2.get_baserope().rubber) {
                        i2 = -1;
                    }
                    this.widget_elasticity.value = (float) i2;
                }
            } else if (o instanceof DamperEnd) {
                DamperEnd e = (DamperEnd) o;
                this.widgets.enable(this.widget_dspeed);
                this.widgets.enable(this.widget_dforce);
                this.widget_dspeed.value = ((e.damper.speed / 200.0f) * 2.0f) - 4.0f;
                this.widget_dforce.value = ((e.damper.force / 1000.0f) * 2.0f) - 4.0f;
            } else if (o instanceof Battery) {
                Battery h = (Battery) o;
                this.widgets.enable(this.widget_current);
                this.widgets.enable(this.widget_voltage);
                this.widgets.enable(this.widget_sizeb);
                if (h.size != 1) {
                    i = 1;
                }
                this.widget_sizeb.value = (float) i;
                this.widget_current.value = (h.current * 2.0f) - 1.0f;
                this.widget_voltage.value = (h.voltage * 2.0f) - 1.0f;
            } else if (o instanceof RocketEngine) {
                this.widgets.enable(this.widget_thrust);
                this.widget_thrust.value = (((RocketEngine) o).thrust * 2.0f) - 1.0f;
            }
        }
        o.grab();
    }

    public void release_object() {
        this.widgets.disable(this.widget_size);
        this.widgets.disable(this.widget_current);
        this.widgets.disable(this.widget_thrust);
        this.widgets.disable(this.widget_sizeb);
        this.widgets.disable(this.widget_voltage);
        this.widgets.disable(this.widget_elasticity);
        this.widgets.disable(this.widget_dforce);
        this.widgets.disable(this.widget_dspeed);
        if (this.grabbed_object != null) {
            this.grabbed_object.release();
            this.last_grabbed = this.grabbed_object;
            this.grabbed_object = null;
            set_mode(MODE_PAUSE);
        }
    }

    /* access modifiers changed from: package-private */
    public void remove_selected() {
        if (this.grabbed_object != null) {
            GrabableObject o = this.grabbed_object;
            release_object();
            if (!this.disable_undo) {
                this.um.begin_step(0, null);
                this.um.add_object(o);
            }
            remove_object(o);
            if (!this.disable_undo) {
                this.um.commit_step();
            }
            this.grabbed_object = null;
            set_mode(MODE_PAUSE);
            if (this.last_grabbed == o) {
                this.last_grabbed = null;
            }
        }
    }

    public void remove_object(GrabableObject o) {
        if (o instanceof RopeEnd) {
            this.om.remove(o);
            remove_potential_fixture_pair(((RopeEnd) o).rope.g1.body.getFixtureList().get(0));
            remove_potential_fixture_pair(((RopeEnd) o).rope.g2.body.getFixtureList().get(0));
            remove_potential_hinges(((RopeEnd) o).rope.g1);
            remove_potential_hinges(((RopeEnd) o).rope.g2);
            ((RopeEnd) o).rope.dispose(world);
            this.grabbed_object = null;
            set_mode(MODE_PAUSE);
        } else if (o instanceof CableEnd) {
            this.om.remove(o);
            ((CableEnd) o).cable.dispose(world);
            this.grabbed_object = null;
            set_mode(MODE_PAUSE);
        } else if (o instanceof PanelCableEnd) {
            this.om.remove(o);
            ((PanelCableEnd) o).cable.dispose(world);
            this.grabbed_object = null;
            set_mode(MODE_PAUSE);
        } else if (o instanceof DamperEnd) {
            this.om.remove(((DamperEnd) o).damper);
            remove_potential_fixture_pair(((DamperEnd) o).damper.g1.body.getFixtureList().get(0));
            remove_potential_fixture_pair(((DamperEnd) o).damper.g1.body.getFixtureList().get(1));
            remove_potential_fixture_pair(((DamperEnd) o).damper.g2.body.getFixtureList().get(0));
            remove_potential_fixture_pair(((DamperEnd) o).damper.g2.body.getFixtureList().get(1));
            remove_potential_hinges(((DamperEnd) o).damper.g1);
            remove_potential_hinges(((DamperEnd) o).damper.g2);
            ((DamperEnd) o).damper.dispose(world);
            this.grabbed_object = null;
            set_mode(MODE_PAUSE);
        } else {
            this.om.remove(o);
            remove_potential_hinges(o);
            if (o.body != null) {
                remove_potential_fixture_pair(o.body);
            }
            o.dispose(world);
        }
    }

    public void remove_potential_hinges(GrabableObject o) {
        remove_potential_hinges(o, false, true);
    }

    private void remove_potential_hinges(GrabableObject o, boolean anim, boolean override_fixed) {
        if (o instanceof BaseMotor) {
            if (!this.disable_undo) {
                this.um.save_basemotor((BaseMotor) o);
            }
            ((BaseMotor) o).detach();
            if (anim) {
                connectanims.add(new ConnectAnim(1, o.get_position().x, o.get_position().y));
            }
            if (o instanceof DynamicMotor) {
                Iterator<Hinge> i = this.hinges.iterator();
                while (i.hasNext()) {
                    Hinge h = i.next();
                    if ((!h.fixed || override_fixed) && (h.body1_id == o.__unique_id || h.body2_id == o.__unique_id)) {
                        if (!this.disable_undo) {
                            this.um.save_hinge(h);
                        }
                        if (anim) {
                            connectanims.add(new ConnectAnim(1, h.get_position().x, h.get_position().y));
                        }
                        h.dispose(world);
                        i.remove();
                    }
                }
                return;
            }
            return;
        }
        Iterator<Hinge> i2 = this.hinges.iterator();
        while (i2.hasNext()) {
            Hinge h2 = i2.next();
            if ((!h2.fixed || override_fixed) && (h2.body1_id == o.__unique_id || h2.body2_id == o.__unique_id)) {
                if (!this.disable_undo) {
                    this.um.save_hinge(h2);
                }
                if (anim) {
                    connectanims.add(new ConnectAnim(1, h2.get_position().x, h2.get_position().y));
                }
                h2.dispose(world);
                i2.remove();
            }
        }
        for (StaticMotor h3 : this.om.static_motors) {
            if ((!h3.fixed || override_fixed) && h3.attached_object == o) {
                if (!this.disable_undo) {
                    this.um.save_basemotor(h3);
                }
                if (anim) {
                    connectanims.add(new ConnectAnim(1, h3.get_position().x, h3.get_position().y));
                }
                h3.detach();
            }
        }
        for (DynamicMotor h4 : this.om.layer0.dynamicmotors) {
            if ((!h4.fixed || override_fixed) && h4.attached_object == o) {
                if (!this.disable_undo) {
                    this.um.save_basemotor(h4);
                }
                if (anim) {
                    connectanims.add(new ConnectAnim(1, h4.get_position().x, h4.get_position().y));
                }
                h4.detach();
            }
        }
        for (DynamicMotor h5 : this.om.layer1.dynamicmotors) {
            if ((!h5.fixed || override_fixed) && h5.attached_object == o) {
                if (!this.disable_undo) {
                    this.um.save_basemotor(h5);
                }
                if (anim) {
                    connectanims.add(new ConnectAnim(1, h5.get_position().x, h5.get_position().y));
                }
                h5.detach();
            }
        }
    }

    public void remove_potential_fixture_pair(Body b) {
        for (Fixture fixture : b.getFixtureList()) {
            remove_potential_fixture_pair(fixture);
        }
    }

    public void remove_potential_fixture_pair(Fixture a) {
        if (a != null) {
            for (int x = 0; x < ContactHandler.num_fixture_pairs; x++) {
                ContactHandler.FixturePair pair = ContactHandler.fixture_pairs[x];
                if (pair.a == a || pair.b == a) {
                    pair.invalid = true;
                }
            }
        }
    }

    @Override
    public boolean touchDown(int x, int y, int p, int button) {
        if (p >= this.widget.length) {
            return true;
        }
        this.widget[p] = false;
        Gdx.app.log("x ", "x " + x);
        if (this.state == STATE_FINISHED) {
            if (((float) x) > 0.4f * ((float) G.realwidth) && ((float) x) < 0.6195313f * ((float) G.realwidth) && ((float) y) > 0.5f * ((float) G.realheight) && ((float) y) < 0.62777776f * ((float) G.realheight)) {
                if (this.level_n != -1) {
                    int next = this.level_n + 1;
                    SoundManager.play_startlevel();
                    ApparatusApp.instance.play(level_type, next);
                    this.do_save = true;
                } else {
                    Gdx.app.log("autosave", "opening community!");
                    ApparatusApp.backend.open_community();
                }
            }
            if (((float) x) > 0.4f * ((float) G.realwidth) && ((float) x) < 0.6039063f * ((float) G.realwidth) && ((float) y) > 0.69027776f * ((float) G.realheight) && ((float) y) < 0.82916665f * ((float) G.realheight)) {
                if (this.level_n == -1) {
                    Settings.msg(L.get("uploading_solution_error"));
                    pause_world();
                    this.state = STATE_PLAYING;
                } else {
                    sandbox = true;
                    this.state = STATE_PLAYING;
                    level_type = 0;
                    this.level_filename = ".lvl" + this.level_n + (this.level_category != 0 ? "_" + this.level_category : "");
                    this.level.type = "apparatus";
                    pause_world();
                    this.do_save = false;
                    save();
                    this.level_filename = "";
                    from_game = true;
                }
            }
        }
        if (p < 3) {
            this.num_touch_points++;
            Vector2 v = this._last_vec[p].set((float) x, (float) (G.realheight - y));
            this._touch_vec[p].set(this._last_vec[p]);
            if (p == 1) {
                this._last_dist = this._touch_vec[1].dst(this._touch_vec[0]);
                this.allow_swipe = false;
            } else if (p == 0) {
                this.prevent_nail = false;
                this._last_touch.set((float) x, (float) y);
                this.undo_begun = false;
            } else {
                this.allow_swipe = false;
            }
            if (mode != MODE_PLAY) {
                this.widget[p] = true;
                this._tmpv.set(v.x * (((float) G.width) / ((float) G.realwidth)), v.y * (((float) G.height) / ((float) G.realheight)));
                Gdx.app.log("pospos", "pos " + this._tmpv.x + " " + this._tmpv.y);
                if ((!sandbox || !touch_handle_sandbox_btns(this._tmpv, p)) && ((this.grabbed_object == null || !touch_handle_object_btns(this._tmpv)) && !touch_handle_left_btns(this._tmpv, p))) {
                    this.widget[p] = false;
                    long time = System.currentTimeMillis();
                    if (!from_community || level_type == 1) {
                        if (p == 0) {
                            if (this.grabbed_object != null && !this.grabbed_object.fixed_rotation && !(this.grabbed_object instanceof Wheel)) {
                                Intersector.intersectRayPlane(G.p_cam.getPickRay((float) x, (float) y), yaxis, this.iresult);
                                float angle = this.grabbed_object.get_state().angle;
                                this.tmp3.set(this.grabbed_object.get_state().position.x, this.grabbed_object.get_state().position.y, 0.0f);
                                float size = 2.0f;
                                if (this.grabbed_object instanceof Bar) {
                                    size = (((Bar) this.grabbed_object).size.x / 2.0f) + 2.0f;
                                }
                                Vector3 vector3 = this.tmp3;
                                vector3.x = (float) (((double) vector3.x) + (Math.cos((double) angle) * ((double) (0.5f + size))));
                                Vector3 vector32 = this.tmp3;
                                vector32.y = (float) (((double) vector32.y) + (Math.sin((double) angle) * ((double) (0.5f + size))));
                                if (this.iresult.dst(this.tmp3) < 1.25f + (this.camera_h.camera_pos.z / 80.0f)) {
                                    this.rotate_point.set(this.grabbed_object.get_state().position);
                                    if (this.grabbed_object instanceof MetalBar) {
                                        ((MetalBar) this.grabbed_object).escape_corners();
                                    }
                                    this.rotating = true;
                                    this.um.begin_step(2, this.grabbed_object);
                                    this.last_touch_time = time;
                                }
                            }
                            Body result = raycast_find_body((float) x, (float) y);
                            if (result != null) {
                                this.grab_offs.set(this._tmp);
                                this.grab_offs.sub(result.getPosition());
                                destroy_drag_body();
                                grab_object((GrabableObject) result.getUserData());
                                set_mode(MODE_GRAB);
                            } else {
                                set_mode(MODE_PAUSE);
                            }
                        }
                        this.last_touch_time = time;
                    }
                }
            } else {
                this._tmpv.set(v.x * (((float) G.width) / ((float) G.realwidth)), v.y * (((float) G.height) / ((float) G.realheight)));
                if (!touch_handle_left_btns(this._tmpv, p) && p == 0) {
                    if (this.mousejoint != null) {
                        world.destroyJoint(this.mousejoint);
                        this.mousejoint = null;
                    }
                    Body result2 = raycast_find_body((float) x, (float) y);
                    if (result2 != null) {
                        if (this.pending_follow) {
                            this.pending_follow = false;
                            this.camera_h.set_target((GrabableObject) this.query_result_body.getUserData(), 3.0f, -3.0f);
                            this.state = STATE_PLAYING;
                            this.time_last = System.nanoTime();
                            this.time_accum = 0;
                            world.step(0.001f, 1, 1);
                        } else if (result2.getUserData() instanceof Panel) {
                            this.active_panel = (Panel) result2.getUserData();
                        } else if (result2.getUserData() instanceof Knob) {
                            _mjd.bodyA = this.ground;
                            _mjd.bodyB = result2;
                            _mjd.maxForce = ((Knob) result2.getUserData()).size * 3000.0f;
                            _mjd.frequencyHz = 10.0f;
                            _mjd.dampingRatio = 2.0f;
                            _mjd.target.set(result2.getWorldCenter());
                            this.mousejoint = (MouseJoint) world.createJoint(_mjd);
                            this.widget[p] = true;
                        }
                    } else if (this.pending_follow) {
                        this.camera_h.release_target();
                        this.pending_follow = false;
                        this.state = STATE_PLAYING;
                        this.time_last = System.nanoTime();
                        this.time_accum = 0;
                        world.step(0.001f, 1, 1);
                    }
                }
            }
        }
        return true;
    }

    private void open_hinge_menu(ContactHandler.FixturePair pair) {
        this.wrench_anim_pos.set(pair.point);
        this.hingeselect = true;
        this.hingepair = pair;
    }

    /* JADX WARNING: Removed duplicated region for block: B:96:0x01cb  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0030  */
    private boolean touch_handle_object_btns(Vector2 v) {
        int i;
        int i2 = 0;
        boolean z = false;
        boolean z2 = false;
        int i3 = 0;
        boolean r = false;
        int h = G.height;
        int w = G.width;
        this.um.begin_step(5, this.grabbed_object);
        if (sandbox) {
            if (this.widgets.touch_down((int) v.x, ((int) v.y) - ((fix_bottombar ? 1 : 0) * 64))) {
                r = true;
                if (r) {
                    this.modified = true;
                } else {
                    this.um.cancel_step();
                }
                return r;
            }
        }
        if (v.y <= ((float) (h - 64)) || v.x <= ((float) (w - 192))) {
            if (v.x > ((float) (w - 64))) {
                switch ((h - ((int) v.y)) / 64) {
                    case 1:
                        if (sandbox) {
                            remove_selected();
                            r = true;
                            break;
                        }
                        break;
                }
            }
            if (r) {
            }
            return r;
        }
        switch ((((int) v.x) - (w - 192)) / 64) {
            case 0:
                if (this.grabbed_object instanceof BaseMotor) {
                    BaseMotor baseMotor = (BaseMotor) this.grabbed_object;
                    if (((BaseMotor) this.grabbed_object).dir > 0.0f) {
                        i = -1;
                    } else {
                        i = 1;
                    }
                    baseMotor.dir = (float) i;
                    r = true;
                    break;
                }
                break;
            case 1:
                if (this.grabbed_object.num_hinges == 0) {
                    Settings.msg(L.get("notattached"));
                    this.um.cancel_step();
                } else {
                    this.um.begin_step(4, this.grabbed_object);
                    GrabableObject grabableObject = this.grabbed_object;
                    if (sandbox) {
                        z = true;
                    }
                    remove_potential_hinges(grabableObject, true, z);
                    this.um.commit_step();
                    SoundManager.play_detach();
                }
                r = true;
                break;
            case 2:
                if (this.grabbed_object instanceof Battery) {
                    Battery battery = (Battery) this.grabbed_object;
                    Battery battery2 = (Battery) this.grabbed_object;
                    if (!((Battery) this.grabbed_object).real_on) {
                        z2 = true;
                    }
                    battery2.real_on = z2;
                    battery.on = z2;
                } else if (this.grabbed_object instanceof StaticMotor) {
                    Settings.msg(L.get("motorcantmove"));
                    this.um.cancel_step();
                } else if (this.grabbed_object instanceof Panel) {
                    ((Panel) this.grabbed_object).set_type((((Panel) this.grabbed_object).type + 1) % Panel.num_types);
                } else if (this.grabbed_object.num_hinges == 0) {
                    if (!this.grabbed_object.fixed_layer) {
                        if (this.level_n >= 7 || this.level_n == -1 || this.level_category == 2) {
                            if (sandbox && (this.grabbed_object instanceof Plank)) {
                                this.om.relayer(this.grabbed_object, (this.grabbed_object.layer + 1) % 3);
                            } else if (this.grabbed_object instanceof DamperEnd) {
                                Damper d = ((DamperEnd) this.grabbed_object).damper;
                                if (d.g1.num_hinges == 0 && d.g2.num_hinges == 0) {
                                    if (this.grabbed_object.layer != 1) {
                                        i3 = 1;
                                    }
                                    this.om.relayer(d, i3);
                                } else {
                                    Settings.msg(L.get("mustdetach"));
                                }
                            } else {
                                GrabableObject grabableObject2 = this.grabbed_object;
                                if (this.grabbed_object.layer != 1) {
                                    i2 = 1;
                                }
                                this.om.relayer(grabableObject2, i2);
                            }
                            if (this.grabbed_object.body != null) {
                                remove_potential_fixture_pair(this.grabbed_object.body);
                            }
                            this.grabbed_object.reshape();
                        } else {
                            Settings.msg(L.get("btnavailablelvl8"));
                            this.um.cancel_step();
                        }
                    }
                } else if (this.level_n >= 7 || this.level_n == -1 || this.level_category == 2) {
                    Settings.msg(L.get("mustdetach"));
                    this.um.cancel_step();
                } else {
                    Settings.msg(L.get("btnavailablelvl8"));
                    this.um.cancel_step();
                }
                r = true;
                break;
        }
        if (r) {
        }
        return r;
    }

    private boolean touch_handle_left_btns(Vector2 v, int p) {
        boolean r = false;
        int h = G.height;
        int i = G.width;
        if (this.hingeselect) {
            this.tmp3.set(this.wrench_anim_pos.x, this.wrench_anim_pos.y, 1.0f);
            G.p_cam.project(this.tmp3);
            this.tmp3.set(this.tmp3.x * (((float) G.width) / ((float) G.realwidth)), this.tmp3.y * (((float) G.height) / ((float) G.realheight)), 0.0f);
            if (v.x <= this.tmp3.x || v.x >= this.tmp3.x + 128.0f || v.y <= this.tmp3.y || v.y >= this.tmp3.y + 64.0f) {
                this.hingeselect = false;
            } else {
                int btn = (((int) v.x) - ((int) this.tmp3.x)) / 64;
                this.wrench_anim_start = System.currentTimeMillis();
                if (btn == 1) {
                    SoundManager.play_hammer();
                }
                create_hinge(this.hingepair, btn);
                this.hingepair.invalid = true;
                this.hingeselect = false;
                return true;
            }
        }
        if (mode == MODE_PLAY) {
            if (this.active_panel != null) {
                this._tmpv.set(v.x, v.y);
                if (this.active_panel.widgets.touch_down((int) this._tmpv.x, (int) this._tmpv.y, p)) {
                    this.widget[p] = true;
                    return true;
                }
            }
            if (v.y > ((float) (G.height - 64)) && v.x > ((float) (G.width - 64))) {
                this.pending_follow = true;
                this.widget[p] = true;
                return true;
            }
        }
        if (p == 0 && v.x < 64.0f) {
            switch ((h - ((int) v.y)) / 56) {
                case 0:
                    if (mode == MODE_PLAY) {
                        pause_world();
                        pause_world();
                    } else if (!sandbox || testing_challenge || level_type != 1) {
                        resume_world();
                    } else {
                        ApparatusApp.backend.open_sandbox_play_options();
                    }
                    r = true;
                    break;
                case 1:
                    if (mode != MODE_PLAY) {
                        this.um.undo();
                        r = true;
                        break;
                    }
                    break;
                case 2:
                    if (mode == MODE_PLAY) {
                        if (level_type == 2 && this.msg != null) {
                            ApparatusApp.backend.open_infobox(this.msg);
                            r = true;
                            break;
                        }
                    } else if (this.msg != null) {
                        ApparatusApp.backend.open_infobox(this.msg);
                        r = true;
                        break;
                    }
                    break;
                case 3:
                    if (!has_multitouch) {
                        this.camera_h.camera_pos.z += 4.0f;
                    }
                    r = true;
                    break;
                case 4:
                    if (!has_multitouch) {
                        this.camera_h.camera_pos.z -= 4.0f;
                    }
                    r = true;
                    break;
            }
        } else if (p == 0 && sandbox && from_game && mode != MODE_PLAY && v.x < 208.0f && v.y > ((float) (h - 48))) {
            release_object();
            SoundManager.play_startlevel();
            open(this.level_category, this.level_n + 1);
            this.num_touch_points = 0;
            level_type = 1;
        }
        return r;
    }

    private boolean touch_handle_sandbox_category_btns(int offs, int p) {
        if (offs >= this.sandbox_category_cache_id.length) {
            return false;
        }
        this.open_sandbox_category = offs;
        this.open_animate_dir = 1;
        this.open_animate_time = 0.0f;
        this.widget[p] = true;
        return true;
    }

    private boolean touch_handle_sandbox_btns(Vector2 v, int p) {
        boolean ret = false;
        int i = G.height;
        int w = G.width;
        if (v.y - ((float) ((fix_bottombar ? 1 : 0) * 64)) < 64.0f) {
            ret = true;
            GrabableObject o = null;
            int offs = (w - ((int) v.x)) / 56;
            Gdx.app.log("offs", String.valueOf(offs));
            if (this.open_sandbox_category == -1) {
                return touch_handle_sandbox_category_btns(offs, p);
            }
            if (offs == 0) {
                this.open_animate_dir = -1;
                this.open_animate_time = 0.2f;
                return true;
            }
            switch (this.open_sandbox_category) {
                case 0:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 13);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 15);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 14);
                            break;
                        case 3:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 16);
                            break;
                        case 4:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 18);
                            Damper _d = (Damper) o;
                            _d.g1.__unique_id = _d.__unique_id * 10000;
                            _d.g2.__unique_id = _d.__unique_id * 10000000;
                            break;
                    }
                    break;
                case 1:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 1);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 2);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 12);
                            break;
                    }
                    break;
                case 2:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 3, 0);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 8);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 7);
                            break;
                        case 3:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 6);
                            Cable _pc = (Cable) o;
                            _pc.g1.__unique_id = _pc.__unique_id * 10000;
                            _pc.g2.__unique_id = _pc.__unique_id * 10000000;
                            break;
                        case 4:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 11);
                            PanelCable _p = (PanelCable) o;
                            _p.g1.__unique_id = _p.__unique_id * 10000;
                            _p.g2.__unique_id = _p.__unique_id * 10000000;
                            break;
                        case 5:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 10);
                            break;
                    }
                    break;
                case 3:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 3);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 0);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 1);
                            break;
                    }
                    break;
                case 4:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 5);
                            Rope r = (Rope) o;
                            r.g1.__unique_id = r.__unique_id * 10000;
                            r.g2.__unique_id = r.__unique_id * 10000000;
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 0);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 4);
                            break;
                    }
                    break;
            }
            if (o != null) {
                this.modified = true;
                this.ghost_object = o;
                o.ghost = true;
                this.pending_ghost = true;
                this.dragging_ghost = true;
                if (o instanceof BaseRope) {
                    ((BaseRope) o).g1.ghost = true;
                    ((BaseRope) o).g2.ghost = true;
                    ((BaseRope) o).g1.translate(-1.0f, -20000.0f);
                    ((BaseRope) o).g2.translate(1.0f, -20000.0f);
                } else if (o instanceof PanelCable) {
                    ((PanelCable) o).g1.ghost = true;
                    ((PanelCable) o).g2.ghost = true;
                    ((PanelCable) o).g1.translate(-1.0f, -20000.0f);
                    ((PanelCable) o).g2.translate(1.0f, -20000.0f);
                } else if (o instanceof Damper) {
                    ((Damper) o).g1.ghost = true;
                    ((Damper) o).g2.ghost = true;
                    ((Damper) o).g1.translate(-20000.0f, ((Damper) o).size / 2.0f);
                    ((Damper) o).g2.translate(-20000.0f, (-((Damper) o).size) / 2.0f);
                } else {
                    o.translate(0.0f, -20000.0f);
                }
                o.save_state();
                o.pause();
                if ((o instanceof MetalBar) || (o instanceof Marble) || (o instanceof StaticMotor)) {
                    o.body.setType(BodyDef.BodyType.DynamicBody);
                }
                this.widget[p] = false;
            } else {
                ret = false;
            }
        }
        return ret;
    }

    @Override
    public boolean touchUp(int x, int y, int p, int button) {
        if (p < this.widget.length && p < 3) {
            this.num_touch_points--;
            if (this.num_touch_points <= 0) {
                this.num_touch_points = 0;
                this.allow_swipe = true;
            }
            if (this.widget[p] && mode != MODE_PLAY) {
                this.um.commit_step();
            }
            if (p == 0 && mode != MODE_PLAY) {
                long time = System.currentTimeMillis();
                Intersector.intersectRayPlane(G.p_cam.getPickRay((float) x, (float) y), yaxis, this.tmp3);
                if (time - this.last_touch_time < 333 && time - this.wrench_anim_start > 150) {
                    ContactHandler.FixturePair found_pair = getFixturePair();
                    if (found_pair != null) {
                        if (found_pair.same_layer || (found_pair.a.getBody().getUserData() instanceof BaseMotor) || (found_pair.b.getBody().getUserData() instanceof BaseMotor)) {
                            create_hinge(found_pair, 1);
                            SoundManager.play_hammer();
                            found_pair.invalid = true;
                        } else if (this.level_n <= -1 || this.level_n >= 12 || this.level_category == 2) {
                            open_hinge_menu(found_pair);
                        } else {
                            SoundManager.play_hammer();
                            create_hinge(found_pair, 0);
                            found_pair.invalid = true;
                        }
                    }
                }
                if (this.dragging_ghost) {
                    release_ghost();
                }
                if (this.rotating && time - this.last_touch_time < 100) {
                    Body result = raycast_find_body((float) x, (float) y);
                    if (result != null) {
                        this.grab_offs.set(this._tmp);
                        this.grab_offs.sub(result.getPosition());
                        destroy_drag_body();
                        grab_object((GrabableObject) result.getUserData());
                        set_mode(MODE_GRAB);
                    } else {
                        set_mode(MODE_PAUSE);
                    }
                }
                this.rotating = false;
                if (this.grabbed_object != null) {
                    this.grabbed_object.release();
                    if (System.currentTimeMillis() - this.last_touch_time < 200 && this.drag_body != null) {
                        release_object();
                    }
                }
                destroy_drag_body();
                this.commit_next = true;
            } else if (mode == MODE_PLAY) {
                if (p == 0 && this.mousejoint != null) {
                    world.destroyJoint(this.mousejoint);
                    this.mousejoint = null;
                }
                if (this.widget[p] && this.active_panel != null) {
                    this.active_panel.widgets.touch_up(x, y, p);
                }
            }
        }
        return true;
    }

    private ContactHandler.FixturePair getFixturePair() {
        ContactHandler.FixturePair found_pair = null;
        float found_dst = 0.0f;
        for (int f = 0; f < ContactHandler.num_fixture_pairs; f++) {
            ContactHandler.FixturePair fp = ContactHandler.fixture_pairs[f];
            Vector2 pt = fp.get_intersection_point();
            if (pt != null && (fp.a.getBody().getUserData() == this.last_grabbed || fp.b.getBody().getUserData() == this.last_grabbed)) {
                float dist = pt.dst(this.tmp3.x, this.tmp3.y);
                if (dist < 3.0f && (found_pair == null || dist < found_dst)) {
                    found_dst = pt.dst(this.tmp3.x, this.tmp3.y);
                    found_pair = fp;
                }
            }
        }
        return found_pair;
    }

    private void release_ghost() {
        this.dragging_ghost = false;
        this.ghost_object.ghost = false;
        world.step(0.001f, 20, 20);
        release_object();
        world.step(0.001f, 20, 20);
        if (this.ghost_object instanceof BaseRope) {
            ((BaseRope) this.ghost_object).g1.ghost = false;
            ((BaseRope) this.ghost_object).g2.ghost = false;
        } else if (this.ghost_object instanceof Damper) {
            ((Damper) this.ghost_object).g1.ghost = false;
            ((Damper) this.ghost_object).g2.ghost = false;
        }
        if (this.ghost_object.get_position().y < -10000.0f || this.ghost_object.get_position().x < -10000.0f) {
            Settings.msg(L.get("dragbtn"));
            this.ghost_object.dispose(world);
        } else {
            this.om.add(this.ghost_object);
            this.ghost_object.reshape();
            this.ghost_object.pause();
            if (this.ghost_object instanceof Damper) {
                grab_object(((Damper) this.ghost_object).g1);
            } else if (!(this.ghost_object instanceof BaseRope)) {
                grab_object(this.ghost_object);
            } else {
                grab_object(((BaseRope) this.ghost_object).g1);
            }
            this.um.begin_step(1, this.ghost_object);
        }
        this.ghost_object = null;
    }

    private void create_hinge(ContactHandler.FixturePair fp, int type) {
        boolean z;
        boolean z2 = true;
        GrabableObject a = (GrabableObject) fp.a.getBody().getUserData();
        GrabableObject b = (GrabableObject) fp.b.getBody().getUserData();
        if (fp.same_layer) {
            type = 1;
        } else if (a instanceof BaseMotor) {
            attach_to_motor((BaseMotor) a, b);
            BaseMotor baseMotor = (BaseMotor) a;
            z = sandbox;
            baseMotor.fixed = z;
            connectanims.add(new ConnectAnim(0, fp.point.x, fp.point.y));
            return;
        } else if (b instanceof BaseMotor) {
            attach_to_motor((BaseMotor) b, a);
            BaseMotor baseMotor2 = (BaseMotor) b;
            if (!sandbox) {
                z2 = false;
            }
            baseMotor2.fixed = z2;
            connectanims.add(new ConnectAnim(0, fp.point.x, fp.point.y));
            return;
        }
        world.step(0.001f, 500, 500);
        this.um.begin_step(3, null);
        Hinge hinge = (Hinge) ObjectFactory.adapter.create(world, 3, 1);
        hinge.type = type;
        hinge.same_layer = fp.same_layer;
        if ((a instanceof DynamicMotor) || (a instanceof Panel) || (a instanceof Hub)) {
            hinge.rot_extra = ((Vector2) fp.a.getUserData()).x * 90.0f;
        } else if ((a instanceof RocketEngine) || (a instanceof DamperEnd)) {
            hinge.rot_extra = 180.0f;
        }
        hinge.setup((GrabableObject) fp.a.getBody().getUserData(), (GrabableObject) fp.b.getBody().getUserData(), fp.point);
        hinge.fixed = sandbox;
        this.hinges.add(hinge);
        this.um.add_hinge(hinge);
        this.um.commit_step();
        connectanims.add(new ConnectAnim(0, fp.point.x, fp.point.y));
    }

    private void attach_to_motor(BaseMotor h, GrabableObject b) {
        this.um.begin_step(3, null);
        if (b instanceof Plank) {
            Plank plank = (Plank) b;
            float aa = plank.body.getAngle();
            this._tmp.set((float) Math.cos((double) aa), (float) Math.sin((double) aa));
            this._tmp.mul(50.0f);
            this._tmp2.set(this._tmp);
            this._tmp3.set(this._tmp2).mul(-1.0f);
            this._tmp2.add(plank.get_position());
            this._tmp3.add(plank.get_position());
            float dist = Intersector.intersectSegmentCircleDisplace(this._tmp2, this._tmp3, h.get_position(), 1.0f, this._tmp);
            if (dist != Float.POSITIVE_INFINITY) {
                this._tmp.mul(-dist);
                b.translate(b.get_position().x - this._tmp.x, b.get_position().y - this._tmp.y);
            } else {
                Gdx.app.log("!!!ERROR!!!", "THIS ERROR SHOULD NEVER OCCUR !!!!!!!!!!!!!!!!!!!!!!!!");
            }
            h.attach(b);
            this.um.add_motor_hinge(h, b);
        } else if (b instanceof Wheel) {
            ((Wheel) b).translate(h.get_position().x, h.get_position().y);
            h.attach(b);
            this.um.add_motor_hinge(h, b);
        }
        world.step(1.0E-4f, 1, 1);
        this.um.commit_step();
    }

    @Override
    public boolean touchDragged(int x, int y, int p) {
        if (p >= this.widget.length) {
            return true;
        }
        this.fpos.set((float) x, (float) y);
        if (p < 3) {
            this._last_vec[p].set(this._touch_vec[p]);
            this._touch_vec[p].set((float) x, (float) (G.realheight - y));
            if (this.widget[p]) {
                this._tmpv.set(((float) x) * (((float) G.width) / ((float) G.realwidth)), ((float) y) * (((float) G.height) / ((float) G.realheight)));
                if (mode == MODE_PLAY) {
                    if (this.active_panel != null) {
                        this.active_panel.widgets.touch_drag((int) this._tmpv.x, (int) this._tmpv.y, p);
                    }
                } else if (sandbox && !this.rotating) {
                    this.widgets.touch_drag((int) this._tmpv.x, ((int) this._tmpv.y) - ((fix_bottombar ? 1 : 0) * 64));
                }
                if (this.mousejoint != null && p == 0) {
                    Intersector.intersectRayPlane(G.p_cam.getPickRay((float) x, (float) y), yaxis, this.iresult);
                    this.mousejoint.setTarget(this._tmp.set(this.iresult.x, this.iresult.y));
                }
                this.modified = true;
            } else if (p == 0) {
                Intersector.intersectRayPlane(G.p_cam.getPickRay((float) x, (float) y), yaxis, this.iresult);
                if (System.currentTimeMillis() - this.last_touch_time < 80) {
                    this.prevent_nail = false;
                    if (this.grabbed_object != null) {
                        this.grab_offs.set(this.iresult.x, this.iresult.y);
                        this.grab_offs.set(this._tmp);
                        this.grab_offs.sub(this.grabbed_object.get_position());
                    }
                } else {
                    this.prevent_nail = true;
                    if (!this.dragging_ghost) {
                        switch (mode) {
                            case 1:
                                if (!this.undo_begun && !this.dragging_ghost) {
                                    this.um.begin_step(2, this.grabbed_object);
                                    this.undo_begun = true;
                                }
                                if (this.grabbed_object != null && !this.rotating) {
                                    this._tmp.set(this.iresult.x, this.iresult.y);
                                    this._tmp.sub(this.grab_offs);
                                    if (this.num_touch_points == 2) {
                                        this._tmp.x = (float) Math.round(this._tmp.x);
                                        this._tmp.y = (float) Math.round(this._tmp.y);
                                    }
                                    this.grabbed_object.translate(this._tmp.x, this._tmp.y);
                                    this.modified = true;
                                    break;
                                }
                            case 3:
                            case 4:
                                if (this.rotating) {
                                    if (!this.undo_begun) {
                                        this.um.begin_step(2, this.grabbed_object);
                                        this.undo_begun = true;
                                        break;
                                    }
                                } else {
                                    this._tmp.set(this._last_touch).sub(this.fpos.x, this.fpos.y).mul(0.02f);
                                    this._tmp.mul(((float) camera_speed) / 40.0f);
                                    this._tmp.mul(1.0f + (2.0f * (this.camera_h.camera_pos.z / 70.0f)));
                                    if (System.currentTimeMillis() - this.last_zoom > 80) {
                                        this.camera_h.add_velocity(this._tmp.x, -this._tmp.y);
                                    }
                                    this._last_touch.set(this.fpos);
                                    break;
                                }
                                break;
                        }
                    } else {
                        if (this.pending_ghost && y < G.realheight - 64) {
                            this.pending_ghost = false;
                        }
                        if (!this.pending_ghost) {
                            this._tmp.set(this.iresult.x, this.iresult.y);
                            if (this.ghost_object instanceof BaseRope) {
                                ((BaseRope) this.ghost_object).g1.translate(this._tmp.x - 1.0f, this._tmp.y);
                                ((BaseRope) this.ghost_object).g2.translate(this._tmp.x + 2.0f, this._tmp.y);
                                ((BaseRope) this.ghost_object).save_state();
                            } else if (this.ghost_object instanceof Damper) {
                                ((Damper) this.ghost_object).g1.translate(this._tmp.x, (((Damper) this.ghost_object).size / 2.0f) + this._tmp.y);
                                ((Damper) this.ghost_object).g2.translate(this._tmp.x, this._tmp.y - (((Damper) this.ghost_object).size / 2.0f));
                                ((Damper) this.ghost_object).save_state();
                            } else {
                                this.ghost_object.translate(this._tmp.x, this._tmp.y);
                                this.ghost_object.save_state();
                            }
                            this.modified = true;
                        }
                    }
                    if (this.rotating && this.grabbed_object != null) {
                        Intersector.intersectRayPlane(G.p_cam.getPickRay((float) x, (float) y), yaxis, this.iresult);
                        float current_angle = this.grabbed_object.get_state().angle;
                        float wanted_angle = (float) Math.atan2((double) (this.iresult.y - this.rotate_point.y), (double) (this.iresult.x - this.rotate_point.x));
                        if (wanted_angle < 0.0f) {
                            wanted_angle = (float) (((double) wanted_angle) + 6.283185307179586d);
                        }
                        int current_rev = (int) (((double) current_angle) / 6.283185307179586d);
                        nangle[0] = (float) (((double) wanted_angle) + (((double) (((float) current_rev) + 2.0f)) * 6.283185307179586d));
                        nangle[1] = (float) (((double) wanted_angle) + (((double) (((float) current_rev) + 1.0f)) * 6.283185307179586d));
                        nangle[2] = (float) (((double) wanted_angle) + (((double) ((float) current_rev)) * 6.283185307179586d));
                        nangle[3] = (float) (((double) wanted_angle) + (((double) (((float) current_rev) - 1.0f)) * 6.283185307179586d));
                        nangle[4] = (float) (((double) wanted_angle) + (((double) (((float) current_rev) - 2.0f)) * 6.283185307179586d));
                        float new_angle = 0.0f;
                        float last_dist = 100000.0f;
                        for (int i = 0; i < 5; i++) {
                            float d = Math.abs(nangle[i] - current_angle);
                            if (d < last_dist) {
                                last_dist = d;
                                new_angle = nangle[i];
                            }
                        }
                        if (this.num_touch_points == 2) {
                            this.grabbed_object.rotate(new_angle);
                        } else {
                            this.grabbed_object.set_angle(new_angle);
                        }
                        this.modified = true;
                    }
                }
            }
        }
        return true;
    }

    private void handle_rotation() {
    }

    public boolean _touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    private Body raycast_find_body(float screenx, float screeny) {
        Ray r = G.p_cam.getPickRay(screenx, screeny);
        Intersector.intersectRayPlane(r, yaxis, this.iresult);
        this._tmp.set(this.iresult.x, this.iresult.y);
        Intersector.intersectRayPlane(r, this.yaxis2, this.iresult);
        this._tmp2.set(this.iresult.x, this.iresult.y);
        if (sandbox || mode == MODE_PLAY) {
            Intersector.intersectRayPlane(r, this.yaxis3, this.iresult);
            this._tmp3.set(this.iresult.x, this.iresult.y);
            this.query_result_body = null;
            this.query_input_pos = this._tmp3;
            this.query_input_layer = 3;
            world.QueryAABB(this.query_find_object_exact, this._tmp3.x - 1.5f, this._tmp3.y - 1.5f, this._tmp3.x + 1.5f, this._tmp3.y + 1.5f);
            if (this.query_result_body != null) {
                return this.query_result_body;
            }
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp2;
        this.query_input_layer = 2;
        world.QueryAABB(this.query_find_object_exact, this._tmp2.x - 1.5f, this._tmp2.y - 1.5f, this._tmp2.x + 1.5f, this._tmp2.y + 1.5f);
        if (this.query_result_body != null) {
            return this.query_result_body;
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp;
        this.query_input_layer = 1;
        world.QueryAABB(this.query_find_object_exact, this._tmp.x - 1.5f, this._tmp.y - 1.5f, this._tmp.x + 1.5f, this._tmp.y + 1.5f);
        if (this.query_result_body != null) {
            return this.query_result_body;
        }
        if (sandbox) {
            this.query_result_body = null;
            this.query_input_pos = this._tmp3;
            this.query_result_dist2 = 0.0f;
            this.query_input_layer = 3;
            world.QueryAABB(this.query_find_object, this._tmp3.x - 1.5f, this._tmp3.y - 1.5f, this._tmp3.x + 1.5f, this._tmp3.y + 1.5f);
            if (this.query_result_body != null) {
                return this.query_result_body;
            }
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp2;
        this.query_result_dist2 = 0.0f;
        this.query_input_layer = 2;
        world.QueryAABB(this.query_find_object, this._tmp2.x - 1.5f, this._tmp2.y - 1.5f, this._tmp2.x + 1.5f, this._tmp2.y + 1.5f);
        if (this.query_result_body != null) {
            return this.query_result_body;
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp;
        this.query_result_dist2 = 0.0f;
        this.query_input_layer = 1;
        world.QueryAABB(this.query_find_object, this._tmp.x - 1.5f, this._tmp.y - 1.5f, this._tmp.x + 1.5f, this._tmp.y + 1.5f);
        if (this.query_result_body != null) {
            return this.query_result_body;
        }
        return null;
    }

    private int query_aabb(QueryCallback callback, Vector2 pos, int layer) {
        return query_aabb(callback, pos, 1.5f, layer);
    }

    private int query_aabb(QueryCallback callback, Vector2 pos, float epsilon, int layer) {
        this.query_result = -1;
        this.query_result_body = null;
        this.query_result_dist2 = 0.0f;
        this.query_input_pos = pos;
        this.query_input_layer = layer;
        world.QueryAABB(callback, pos.x - 1.5f, pos.y - 1.5f, pos.x + 1.5f, 1.5f + pos.y);
        return this.query_result;
    }

    @Override
    public boolean scrolled(int arg0) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        this.fpos.set((float) x, (float) y);
        return true;
    }

    @Override
    public void on_widget_value_change(int id, float value) {
        float f;
        int i = 2;
        switch (id) {
            case 0:
                if (this.grabbed_object == null) {
                    return;
                }
                if (this.grabbed_object instanceof Bar) {
                    ((Bar) this.grabbed_object).size.x = (float) (2 << ((int) (1.0f + value)));
                    remove_potential_hinges(this.grabbed_object, true, true);
                    ((Bar) this.grabbed_object).reshape();
                    return;
                } else if (this.grabbed_object instanceof Wheel) {
                    Wheel wheel = (Wheel) this.grabbed_object;
                    if (value < -0.5f) {
                        f = 0.5f;
                    } else {
                        if (value < 0.5f) {
                            i = 1;
                        }
                        f = (float) i;
                    }
                    wheel.size = f;
                    remove_potential_hinges(this.grabbed_object, true, true);
                    ((Wheel) this.grabbed_object).reshape();
                    return;
                } else if (!(this.grabbed_object instanceof RopeEnd) && !(this.grabbed_object instanceof PanelCableEnd) && !(this.grabbed_object instanceof CableEnd)) {
                    return;
                } else {
                    if (this.grabbed_object instanceof CableEnd) {
                        ((CableEnd) this.grabbed_object).cable.set_size(((value + 1.0f) * 5.0f) + 5.0f);
                        return;
                    } else if (this.grabbed_object instanceof PanelCableEnd) {
                        ((PanelCableEnd) this.grabbed_object).cable.set_size(((value + 1.0f) * 5.0f) + 5.0f);
                        return;
                    } else if (this.grabbed_object instanceof RopeEnd) {
                        ((RopeEnd) this.grabbed_object).rope.set_size(((value + 1.0f) * 5.0f) + 5.0f);
                        return;
                    } else {
                        return;
                    }
                }
            case 1:
                if (this.grabbed_object instanceof Battery) {
                    ((Battery) this.grabbed_object).voltage = (value + 1.0f) / 2.0f;
                    return;
                }
                return;
            case 2:
                if (this.grabbed_object instanceof Battery) {
                    ((Battery) this.grabbed_object).current = (value + 1.0f) / 2.0f;
                    return;
                }
                return;
            case 3:
            case 4:
            case 5:
            default:
                return;
            case 6:
                if (this.grabbed_object instanceof Battery) {
                    switch ((int) value) {
                        case -1:
                            if (1 != ((Battery) this.grabbed_object).size) {
                                ((Battery) this.grabbed_object).resize(1);
                                return;
                            }
                            return;
                        case 0:
                            this.widget_sizeb.value = (float) (((((Battery) this.grabbed_object).size - 1) * 2) - 1);
                            return;
                        case 1:
                            if (2 != ((Battery) this.grabbed_object).size) {
                                ((Battery) this.grabbed_object).resize(2);
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                } else {
                    return;
                }
            case 7:
                if (this.grabbed_object instanceof RopeEnd) {
                    int v = (int) value;
                    Rope rope = ((RopeEnd) this.grabbed_object).rope;
                    switch (v) {
                        case -1:
                            rope.set_elastic(false);
                            return;
                        case 0:
                            this.widget_elasticity.value = (float) (rope.rubber ? 1 : -1);
                            return;
                        case 1:
                            rope.set_elastic(true);
                            return;
                        default:
                            return;
                    }
                } else {
                    return;
                }
            case 8:
                if (this.grabbed_object instanceof RocketEngine) {
                    ((RocketEngine) this.grabbed_object).thrust = (value + 1.0f) / 2.0f;
                    return;
                }
                return;
            case 9:
                if (this.grabbed_object instanceof DamperEnd) {
                    ((DamperEnd) this.grabbed_object).damper.speed = ((value + 1.0f) / 2.0f) * 200.0f;
                    ((DamperEnd) this.grabbed_object).damper.update_motor();
                    return;
                }
                return;
            case 10:
                if (this.grabbed_object instanceof DamperEnd) {
                    ((DamperEnd) this.grabbed_object).damper.force = ((value + 1.0f) / 2.0f) * 1000.0f;
                    ((DamperEnd) this.grabbed_object).damper.update_motor();
                    return;
                }
                return;
        }
    }

    public void begin_challenge_testing() {
        from_sandbox = true;
        sandbox = false;
        this.um.clear();
        create_metal_cache();
        for (GrabableObject b : this.om.all) {
            if (b.num_hinges > 0) {
                b.fixed_dynamic = true;
            }
        }
        if (this.grabbed_object != null) {
            release_object();
        }
        this.contact_handler.reset();
        for (GrabableObject grabableObject : this.om.all) {
            grabableObject.sandbox_save();
        }
    }

    public void end_challenge_testing() {
        if (from_sandbox) {
            this.um.clear();
            sandbox = true;
            from_sandbox = false;
            world.setContactFilter(this.falsefilter);
            this.contact_handler.reset();
            for (GrabableObject b : this.om.all) {
                if (b.num_hinges > 0) {
                    b.fixed_dynamic = false;
                }
            }
            if (this.grabbed_object != null) {
                release_object();
            }
            for (GrabableObject grabableObject : this.om.all) {
                grabableObject.set_active(false);
            }
            Iterator<Hinge> i = this.hinges.iterator();
            while (i.hasNext()) {
                Hinge h = i.next();
                if (!h.fixed) {
                    h.dispose(world);
                    i.remove();
                }
            }
            for (DynamicMotor h2 : this.om.layer0.dynamicmotors) {
                if (!h2.fixed) {
                    h2.detach();
                }
            }
            for (DynamicMotor h3 : this.om.layer0.dynamicmotors) {
                if (!h3.fixed) {
                    h3.detach();
                }
            }
            for (StaticMotor h4 : this.om.static_motors) {
                if (!h4.fixed) {
                    h4.detach();
                }
            }
            for (GrabableObject grabableObject : this.om.all) {
                grabableObject.set_active(false);
            }
            for (GrabableObject grabableObject : this.om.all) {
                grabableObject.sandbox_load();
            }
            for (GrabableObject grabableObject : this.om.all) {
                grabableObject.set_active(true);
            }
            world.setContactFilter(this.contact_handler);
        }
    }

    public void add_corner(MetalCorner a) {
        this.om.add(a);
    }

    public void remove_corner(MetalCorner a) {
        this.om.remove(a);
    }

    /* access modifiers changed from: private */
    public class SimpleContactHandler implements ContactListener, ContactFilter {
        private SimpleContactHandler() {
        }

        /* synthetic */ SimpleContactHandler(Game game, SimpleContactHandler simpleContactHandler) {
            this();
        }

        @Override
        public boolean shouldCollide(Fixture f1, Fixture f2) {
            Body A = f1.getBody();
            Body B = f2.getBody();
            if (A.getUserData() == null || B.getUserData() == null) {
                return false;
            }
            return f1.isSensor() || f2.isSensor() || ((BaseObject) A.getUserData()).layer == ((BaseObject) B.getUserData()).layer;
        }

        @Override
        public void beginContact(Contact c) {
            if (Game.mode == MODE_PLAY) {
                if ((c.getFixtureA().getBody().getUserData() instanceof Mine) && !c.getFixtureB().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 1.8f) {
                    ((Mine) c.getFixtureA().getBody().getUserData()).trigger(Game.world);
                }
                if ((c.getFixtureB().getBody().getUserData() instanceof Mine) && !c.getFixtureA().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 1.8f) {
                    ((Mine) c.getFixtureB().getBody().getUserData()).trigger(Game.world);
                }
                if ((c.getFixtureA().getUserData() instanceof Battery) && System.currentTimeMillis() - Game.this.game_start > 300 && !c.getFixtureB().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Battery) c.getFixtureA().getUserData()).toggle_onoff();
                }
                if ((c.getFixtureB().getUserData() instanceof Battery) && System.currentTimeMillis() - Game.this.game_start > 300 && !c.getFixtureA().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Battery) c.getFixtureB().getUserData()).toggle_onoff();
                }
                if ((c.getFixtureA().getUserData() instanceof Button) && System.currentTimeMillis() - Game.this.game_start > 50 && !c.getFixtureB().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Button) c.getFixtureA().getUserData()).activate();
                }
                if ((c.getFixtureB().getUserData() instanceof Button) && System.currentTimeMillis() - Game.this.game_start > 50 && !c.getFixtureA().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Button) c.getFixtureB().getUserData()).activate();
                }
                Fixture a = c.getFixtureA();
                Fixture b = c.getFixtureB();
                Object objA = a.getBody().getUserData();
                Object objB = b.getBody().getUserData();
                if (a.isSensor()) {
                    if ((objA instanceof Bucket) && ((objB instanceof Marble) || (objB instanceof ChristmasGift))) {
                        Game.this.num_balls_in_goal++;
                    }
                } else if (b.isSensor() && (objB instanceof Bucket) && ((objA instanceof Marble) || (objA instanceof ChristmasGift))) {
                    Game.this.num_balls_in_goal++;
                }
                if (objA instanceof ChristmasGift) {
                    SoundManager.handle_gift_hit((ChristmasGift) objA, (GrabableObject) objB);
                } else if (objA instanceof Marble) {
                    SoundManager.handle_marble_hit((Marble) objA, (GrabableObject) objB);
                } else if (objA instanceof MetalWheel) {
                    SoundManager.handle_metal_hit((MetalWheel) objA, (GrabableObject) objB);
                } else if ((objA instanceof Plank) || (objA instanceof Wheel)) {
                    SoundManager.handle_wood_hit((GrabableObject) objA, (GrabableObject) objB);
                } else if (objB instanceof Marble) {
                    SoundManager.handle_marble_hit((Marble) objB, (GrabableObject) objA);
                } else if (objB instanceof MetalWheel) {
                    SoundManager.handle_metal_hit((MetalWheel) objB, (GrabableObject) objA);
                } else if ((objB instanceof Plank) || (objB instanceof Wheel)) {
                    SoundManager.handle_wood_hit((GrabableObject) objB, (GrabableObject) objA);
                }
            }
        }

        @Override
        public void endContact(Contact c) {
            Fixture a = c.getFixtureA();
            Fixture b = c.getFixtureB();
            if (a != null && b != null && a.getBody() != null && b.getBody() != null) {
                if (a.getBody().getUserData() instanceof Marble) {
                    SoundManager.stop_marble_roll((Marble) a.getBody().getUserData());
                }
                if (b.getBody().getUserData() instanceof Marble) {
                    SoundManager.stop_marble_roll((Marble) b.getBody().getUserData());
                }
                Object objA = a.getBody().getUserData();
                Object objB = b.getBody().getUserData();
                if (a.isSensor()) {
                    if (!(objA instanceof Bucket)) {
                        return;
                    }
                    if ((objB instanceof Marble) || (objB instanceof ChristmasGift)) {
                        Game game = Game.this;
                        game.num_balls_in_goal--;
                    }
                } else if (b.isSensor() && (objB instanceof Bucket)) {
                    if ((objA instanceof Marble) || (objA instanceof ChristmasGift)) {
                        Game game2 = Game.this;
                        game2.num_balls_in_goal--;
                    }
                }
            }
        }

        @Override
        public void postSolve(Contact arg0, ContactImpulse arg1) {
        }

        @Override
        public void preSolve(Contact arg0, Manifold arg1) {
        }
    }

    public static class ConnectAnim {
        int dir;
        float size = 0.0f;
        float x;
        float y;

        public ConnectAnim(int dir2, float x2, float y2) {
            if (dir2 == 0) {
                this.size = 1.0f;
            } else {
                this.size = 0.0f;
            }
            this.x = x2;
            this.y = y2;
            this.dir = dir2;
        }

        public boolean render() {
            if (this.dir == 0) {
                this.size -= G.delta * 4.0f;
                if (this.size <= 0.0f) {
                    return false;
                }
            } else {
                this.size += G.delta * 6.0f;
                if (this.size >= 1.0f) {
                    return false;
                }
            }
            G.gl.glPushMatrix();
            G.gl.glTranslatef(this.x, this.y, 1.0f);
            G.gl.glScalef(this.size * 2.0f, this.size * 2.0f, 1.0f);
            MiscRenderer.draw_colored_circle();
            G.gl.glPopMatrix();
            return true;
        }
    }

    /* access modifiers changed from: private */
    public class SimulationThread extends Thread {
        boolean modified;
        private long t_accum;
        private long t_delta;
        private long t_last;
        private long t_now;
        boolean terminate;

        private SimulationThread() {
            this.terminate = false;
            this.modified = false;
        }

        /* synthetic */ SimulationThread(Game game, SimulationThread simulationThread) {
            this();
        }

        public int swap_state_buffers() {
            synchronized (this) {
                if (this.modified) {
                    this.modified = false;
                }
            }
            return 0;
        }

        public void terminate() {
            synchronized (this) {
                this.terminate = true;
            }
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c6, code lost:
            r4 = java.lang.Math.max(0L, 8 - ((java.lang.System.nanoTime() - r14.t_now) / 1000000));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x00de, code lost:
            if (r4 <= 0) goto L_0x0013;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
            java.lang.Thread.sleep(r4);
         */
        public void run() {
            Gdx.app.log("SIMULATION THREAD", "STARTED");
            this.t_last = System.nanoTime();
            this.t_accum = 0;
            while (true) {
                boolean i = false;
                this.t_now = System.nanoTime();
                if (this.t_accum > 100000000) {
                    this.t_accum = 24000000;
                }
                this.t_delta = (this.t_now - this.t_last) + this.t_accum;
                this.t_last = this.t_now;
                if (this.t_delta > 100000000) {
                    this.t_delta = 48000000;
                }
                if (this.t_delta >= 8000000) {
                    i = true;
                    do {
                        int iterations = Game.physics_stability == 1 ? 10 : Game.physics_stability == 0 ? 1 : 64;
                        Game.world.step(0.011f, iterations, iterations);
                        Iterator<Hinge> it = Game.this.hinges.iterator();
                        while (it.hasNext()) {
                            it.next().tick();
                        }
                        this.t_delta -= 8000000;
                    } while (this.t_delta >= 8000000);
                }
                this.t_accum = this.t_delta;
                synchronized (this) {
                    if (i) {
                        Iterator<GrabableObject> it2 = Game.this.om.all.iterator();
                        while (it2.hasNext()) {
                            it2.next().save_state();
                        }
                        this.modified = true;
                    }
                    if (this.terminate) {
                        Gdx.app.log("SIMULATION THREAD", "TERMINATED");
                        return;
                    }
                }
            }
        }
    }

    public void set_bg(int x) {
        if (this.background_n != x) {
            if (bgtex != null) {
                TextureFactory.unload(bgtex);
            }
            bgtex = TextureFactory.load_mipmapped("data/bg" + Math.max(x, 0) + ".jpg");

            this.background_n = x;
            if (this.level != null) {
                this.level.background = this.background_n;
            }
        }
    }
}
