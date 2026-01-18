package com.bithack.apparatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.Gdx;

/* loaded from: classes.dex */
public class ApparatusPlay extends Activity {
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, (Class<?>) ApparatusApplication.class);
        i.setFlags(604045312);
        Gdx.app.log("intent 1", new StringBuilder().append(getIntent()).toString());
        Gdx.app.log("intent", getIntent().getScheme());
        Gdx.app.log("", new StringBuilder().append(getIntent().getData()).toString());
        i.putExtra("id", 64);
        startActivity(i);
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
    }

    @Override // android.app.Activity
    public void onDestroy() {
        Settings.save();
        super.onDestroy();
    }
}
