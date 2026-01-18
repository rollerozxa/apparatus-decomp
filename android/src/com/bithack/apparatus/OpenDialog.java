package com.bithack.apparatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class OpenDialog {
    ApparatusApplication app;
    Dialog dialog;
    final CharSequence[] filelist;

    public OpenDialog(final ApparatusApplication app) {
        this.app = app;
        AlertDialog.Builder builder = new AlertDialog.Builder(app);
        ArrayList<String> filenames = new ArrayList<>();
        FileHandle h = Gdx.files.getFileHandle("/ApparatusLevels", Files.FileType.External);
        FileHandle[] files = h.list();
        for (FileHandle file : files) {
            if (!file.isDirectory() && ((file.name().length() < 4 || !file.name().substring(0, 4).equals(".lvl")) && ((file.name().length() < 9 || !file.name().substring(0, 9).equals(".autosave")) && file.extension().equals("jar")))) {
                filenames.add(file.nameWithoutExtension());
            }
        }
        this.filelist = new CharSequence[filenames.size()];
        for (int x = 0; x < filenames.size(); x++) {
            this.filelist[x] = filenames.get(x);
        }
        builder.setTitle(L.get("open_level"));
        builder.setItems(this.filelist, new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.OpenDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface d, int which) {
                final CharSequence levelname = OpenDialog.this.filelist[which];
                app.run_on_gl_thread(new Runnable() { // from class: com.bithack.apparatus.OpenDialog.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Game.sandbox = true;
                        ApparatusApp.game.open(levelname.toString());
                        if (ApparatusApp.current != ApparatusApp.game) {
                            ApparatusApp.instance.play();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton(L.get("cancel"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.OpenDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.dialog = builder.create();
    }

    public Dialog get_dialog() {
        return this.dialog;
    }
}
