package br.com.bornsolutions.bsreembolso.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Spinner;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_NOTIFICACAO_TEMPO;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_NOTIFICACAO_TEMPO;

public class ConfigurarActivity extends BaseActivity {

    Spinner spnTempoAtualizacao;
    TextView txtTempoAtual;
    TB_CAD_NOTIFICACAO_TEMPO TB_CAD_NOTIFICACAO_TEMPO;
    private SharedPreferences preferences;
    CAD_NOTIFICACAO_TEMPO TempoNotificacao;
    Button atvconf_btnGravar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);

        //CRIA A ACTION BAR COM O BOTÃO VOLTA
        getSupportActionBar().setTitle("Configurar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(ConfigurarActivity.this, "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

        spnTempoAtualizacao = (Spinner) findViewById(R.id.spnTempoAtualizacao);
        txtTempoAtual = (TextView) findViewById(R.id.txtTempoAtual);
        atvconf_btnGravar = (Button) findViewById(R.id.atvconf_btnGravar);

        TB_CAD_NOTIFICACAO_TEMPO = new TB_CAD_NOTIFICACAO_TEMPO(this);

        TempoNotificacao = TB_CAD_NOTIFICACAO_TEMPO.prcRetornaTempoUsuario(prpUsuario_ID);

        txtTempoAtual.setText(TempoNotificacao.getCNT_TEMPO()+ " Minutos");

        // Spinner Drop down elements
        List <CLG_Spinner> lables = prcGetTempoAtualziacao();
        // Creating adapter for spinner
        ArrayAdapter<CLG_Spinner> dataAdapter = new ArrayAdapter<CLG_Spinner>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnTempoAtualizacao.setAdapter(dataAdapter);

        //ON ITEM SELECTED SPINNER
        spnTempoAtualizacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        atvconf_btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CLG_Spinner) spnTempoAtualizacao.getSelectedItem()).getId() > 0) {
                    Integer _giTempoValor = ((CLG_Spinner) spnTempoAtualizacao.getSelectedItem()).getId();
                    String _gsTempoTexto = ((CLG_Spinner) spnTempoAtualizacao.getSelectedItem()).getValue();

                    Toast.makeText(ConfigurarActivity.this, _gsTempoTexto, Toast.LENGTH_SHORT).show();

                    CAD_NOTIFICACAO_TEMPO TempoNotificacao = new CAD_NOTIFICACAO_TEMPO();

                    TB_CAD_NOTIFICACAO_TEMPO TB_CAD_NOTIFICACAO_TEMPO = new TB_CAD_NOTIFICACAO_TEMPO(ConfigurarActivity.this);
                    if (TB_CAD_NOTIFICACAO_TEMPO.prcVerificaExiste(prpUsuario_ID)) {
                        TempoNotificacao.setCU_ID(Integer.parseInt(prpUsuario_ID));
                        TempoNotificacao.setCNT_TEMPO(_giTempoValor);
                        //UPDATE
                        TB_CAD_NOTIFICACAO_TEMPO.prcAtualizar(TempoNotificacao);
                    } else {
                        TempoNotificacao.setCU_ID(Integer.parseInt(prpUsuario_ID));
                        TempoNotificacao.setCNT_TEMPO(_giTempoValor);
                        //INSERT
                        TB_CAD_NOTIFICACAO_TEMPO.prcIncluir(TempoNotificacao);
                    }
                }
            }
        });

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

    public List< CLG_Spinner> prcGetTempoAtualziacao(){

        List < CLG_Spinner > labels = new ArrayList< CLG_Spinner >();
        labels.add(new CLG_Spinner(0, "Selecione"));
        labels.add(new CLG_Spinner(15, "15 Minutos"));
        labels.add(new CLG_Spinner(30, "35 Minutos"));
        labels.add(new CLG_Spinner(45, "45 Minutos"));
        labels.add(new CLG_Spinner(60, "60 Minutos"));
        labels.add(new CLG_Spinner(120, "120 Minutos"));
        // returning labels
        return labels;
    }

}
