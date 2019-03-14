package br.com.bornsolutions.bsreembolso.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_GCM;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Util.Constantes;
import br.com.bornsolutions.bsreembolso.WebService.WS_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.WebService.WS_GCM;

public class RegistrarGCM extends BaseActivity {

    private static final String TAG = "BSReembolso";
    TextView txtGcmRegistro;
    WS_GCM _gWS_GCM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_gcm);

        //CRIA A ACTION BAR COM O BOTÃO VOLTA
        getSupportActionBar().setTitle("GCM Google");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtGcmRegistro = (TextView) findViewById(R.id.txtGcmRegistro);

        //VERIFICA SE O PLAY SERVICES ESTÁ INSTALADO
        Boolean _bok = prcCheckPlaySeervices();
        if(_bok)
        {
            CLG_GCM CL_GCM = new CLG_GCM();
            String _sregID = CL_GCM.getRegistrationID(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        //CHAMADO AO CLICAR NA NOTIFICAÇÃO SE A ACTIVITY JA ESTAVA ABERTA
        //DEVIDO A CONFIGURAÇÃO DO ANRDOI LOUCHE MODE SINGLE TOP
        //LE A MSG DA NOTIFICAÇÃO
        String msg = intent.getStringExtra("msg");
        setText(msg);
    }

    private void setText(final String s)
    {
        if(s != null)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView text = (TextView) findViewById(R.id.txtGcmRegistro);
                    text.setText(s);
                    Log.d(TAG,s);
                }
            });
        }
    }

    public void prcClickRegistrar(View view)
    {
        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WS_GCM Thread_RegistrarGCM = new WS_GCM(this, this, true, prpUsuario_Logado, prpUsuario_ID);
            Thread_RegistrarGCM.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }
    }

    public void prcClickCancelar(View view)
    {
        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WS_GCM Thread_RegistrarGCM = new WS_GCM(this, this, false, prpUsuario_Logado, prpUsuario_ID);
            Thread_RegistrarGCM.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }
    }

    private boolean prcCheckPlaySeervices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                int PLAY_SERVICES_RESOLUTION_REQUEST= 9000;
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(this, "Esse dispositivo não suporte google play services", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private Context getContext()
    {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
