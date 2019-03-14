package br.com.bornsolutions.bsreembolso.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.prefs.Preferences;

import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Login;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.WebService.WS_GCM;
import br.com.bornsolutions.bsreembolso.WebService.WebService_Login;

public class LoginActivity extends BaseActivity {

    Button btnLogin;
    private ProgressDialog progress;
    EditText edtLogin, edtSenha;
    String _strLogin, _strSenha, _strID;
    Boolean _bResultado = false;
    TextView txtError;

    //OBJETO PARA SALVAR DADOS DA APLICAÇÃO, COMO COOKIES DA WEB OU SESSÕES
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        try {

            CL_Preferences Preferences = new CL_Preferences();
            preferences = Preferences.PrcGet_SharedPreferences_Login(LoginActivity.this, "CAD_USUARIO");

            prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
            prpUsuario_ID = preferences.getString("CU_ID", null);

            if (prpUsuario_ID != null) {

                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent intent = new Intent(this, MainActivity.class);
                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtras(params);
                startActivity(i);
                finish(); //REMOVE ACTIVITY DA PILHA DO ANDROID.
            }
        }
        catch (Exception ex)
        {

        }

        //CLICK NO LOGIN
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _strLogin = edtLogin.getText().toString();
                _strSenha = edtSenha.getText().toString();

                CL_Login CL_Login = new CL_Login();
                Boolean _bRetorno = CL_Login.prcValidarLogin(_strLogin, edtLogin, _strSenha, edtSenha);

                if (_bRetorno) {
                    try {
                        //NO WEB SERVICE JÁ VALIDA TUDO
                        WebService_Login WebServiceLogin = new WebService_Login(LoginActivity.this, LoginActivity.this, edtLogin, edtSenha);
                        WebServiceLogin.execute();

                    } catch (Exception ex) {
                        String _sErro = ex.getMessage();
                    }
                }


            }
        });
    }
}
