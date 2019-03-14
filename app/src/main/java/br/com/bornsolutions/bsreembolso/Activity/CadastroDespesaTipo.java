package br.com.bornsolutions.bsreembolso.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.R;

public class CadastroDespesaTipo extends BaseActivity {

    Button btnDespesaViagem, btnDespesaBasica;
    private SharedPreferences preferences;
    String _gsTipoDespesaViagem = "V";
    String _gsTipoDespesaLocal = "L";

    String _gsTIPOPESQUISA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_despesa_tipo);

        getSupportActionBar().setTitle("Tipo de Despesa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btnDespesaViagem = (Button) findViewById(R.id.btnDespesaViagem);
        btnDespesaBasica = (Button) findViewById(R.id.btnDespesaBasica);

        _gsTIPOPESQUISA = this.getIntent().getExtras().getSerializable("TIPO_PESQUISA").toString();

        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(CadastroDespesaTipo.this, "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

        btnDespesaBasica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent i = new Intent(CadastroDespesaTipo.this, CadastroDespesa.class);

                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                params.putString("CVD_TIPO", _gsTipoDespesaLocal);
                params.putString("TIPO_PESQUISA", _gsTIPOPESQUISA);
                i.putExtras(params);

                startActivity(i);
            }
        });

        btnDespesaViagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent i = new Intent(CadastroDespesaTipo.this, CadastroDespesa.class);

                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                params.putString("CVD_TIPO", _gsTipoDespesaViagem);
                params.putString("TIPO_PESQUISA", _gsTIPOPESQUISA);
                i.putExtras(params);

                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent i = new Intent(this, ListaDespesa.class);
                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                params.putString("TIPO_PESQUISA", _gsTIPOPESQUISA);
                i.putExtras(params);

                startActivity(i);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
