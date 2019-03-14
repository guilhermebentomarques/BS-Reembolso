package br.com.bornsolutions.bsreembolso.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Conexao;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_CustomListView;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Util.Constantes;
import br.com.bornsolutions.bsreembolso.Util.TipoMsg;
import br.com.bornsolutions.bsreembolso.Util.Util;
import br.com.bornsolutions.bsreembolso.WebService.WS_Envio_Despesa_AlteraTipo;
import br.com.bornsolutions.bsreembolso.WebService.WS_Envio_Despesa_Finalizada;
import br.com.bornsolutions.bsreembolso.WebService.WS_Envio_Despesa_Reabrir;

public class ListaDespesa extends BaseActivity {

    private ListView listViewCustom;
    private int _igPosicaoSelecionada;
    private List<CAD_VIAGEM_DESPESA> listareembolso;
    private SharedPreferences preferences;
    TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA;
    TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM;
    String _gsDataInicio, _gsDataFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_despesa);

        prpTipoPesquisa = this.getIntent().getExtras().getString("TIPO_PESQUISA").toString();

        try {
            _gsDataInicio = Constantes.DATA_INICIO;
            _gsDataFim = Constantes.DATA_FIM;
        }
        catch (Exception ex)
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            _gsDataInicio = dateFormat.format(date);
            _gsDataFim = dateFormat.format(date);
        }

        TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(ListaDespesa.this);
        TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(ListaDespesa.this);

        //CRIA A ACTION BAR COM O BOTÃO VOLTA
        getSupportActionBar().setTitle("Lista Despesas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(ListaDespesa.this, "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddReembolso);

        if(prpTipoPesquisa.equals("CONSULTA")) {
            fab.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent i = new Intent(ListaDespesa.this, CadastroDespesaTipo.class);

                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                params.putString("TIPO_PESQUISA", prpTipoPesquisa);
                i.putExtras(params);

                startActivity(i);
            }
        });

        listViewCustom = (ListView) findViewById(R.id.listViewCustom);
        prcCarregaListaDespesas(prpTipoPesquisa, _gsDataInicio, _gsDataFim);

        listViewCustom.setOnItemClickListener(clickListenerReembolso); //SETA O CLICK
        listViewCustom.setOnCreateContextMenuListener(contextmenulistener); //CLICA E SEGURA
        listViewCustom.setOnItemLongClickListener(longclicklistenerReembolso); //CLICK LONGO PEGA O ID CLICADO
    }

    private AdapterView.OnItemLongClickListener longclicklistenerReembolso = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            _igPosicaoSelecionada = position;
            CAD_VIAGEM_DESPESA Reembolso = (CAD_VIAGEM_DESPESA)listareembolso.get(position);
            return false;
        }
    };

    private View.OnCreateContextMenuListener contextmenulistener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //SÓ SE FOR NA EDIÇÃO, QUANDO ESTIVER FINALIZADO, NÃO PODE MAIS REMOVER O ITEM
            if(!prpTipoPesquisa.equals("CONSULTA")) {
                menu.setHeaderTitle("Opções").setHeaderIcon(R.drawable.btn_edit).add(1, 10, 1, "Finalizar");
                menu.add(1, 20, 2, "Editar");
                menu.add(1, 30, 2, "Excluir");
                menu.add(1, 40, 2, "Alterar Tipo Despesa");
            }
            else if(prpTipoPesquisa.equals("CONSULTA")) {
                menu.add(1, 50, 2, "Reabrir Despesa");
            }
        }
    };

    //CLICK NOS ITENS DO MENU
    private AdapterView.OnItemClickListener clickListenerReembolso = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CAD_VIAGEM_DESPESA Reembolso = (CAD_VIAGEM_DESPESA)listareembolso.get(position); //TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(listareembolso.get(position).getCVD_ID());

            Intent i = new Intent(ListaDespesa.this, DespesaItensActivity.class);
            i.putExtra("Reembolso", Reembolso); //ENVIA PESSOA COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO
            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuario_Logado);
            params.putString("CU_ID", prpUsuario_ID);
            params.putString("TIPO_PESQUISA", prpTipoPesquisa);
            i.putExtras(params);

            startActivity(i);
            //finish();
        }
    };

    //QUANDO UM ITEM QUE SEGUROU PARA APARECER É CLICADO;
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        CLG_Conexao CL_Conexao = new CLG_Conexao(ListaDespesa.this);

        if(item.getItemId() == 10)
        {
            if(CL_Conexao.verificaConexao()) {
                Util.showMsgConfirm(ListaDespesa.this, "Finalizar Despesa", "Deseja realmente finalizar o registro?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int _iCVD_ID = listareembolso.get(_igPosicaoSelecionada).getCVD_ID();
                        CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(_iCVD_ID);

                        //SE SIM FINALIZA
                        Despesa.setCVD_FINALIZADO("S");

                        //ENVIA DESPESA POR WEB SERVICE
                        try {
                            //NO WEB SERVICE JÁ VALIDA TUDO
                            WS_Envio_Despesa_Finalizada WebService_EnvioDespesa_Finalizada = new WS_Envio_Despesa_Finalizada(ListaDespesa.this, Despesa, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
                            WebService_EnvioDespesa_Finalizada.execute();

                        } catch (Exception ex) {
                            String _sErro = ex.getMessage();
                        }
                        prcCarregaListaDespesas(prpTipoPesquisa, "", "");
                    }
                });
            }
            else
            {
                Toast.makeText(ListaDespesa.this, "Sem acesso a internet: Opção inválida!!", Toast.LENGTH_LONG).show();
            }
        }
        else if(item.getItemId() == 20)
        {
                Util.showMsgConfirm(ListaDespesa.this, "Editar Despesa", "Deseja realmente editar o registro?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int _iCVD_ID = listareembolso.get(_igPosicaoSelecionada).getCVD_ID();
                        CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(_iCVD_ID);

                        Intent i = new Intent(ListaDespesa.this, CadastroDespesa.class);
                        Bundle params = new Bundle();
                        params.putString("CU_LOGIN", prpUsuario_Logado);
                        params.putString("CU_ID", prpUsuario_ID);
                        params.putString("TIPO_PESQUISA", prpTipoPesquisa);
                        params.putString("CVD_TIPO", Despesa.getCVD_TIPO());
                        i.putExtras(params);
                        i.putExtra("Despesa", Despesa); //ENVIA PESSOA COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO
                        startActivity(i);
                        finish();
                    }
                });
        }
        else if(item.getItemId() == 30)
        {
            if(CL_Conexao.verificaConexao()) {
                Util.showMsgConfirm(ListaDespesa.this, "Remover Despesa", "Deseja realmente remover o registro?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int _iCVD_ID = listareembolso.get(_igPosicaoSelecionada).getCVD_ID();
                        CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(_iCVD_ID);
                        TB_CAD_VIAGEM_DESPESA.prcDeletar(ListaDespesa.this, _iCVD_ID, Despesa, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
                        TB_CAD_VIAGEM_DESPESA_ITEM.prcDeletar_Todos(_iCVD_ID);
                        //prcCarregaListaDespesas(prpTipoPesquisa);
                    }
                });
            }
            else
            {
                Toast.makeText(ListaDespesa.this, "Sem acesso a internet: Opção inválida!!", Toast.LENGTH_LONG).show();
            }

        }
        else if(item.getItemId() == 40)
        {
            if(CL_Conexao.verificaConexao()) {
                Util.showMsgConfirm(ListaDespesa.this, "Alterar Tipo Despesa", "Deseja realmente alterar o tipo de despesa?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int _iCVD_ID = listareembolso.get(_igPosicaoSelecionada).getCVD_ID();
                        CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(_iCVD_ID);

                        //SE SIM FINALIZA
                        if (Despesa.getCVD_TIPO().equals("V"))
                            Despesa.setCVD_TIPO("L");
                        else
                            Despesa.setCVD_TIPO("V");

                        //ENVIA DESPESA POR WEB SERVICE
                        try {
                            //NO WEB SERVICE JÁ VALIDA TUDO
                            WS_Envio_Despesa_AlteraTipo WebService_Envio_Despesa_AlteraTipo = new WS_Envio_Despesa_AlteraTipo(ListaDespesa.this, Despesa, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
                            WebService_Envio_Despesa_AlteraTipo.execute();

                        } catch (Exception ex) {
                            String _sErro = ex.getMessage();
                        }
                        prcCarregaListaDespesas(prpTipoPesquisa, "", "");
                    }
                });
            }
            else
            {
                Toast.makeText(ListaDespesa.this, "Sem acesso a internet: Opção inválida!!", Toast.LENGTH_LONG).show();
            }
        }
        else if(item.getItemId() == 50) //REABRIR DESPESA
        {
            if(CL_Conexao.verificaConexao()) {
                Util.showMsgConfirm(ListaDespesa.this, "Reabrir Despesa", "Deseja realmente reabrir a despesa?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int _iCVD_ID = listareembolso.get(_igPosicaoSelecionada).getCVD_ID();
                        CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(_iCVD_ID);

                        //SE SIM FINALIZA
                        if (Despesa.getCVD_FINALIZADO().equals("S"))
                            Despesa.setCVD_FINALIZADO("N");

                        //ENVIA DESPESA POR WEB SERVICE
                        try {
                            //NO WEB SERVICE JÁ VALIDA TUDO
                            WS_Envio_Despesa_Reabrir WebService_Envio_Despesa_Reabrir = new WS_Envio_Despesa_Reabrir(ListaDespesa.this, Despesa, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
                            WebService_Envio_Despesa_Reabrir.execute();

                        } catch (Exception ex) {
                            String _sErro = ex.getMessage();
                        }
                        prcCarregaListaDespesas(prpTipoPesquisa, _gsDataInicio, _gsDataFim);
                    }
                });
            }
            else
            {
                Toast.makeText(ListaDespesa.this, "Sem acesso a internet: Opção inválida!!", Toast.LENGTH_LONG).show();
            }
        }
        //return super.onContextItemSelected(item);
        return true;
    }

    @NonNull
    private void prcCarregaListaDespesas(String _psTipoTela, String _psDataInicio, String _psDataFim)
    {
        listareembolso = TB_CAD_VIAGEM_DESPESA.prcLista(prpUsuario_ID, prpTipoPesquisa, _psDataInicio, _psDataFim);

        List<ListViewItem> items = new ArrayList<ListViewItem>();

        for(final CAD_VIAGEM_DESPESA r : listareembolso){

            final String _strDescricao = r.getCVD_DESCRICAO();
            DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
            final String _strDataViagem = dateFormat.format(r.getCVD_VIAGEM_DATA_INICIO());

            TB_CAD_CLIENTE TB_CAD_CLIENTE = new TB_CAD_CLIENTE(ListaDespesa.this);
            final CAD_CLIENTE c = TB_CAD_CLIENTE.prcRetornaCliente(r.getCL_ID());

            items.add(new ListViewItem() {{
                //ThumbnailResource = _iImagem;

                //VALIDA O TIPO DE IMAGEM DE ACORDO COM A DESPESA
                if (r.getCVD_TIPO().toString().equals("V")) {
                    if (r.getCVD_FINALIZADO().toString().equals("S"))
                        ThumbnailResource = R.drawable.btn_aviao_cinza;
                    else
                        ThumbnailResource = R.drawable.btn_aviao_azul;
                } else {
                    if(r.getCVD_FINALIZADO().toString().equals("S"))
                        ThumbnailResource = R.drawable.btn_dinheiro_cinza;
                    else
                        ThumbnailResource = R.drawable.btn_dinheiro_azul;
                }

                Titulos = _strDescricao;
                Proposta = c.getCL_RAZAOSOCIAL();
                Data = _strDataViagem;
                Valores = "Adiantamento - R$" + String.format("%.2f", r.getCVD_ADIANTAMENTO_VALOR());

                if(!String.valueOf(r.getCVD_ID_BORN()).equals("0"))
                    CodInterno =  "Cod: " + r.getCVD_ID_BORN();
            }});


        }

        CLG_CustomListView adapter = new CLG_CustomListView(this, items);
        listViewCustom.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:

                Intent i = new Intent(ListaDespesa.this, MainActivity.class);
                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                i.putExtras(params);

                startActivity(i);

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ListViewItem
    {
        public int ThumbnailResource;
        public String Titulos;
        public String Valores;
        public String Data;
        public String Proposta;
        public String CodInterno;
    }

}
