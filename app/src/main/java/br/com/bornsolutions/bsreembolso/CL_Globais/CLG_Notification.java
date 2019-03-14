package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import br.com.bornsolutions.bsreembolso.R;

/**
 * Created by guilherme on 26/04/2016.
 */
public class CLG_Notification  {

    private static final String TAG = "BS Reembolso";

    public static void create(Context context, int id, Intent intent, String contentTitle, String contentText){

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //INTENT PARA DISPARAR O BROADCAST
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //CRIA A NOTIFICATION
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(p)         //INTENT CHAMADA AO CLICAR
                .setContentTitle(contentTitle) //TITULO
                .setContentText(contentText) //TEXTO
                .setSmallIcon(R.mipmap.app_icon) //ICONE
                .setAutoCancel(true); //REMOVE A NOTIFICAÇÃO AO CLICAR

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.app_icon_transparent);
            builder.setColor(context.getResources().getColor(R.color.cor_logotipo));
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher_bs);
        }



        //DISPARA A NOTIFICACAO
        Notification n = builder.build();
        manager.notify(id, n);

    }

}
