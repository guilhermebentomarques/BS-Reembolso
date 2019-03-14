package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOError;
import java.io.IOException;

/**
 * Created by guilherme on 25/08/2016.
 */
public class CLG_GCM {

    private static final String tag = "gcm";
    public static final String PROPERTY_REG_ID = "registration_id";

    //PREFERENCIAS PARA SALVAR O REGISTRATION ID
    private static SharedPreferences getGCMPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("bsreembolso_gcm", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    //RETORNA REGISTRO ID SALVO NA PREFS
    public static String getRegistrationID(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationid = prefs.getString(PROPERTY_REG_ID, "");
        if(registrationid == null || registrationid.trim().length() == 0)
        {
            return null;
        }
        return registrationid;
    }

    //SALVA REGISTRATION NA PREFS
    private static void saveRegistrationID(Context context, String registrationID){
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, registrationID);
        editor.commit();
    }

    //FAZER REGISTRO NA GCM
    public static String register(Context context, String projectNumber){
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        try
        {
            Log.d(tag, "GCM.registrar():" + projectNumber);
            String registrationID = gcm.register(projectNumber);
            if(registrationID != null)
            {
                //SALVA NAS PREFS
                saveRegistrationID(context,registrationID);
            }
            Log.d(tag, "GCM.registrar() OK, ID: " + registrationID);
            return  registrationID;
        }
        catch (IOException e)
        {
            Log.e(tag,"GCM Erro: " + e.getMessage(),e);
        }
        return null;
    }

    //FAZER REGISTRO NA GCM
    public static void unregister(Context context, String projectNumber){
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        try
        {
            gcm.unregister();
            saveRegistrationID(context,null);
            Log.d(tag,"GCM Cancelado com sucess!!");
        }
        catch (IOException e)
        {
            Log.e(tag,"GCM Erro ao remover registro: " + e.getMessage(),e);
        }
    }
}
