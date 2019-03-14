package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import br.com.bornsolutions.bsreembolso.Service.GcmIntentService;

/**
 * Created by guilherme on 25/08/2016.
 */
public class CLG_GCM_BroadcastReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "gcm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "GCM Broadcast onReceive: " + intent.getExtras());
        //INICIA SERVICE COM WAKE LOCK
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
