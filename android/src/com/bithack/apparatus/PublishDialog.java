package com.bithack.apparatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class PublishDialog {
    final ApparatusApplication activity;
    Dialog dialog;
    final View view;

    public PublishDialog(ApparatusApplication app) {
        this.activity = app;
        AlertDialog.Builder builder = new AlertDialog.Builder(app);
        this.view = LayoutInflater.from(app).inflate(R.layout.publishdialog, (ViewGroup) null);
        builder.setTitle(L.get("publish_community_level"));
        builder.setView(this.view);
        builder.setNeutralButton(L.get("publish"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.PublishDialog.1
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PublishDialog.this.new PublishTask().execute(((TextView) PublishDialog.this.view.findViewById(R.id.levelname)).getText().toString(), ((TextView) PublishDialog.this.view.findViewById(R.id.description)).getText().toString(), ((TextView) PublishDialog.this.view.findViewById(R.id.keywords)).getText().toString());
            }
        });
        builder.setNegativeButton(L.get("cancel"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.PublishDialog.2
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.dialog = builder.create();
    }

    public void prepare() {
        EditText name = (EditText) this.view.findViewById(R.id.levelname);
        EditText descr = (EditText) this.view.findViewById(R.id.description);
        EditText tags = (EditText) this.view.findViewById(R.id.keywords);
        name.setText("");
        descr.setText("");
        tags.setText("");
        Game game = ApparatusApp.game;
        if (!game.level.name.equals("")) {
            name.setText(game.level.name);
            descr.setText(game.level.description);
            tags.setText(game.level.tags);
        } else if (ApparatusApp.game.level_filename != null) {
            name.setText(ApparatusApp.game.level_filename);
        }
        if (!game.level.community_id.equals("")) {
            this.dialog.setTitle(L.get("publish_update"));
        } else {
            this.dialog.setTitle(L.get("publish_community_level"));
        }
    }

    public Dialog get_dialog() {
        return this.dialog;
    }

    public static class HttpUtils {
        public static HttpClient getNewHttpClient() throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory sf = new EasySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, "UTF-8");
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));
                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                e.printStackTrace();
                return new DefaultHttpClient();
            }
        }
    }

    public static class EasySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext;

        public EasySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, KeyStoreException {
            super(truststore);
            this.sslContext = SSLContext.getInstance("TLS");
            TrustManager tm = new X509TrustManager() { // from class: com.bithack.apparatus.PublishDialog.EasySSLSocketFactory.1
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            this.sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return this.sslContext.getSocketFactory().createSocket();
        }
    }

    protected class PublishTask extends AsyncTask<String, String, String> {
        public int status = 0;
        public byte[] level_data = null;

        protected PublishTask() {
        }

        @Override
        protected String doInBackground(final String... params) {
            try {
                PublishDialog.this.activity.run_on_gl_thread(() -> {
                    try {
                        Game game = ApparatusApp.game;
                        game.prepare_save();
                        game.level.name = params[0];
                        game.level.description = params[1];
                        game.level.tags = params[2];

                        File tempFile = Settings.get_tmp_file();
                        game.level.save_jar(tempFile);

                        long fileLength = tempFile.length();
                        if (fileLength > Integer.MAX_VALUE) {
                            throw new IOException("File too large to process.");
                        }

                        byte[] data = new byte[(int) fileLength];
                        try (InputStream inputStream = new FileInputStream(tempFile)) {
                            int bytesRead;
                            int offset = 0;
                            while (offset < data.length && (bytesRead = inputStream.read(data, offset, data.length - offset)) >= 0) {
                                offset += bytesRead;
                            }
                        }

                        tempFile.delete();
                        game.save();

                        synchronized (this) {
                            this.level_data = data;
                            this.status = 1;
                        }
                    } catch (Exception e) {
                        synchronized (this) {
                            this.status = 2;
                        }
                    }
                });

                int retries = 0;
                while (retries < 1000) {
                    synchronized (this) {
                        if (this.status != 0) {
                            break;
                        }
                    }
                    Thread.sleep(200);
                    retries++;
                }

                if (this.status != 1) {
                    return "Interrupted";
                }

                HttpClient httpClient = HttpUtils.getNewHttpClient();
                HttpPost request = new HttpPost("http://apparatus-web.voxelmanip.se/internal/upload.php?t=" + Settings.get("community-token"));
                ByteArrayEntity entity = new ByteArrayEntity(this.level_data);
                entity.setContentType("binary/octet-stream");
                request.setEntity(entity);

                try {
                    HttpResponse response = httpClient.execute(request);
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                        StringBuilder resultBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            resultBuilder.append(line);
                        }
                        return resultBuilder.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error occurred during publishing.";
            }
        }

        @Override
        protected void onPreExecute() {
            PublishDialog.this.activity.dismissDialog(2);
            PublishDialog.this.activity.showDialog(3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override
        public void onPostExecute(String result) {
            String result2 = result.trim();
            boolean error = false;
            if (result2.length() > 0 && result2.substring(0, 3).equals("OK:")) {
                final String token = result2.substring(3).trim();
                PublishDialog.this.activity.run_on_gl_thread(new Runnable() { // from class: com.bithack.apparatus.PublishDialog.PublishTask.2
                    @Override
                    public void run() {
                        ApparatusApp.game.level.community_id = token;
                        ApparatusApp.game.save();
                    }
                });
                Toast.makeText(PublishDialog.this.activity, L.get("published_successfully"), 0).show();
            } else {
                error = true;
            }
            PublishDialog.this.activity.dismissDialog(3);
            if (error) {
                if (result2.equals("UNAUTH")) {
                    Toast.makeText(PublishDialog.this.activity, L.get("log_in_to_publish"), 0).show();
                    PublishDialog.this.activity.showDialog(11);
                } else if (result2.equals("INVALID3")) {
                    Toast.makeText(PublishDialog.this.activity, L.get("name_required"), 0).show();
                    PublishDialog.this.activity.showDialog(2);
                } else {
                    Toast.makeText(PublishDialog.this.activity, String.valueOf(L.get("server error")) + "(" + result2 + ")", 0).show();
                    PublishDialog.this.activity.showDialog(2);
                }
            }
        }
    }
}
