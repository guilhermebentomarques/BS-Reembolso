package br.com.bornsolutions.bsreembolso.Service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import br.com.bornsolutions.bsreembolso.Activity.MainActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_GCM_BroadcastReceiver;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Notification;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 25/08/2016.
 */
public class GcmIntentService extends IntentService {

    public static final  String TAG = "gcm";

    public GcmIntentService(){
        super(Constantes.PROJECT_NUMBER);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.i(TAG, "GCMItentService onHandleIntent" + extras);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        //VERIFICA O TIPO DE MENSAGEM
        String MessageType = gcm.getMessageType(intent);
        if(!extras.isEmpty()){
            //O EXTRAS.ISEMPTY PRECISA SER CHAMADO PARA LER O BUNDLE
            //VERIFICA O TIPO DE MENSAGEM, NO FUTURO PODERÃO TER MAIS TIPO
            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(MessageType)){
                //ERRO
                onError(extras);
            }
            else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(MessageType))
            {
                //MENSAGEM NORMAL, LER PARAMETRO MSG
                //ENVIADO PELO SERVIDOR
                onMessage(extras);
            }

        }
        //LIBERA WAKE LOCK QUE FOI BLOQUEADO PELA CLASSE
        CLG_GCM_BroadcastReceiver.completeWakefulIntent(intent);
    }

    private void onError(Bundle extras)
    {
        Log.d(TAG, "Erro" + extras.toString());
    }

    private void onMessage(Bundle extras)
    {
        //LÊ A MENSAGEM E MOSTRA NOTIFICACAO
        String _sMensagem = extras.getString("message");
        String _sTitutlo = extras.getString("contentTitle");
        Log.d(TAG, "Msg" + extras.toString());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg",_sMensagem);
        CLG_Notification CL_Notification = new CLG_Notification();
        CL_Notification.create(this,1,intent, _sTitutlo,_sMensagem);
    }
}
