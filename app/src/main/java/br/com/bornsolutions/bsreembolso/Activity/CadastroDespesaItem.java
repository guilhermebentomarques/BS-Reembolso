package br.com.bornsolutions.bsreembolso.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import br.com.bornsolutions.bsreembolso.Adapter.FragmentPagerAdapterReembolsoItem;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.R;

public class CadastroDespesaItem extends BaseActivity {

    private TabLayout nTabLayout;
    private ViewPager nViewPager;
    CAD_VIAGEM_DESPESA Reembolso;
    SharedPreferences preferences;
    String _gsTIPOPESQUISA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_despesa_item_novo);

        //CRIA A ACTION BAR COM O BOTÃO VOLTA
        getSupportActionBar().setTitle("Cadastro Despesa Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        _gsTIPOPESQUISA = this.getIntent().getExtras().getSerializable("TIPO_PESQUISA").toString();

        Reembolso = (CAD_VIAGEM_DESPESA) this.getIntent().getExtras().getSerializable("Reembolso");

        nTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        nViewPager = (ViewPager) findViewById(R.id.view_pager);

        String [] _sTabs = {"Despesa","Foto"};

        nViewPager.setAdapter(new FragmentPagerAdapterReembolsoItem(getSupportFragmentManager(), _sTabs));

        nTabLayout.setupWithViewPager(nViewPager);

        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(CadastroDespesaItem.this, "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent i = new Intent(this, DespesaItensActivity.class);
                i.putExtra("Reembolso",Reembolso);
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