package br.com.bornsolutions.bsreembolso.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.WebService.WS_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.WebService.WS_CAD_USUARIO;
import br.com.bornsolutions.bsreembolso.WebService.WebService_Envio_Despesa;
import br.com.bornsolutions.bsreembolso.WebService.WebService_Envio_DespesaItem;

public class SincronizarActivity extends BaseActivity {

    ProgressBar pbprogressbarCadUsuario, pbprogressbarCadCliente;
    TextView txtStatusProgressCadUsuario, txtStatusProgressCadCliente;
    ProgressBar pbprogressbarCadDespesa, pbprogressbarCadDespesaItem, getPbprogressbarCadDespesa;
    TextView txtStatusProgressCadDespesa, txtStatusProgressCadDespesaItem;
    private List<CAD_VIAGEM_DESPESA> _gListaDespesa;
    private List<CAD_VIAGEM_DESPESA_ITEM> _gListaDespesaItem;
    SharedPreferences preferences;

    TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA;
    TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);

        TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(SincronizarActivity.this);
        TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(SincronizarActivity.this);

        //CRIA A ACTION BAR COM O BOTÃO VOLTA
        getSupportActionBar().setTitle("Sincronizar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(SincronizarActivity.this, "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

        //CADASTRO
        pbprogressbarCadUsuario = (ProgressBar) findViewById(R.id.pbprogressbarCadUsuario);
        txtStatusProgressCadUsuario = (TextView) findViewById(R.id.txtStatusProgressCadUsuario);
        pbprogressbarCadCliente = (ProgressBar) findViewById(R.id.pbprogressbarCadCliente);
        txtStatusProgressCadCliente = (TextView) findViewById(R.id.txtStatusProgressCadCliente);

        //DESPESA E DESPESA ITEM
        pbprogressbarCadDespesa = (ProgressBar) findViewById(R.id.pbprogressbarCadDespesa);
        txtStatusProgressCadDespesa = (TextView) findViewById(R.id.txtStatusProgressCadDespesa);
        pbprogressbarCadDespesaItem = (ProgressBar) findViewById(R.id.pbprogressbarCadDespesaItem);
        txtStatusProgressCadDespesaItem = (TextView) findViewById(R.id.txtStatusProgressCadDespesaItem);

        _gListaDespesa = TB_CAD_VIAGEM_DESPESA.prcLista_Despesa_Pendente(prpUsuario_ID);
        _gListaDespesaItem = TB_CAD_VIAGEM_DESPESA_ITEM.prcLista_DespesaItem_Pendente(prpUsuario_ID);

        txtStatusProgressCadDespesa.setText("Despesas 0 / " + _gListaDespesa.size());
        txtStatusProgressCadDespesaItem.setText("Despesas Item 0 / " + _gListaDespesaItem.size());


    }

    public void prcSyncCadastro(View view)
    {

        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WS_CAD_USUARIO WebServiceUsuario = new WS_CAD_USUARIO(this, this, pbprogressbarCadUsuario, txtStatusProgressCadUsuario);
            WebServiceUsuario.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }

        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WS_CAD_CLIENTE WebServiceCliente = new WS_CAD_CLIENTE(this, this, pbprogressbarCadCliente, txtStatusProgressCadCliente);
            WebServiceCliente.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }
    }

    public void prcSyncDespesas(View view)
    {
        //ENVIA DESPESA POR WEB SERVICE
        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WebService_Envio_Despesa WebService_Envio_Despesa = new WebService_Envio_Despesa(_gListaDespesa, this, this, pbprogressbarCadDespesa, txtStatusProgressCadDespesa, prpUsuario_Logado);
            WebService_Envio_Despesa.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }


        //ENVIA DESPESA POR WEB SERVICE
        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WebService_Envio_DespesaItem WebService_Envio_DespesaItem = new WebService_Envio_DespesaItem(_gListaDespesaItem, this, this, pbprogressbarCadDespesaItem, txtStatusProgressCadDespesaItem, prpUsuario_Logado);
            WebService_Envio_DespesaItem.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }
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
