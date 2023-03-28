package com.example.skoola11;

import static com.example.skoola11.MainApp.manager;
import static com.example.skoola11.MainApp.serviceOn;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import com.example.skoola11.MainApp.*;
public class NotificationService extends Service {

    String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";

    @Override
    public void onCreate() {
        //createNewNotification("Service démarré.");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String row = intent.getStringExtra("response_row");
        createNewNotification(row);
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void createNewNotification(String message){
        String GROUP = "com.android.example.GROUP";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification;
        notification = new NotificationCompat.Builder(NotificationService.this, MainApp.CHANNEL_ID)
                .setGroup(GROUP)
                .setContentTitle("Notification of Skoola")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.btn_star_big_on)
                .setContentIntent(pendingIntent)
//                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)
//                .setOngoing(false)
                .build();
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH);
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(serviceOn, notification);
        startForeground(serviceOn, notification);
    }
    /*              JSONObject resp_row = JSONObject(resp_data[i]);
              JSONObject Label=resp_row.getJSONObject( "label");
              if(Label!=null) output+="Label:"+Label.getString();
              JSONObject Description=resp_row.getJSONObject( "description");
              if(Label!=null) output+="Description:"+Description.getString();
              JSONObject Created_At=resp_row.getJSONObject( "created_at");
              if(Created_At!=null) output+="Créé(e) le:"+Created_At.getString();
              JSONObject Updated_At=resp_row.getJSONObject( "updated_at");
              if(Updated_At!=null) output+="Modifié(e) le:"+Updated_At.getString();
              JSONObject Exam_Center=resp_row.getJSONObject( "exam_center);
              if(Exam_Center!=null) output+="Centre d'examen:"+Exam_Center.getString();
              JSONObject Start_Date=resp_row.getJSONObject( "start_date);
              if(Start_Date!=null) output+="Date de début:"+Start_Date.getString();
              JSONObject End_Date=resp_row.getJSONObject( "end_date);
              if(End_Date!=null) output+="Date de fin:"+End_Date.getString();
              JSONObject Invoice_Number=resp_row.getJSONObject( "invoice_number);
              if(Invoice_Number!=null) output+="Numéro de facture:"+Invoice_Number.getString();
              JSONObject Invoice_Total=resp_row.getJSONObject( "invoice_amount_total");
              if(Invoice_Total!=null) output+="Montant total de la facture:"+Invoice_Total.getDouble();
              JSONObject Status=resp_row.getJSONObject( "status");
              if(Status!=null) output+="État:"+Status.getDouble();
              JSONObject Status=resp_row.getJSONObject( "status");
              if(Status!=null) output+="État:"+Status.getDouble();
                  if (i == "added_by") t = "AjoutÃ© par:";
                  if (i == "homework_date") t = "Date du homework:";
                  if (i == "subject") t = "Sujet:";
                  if (i == "title") t = "Titre:";
    */
}