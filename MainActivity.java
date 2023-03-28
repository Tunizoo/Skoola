package com.example.skoola11;

import static com.example.skoola11.MainApp.serviceOn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private TimerTask timerTask;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleService();
            }
        });

        webView=(WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://miskacademy.tn/skoola");
        webView.clearCache(true);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    protected TimerTask newTask() {
        return new TimerTask() {
            @Override
            public void run() {
                fetchNotifs();
            }
        };
    }

    public void stopService() {
        while(serviceOn>0) {
            Intent serviceIntent = new Intent(this, NotificationService.class);
            stopService(serviceIntent);
            serviceOn--;
        }
        //fab.setImageDrawable(Drawable.createFromPath("res/drawable/ic_baseline_assistant_photo_24.xml"));
        fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_assistant_photo_24));
    }

    public void toggleService() {
        Timer timer=new Timer();
        if (serviceOn==0) {
            timerTask=newTask();
            timer.scheduleAtFixedRate(timerTask, 0, 150000);
        } else {
            timerTask.cancel();
            stopService();
        }
    }

    public void startService(String response_row) {

        serviceOn++;
        Toast.makeText(MainActivity.this,String.valueOf(serviceOn),Toast.LENGTH_LONG);
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("response_row", response_row);

        ContextCompat.startForegroundService(this, serviceIntent);

        fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_back_hand_24));
   }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private void fetchNotifs() {

        String url = "https://miskacademy.tn/skoola/fetch_notifs_mobile.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(
                                response.indexOf("L'utilisateur courant")>=0
                                        || response.contains("section de l'élève")
                                        || response.contains("Échec")
                        ){
                            Toast.makeText(MainActivity.this, response,Toast.LENGTH_LONG);
                        }
                        else {
                            try {
                                JSONArray resp_data = new JSONArray(response);
                                String[] output=new String[resp_data.length()];
                                for (int i = 0; i < resp_data.length(); i++) {
                                    JSONObject row=resp_data.getJSONObject(i);
                                    output[i]=row.getString("type")+row.getString("new_updt")+": "+row.getString("date");
                                    startService(output[i]);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            public HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                //               params.put("curr_user", String.valueOf(MainActivity.currentUser));
                return params;
            }
        };
        requestQueue.add(request);
    }

}
