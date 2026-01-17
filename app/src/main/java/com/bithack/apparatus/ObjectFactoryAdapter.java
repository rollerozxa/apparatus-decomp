package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.ObjectFactory;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.Battery;
import com.bithack.apparatus.objects.Bucket;
import com.bithack.apparatus.objects.Button;
import com.bithack.apparatus.objects.Cable;
import com.bithack.apparatus.objects.ChristmasGift;
import com.bithack.apparatus.objects.Damper;
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
import com.bithack.apparatus.objects.Plank;
import com.bithack.apparatus.objects.RocketEngine;
import com.bithack.apparatus.objects.Rope;
import com.bithack.apparatus.objects.StaticMotor;
import com.bithack.apparatus.objects.Weight;
import com.bithack.apparatus.objects.Wheel;

/* loaded from: classes.dex */
public class ObjectFactoryAdapter extends ObjectFactory.Adapter {
    static final int CATEGORY_CUSTOM = 0;
    static final int CATEGORY_DYNAMIC = 2;
    static final int CATEGORY_MECHANICS = 3;
    static final int CATEGORY_STATIC = 1;
    public static final String[] category_names = {"Custom", "Static", "Dynamic", "Mechanics"};
    public final int num_categories = category_names.length;
    public final String[][] object_names = {new String[0], new String[]{"Metal Bar", "Weight", "Bucket", "Metal Wheel"}, new String[]{"Plank", "Marble", "Small Plank", "Mini Plank", "Big Wheel", "Rope", "Cable", "Dynamic Motor", "Metal Corner", "Panel", "PanelCable"}, new String[]{"Static Hinge", "Hinge"}};

    @Override // com.bithack.apparatus.ObjectFactory.Adapter
    public BaseObject create(World world, int g, int c) {
        BaseObject o;
        Gdx.app.log("ObjectFactoryAdapter", "Creating object group " + g + " child " + c);
        switch (g) {
            case 1:
                switch (c) {
                    case 0:
                        o = new MetalBar(world);
                        break;
                    case 1:
                        o = new Weight(world);
                        break;
                    case 2:
                        o = new Bucket(world);
                        break;
                    case 3:
                        o = new MetalWheel(world, 1.0f);
                        break;
                    default:
                        o = null;
                        break;
                }
                break;
            case 2:
                switch (c) {
                    case 0:
                        o = new Plank(world, 4.0f);
                        break;
                    case 1:
                        o = new Marble(world);
                        break;
                    case 2:
                        o = new Plank(world, 2.0f);
                        break;
                    case 3:
                        o = new Plank(world, 1.0f);
                        break;
                    case 4:
                        o = new Wheel(world, 1.0f);
                        break;
                    case 5:
                        o = new Rope(world);
                        break;
                    case 6:
                        o = new Cable(world);
                        break;
                    case 7:
                        o = new Battery(world);
                        break;
                    case 8:
                        o = new DynamicMotor(world);
                        break;
                    case 9:
                        o = new MetalCorner(world);
                        break;
                    case 10:
                        o = new Panel(world);
                        break;
                    case 11:
                        o = new PanelCable(world);
                        break;
                    case 12:
                        o = new Knob(world, 1.0f);
                        break;
                    case 13:
                        o = new Mine(world);
                        break;
                    case 14:
                        o = new RocketEngine(world);
                        break;
                    case 15:
                        o = new Hub(world);
                        break;
                    case 16:
                        o = new Button(world);
                        break;
                    case 17:
                        o = new ChristmasGift(world);
                        break;
                    case 18:
                        o = new Damper(world, 1.0f);
                        break;
                    default:
                        o = null;
                        break;
                }
                break;
            case 3:
                switch (c) {
                    case 0:
                        o = new StaticMotor(world);
                        break;
                    case 1:
                        o = new Hinge(world);
                        break;
                    default:
                        o = null;
                        break;
                }
                break;
            default:
                o = null;
                break;
        }
        if (o != null) {
            o.__child_id = c;
            o.__group_id = g;
            o.__unique_id = Game.id_counter;
            Game.id_counter++;
        }
        Gdx.app.log("ObjectFactoryAdapter", "Created object: " + o);
        return o;
    }

    @Override // com.bithack.apparatus.ObjectFactory.Adapter
    public GrabableObject wrap(GrabableObject o) {
        if (o instanceof MetalCorner) {
            o.__child_id = 9;
            o.__group_id = 2;
            o.__unique_id = Game.id_counter;
            Game.id_counter++;
        }
        return o;
    }
}
