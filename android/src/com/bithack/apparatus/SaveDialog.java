package com.bithack.apparatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class SaveDialog {
    ApparatusApplication app;
    Dialog dialog;
    final EditText textfield;

    public SaveDialog(final ApparatusApplication app) {
        this.app = app;
        AlertDialog.Builder builder = new AlertDialog.Builder(app);
        View view = LayoutInflater.from(app).inflate(R.layout.savedialog, (ViewGroup) null);
        builder.setTitle(L.get("save_level"));
        builder.setView(view);
        this.textfield = (EditText) view.findViewById(R.id.filename);
        builder.setPositiveButton(L.get("save"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.SaveDialog.1
            @Override
            public void onClick(DialogInterface d, int which) {
                final String filename = SaveDialog.this.textfield.getText().toString();
                if (filename.length() <= 1) {
                    Toast.makeText(app, L.get("filename_too_short"), 0).show();
                    ApparatusApplication apparatusApplication = app;
                    final ApparatusApplication apparatusApplication2 = app;
                    apparatusApplication.run_on_gl_thread(new Runnable() { // from class: com.bithack.apparatus.SaveDialog.1.1
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100L);
                            } catch (Exception e) {
                            }
                            apparatusApplication2.runOnUiThread(new Runnable() { // from class: com.bithack.apparatus.SaveDialog.1.1.1
                                @Override
                                public void run() {
                                    ApparatusApp.backend.open_save_dialog();
                                }
                            });
                        }
                    });
                    return;
                }
                app.run_on_gl_thread(new Runnable() { // from class: com.bithack.apparatus.SaveDialog.1.2
                    @Override
                    public void run() {
                        ApparatusApp.game.level_filename = filename;
                        ApparatusApp.game.save();
                        Settings.msg("Level saved!");
                    }
                });
            }
        });
        builder.setNegativeButton(L.get("cancel"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.SaveDialog.2
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.dialog = builder.create();
    }

    public void prepare() {
        if (ApparatusApp.game.level_filename != null) {
            this.textfield.setText(ApparatusApp.game.level_filename);
        } else {
            this.textfield.setText("");
        }
    }

    public Dialog get_dialog() {
        return this.dialog;
    }
}
