package br.com.bornsolutions.bsreembolso.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Activity.CadastroDespesaItem;
import br.com.bornsolutions.bsreembolso.Activity.DespesaItensActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Conexao;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Fotos;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_PROPOSTA;
import br.com.bornsolutions.bsreembolso.Util.TipoMsg;
import br.com.bornsolutions.bsreembolso.Util.Util;

/**
 * Created by guilherme on 02/05/2016.
 */
public class FragmentDespesaItem_Dados extends Fragment {

    TextView txtReembolso,txtDataAdiantamento, txtTitulo, txtValores, txtProposta;
    private ListView listViewCustomItem;
    private int _igPosicaoSelecionada;
    private List<CAD_VIAGEM_DESPESA_ITEM> listareembolsoitem;
    private SharedPreferences preferences;
    TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM;
    CAD_VIAGEM_DESPESA Reembolso;
    Integer _giCVD_ID;
    String prpUsuario_Logado,prpUsuario_ID, _gsTIPOPESQUISA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragmentdespesaitem_dados, container, false);

        TextView txtDataAdiantamento = (TextView) view.findViewById(R.id.txtData);
        TextView txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        TextView txtProposta = (TextView) view.findViewById(R.id.txtProposta);

        _gsTIPOPESQUISA = getActivity().getIntent().getExtras().getSerializable("TIPO_PESQUISA").toString();
        Reembolso = (CAD_VIAGEM_DESPESA) getActivity().getIntent().getExtras().getSerializable("Reembolso"); //BUSCA A PESSOA PASSADA COMO PARAMETRO
        _giCVD_ID = Reembolso.getCVD_ID();


        DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        final String _strDataViagem = dateFormat.format(Reembolso.getCVD_VIAGEM_DATA_INICIO());

        TB_CAD_CLIENTE TB_CAD_CLIENTE = new TB_CAD_CLIENTE(getActivity());
        final CAD_CLIENTE Cliente = TB_CAD_CLIENTE.prcRetornaCliente(Reembolso.getCL_ID());

        txtDataAdiantamento.setText(_strDataViagem);
        txtProposta.setText(Cliente.getCL_RAZAOSOCIAL());
        txtTitulo.setText(Reembolso.getCVD_DESCRICAO());

        TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(getActivity());

        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(getActivity(), "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btnAddReembolso);

        if(_gsTIPOPESQUISA.equals("CONSULTA"))
            fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                //Intent i = new Intent(getActivity(), CadastroDespesaItem.class);
                Intent i = new Intent(getActivity(), CadastroDespesaItem.class);
                i.putExtra("Reembolso", Reembolso);
                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                params.putString("TIPO_PESQUISA", _gsTIPOPESQUISA);
                i.putExtras(params);

                startActivity(i);
            }
        });

        listViewCustomItem = (ListView) view.findViewById(R.id.listViewCustomItem);
        prcSetArrayAdapterReembolsoItem();

        listViewCustomItem.setOnItemClickListener(clickListenerReembolso); //SETA O CLICK
        listViewCustomItem.setOnCreateContextMenuListener(contextmenulistener); //CLICA E SEGURA
        listViewCustomItem.setOnItemLongClickListener(longclicklistenerReembolso); //CLICK LONGO PEGA O ID CLICADO

        return view;
    }

    private AdapterView.OnItemLongClickListener longclicklistenerReembolso = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            _igPosicaoSelecionada = position;
            CAD_VIAGEM_DESPESA_ITEM Reembolso = TB_CAD_VIAGEM_DESPESA_ITEM.prcRetornaDespesaItem(listareembolsoitem.get(_igPosicaoSelecionada).getCVD_ID());
            return false;
        }
    };

    private View.OnCreateContextMenuListener contextmenulistener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opções").setHeaderIcon(R.drawable.btn_edit).add(1, 10, 1, "Editar");
            menu.add(1, 20, 2, "Excluir");
        }
    };

    //CLICK NOS ITENS DO MENU
    private AdapterView.OnItemClickListener clickListenerReembolso = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _igPosicaoSelecionada = position;
            CAD_VIAGEM_DESPESA_ITEM ReembolsoItem = TB_CAD_VIAGEM_DESPESA_ITEM.prcRetornaDespesaItem(listareembolsoitem.get(_igPosicaoSelecionada).getCVDI_ID());
            //Intent i = new Intent(getActivity(), CadastroDespesaItem.class);

            Intent i = new Intent(getActivity(), CadastroDespesaItem.class);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuario_Logado);
            params.putString("CU_ID", prpUsuario_ID);
            params.putString("TIPO_PESQUISA", _gsTIPOPESQUISA);
            i.putExtras(params);

            i.putExtra("Reembolso", Reembolso); //ENVIA O REEMBOLSO COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO
            i.putExtra("ReembolsoItem", ReembolsoItem); //ENVIA O REEMBOLSO COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO
            startActivity(i);
            getActivity().finish();
        }
    };

    //QUANDO UM ITEM QUE SEGUROU PARA APARECER É CLICADO;
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        CLG_Conexao CL_Conexao = new CLG_Conexao(getContext());

        if(item.getItemId() == 10) {

               Integer _iCVDI_ID = listareembolsoitem.get(_igPosicaoSelecionada).getCVDI_ID();
                CAD_VIAGEM_DESPESA_ITEM ReembolsoItem = TB_CAD_VIAGEM_DESPESA_ITEM.prcRetornaDespesaItem(_iCVDI_ID);

                Intent i = new Intent(getActivity(), CadastroDespesaItem.class);

                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                params.putString("TIPO_PESQUISA", _gsTIPOPESQUISA);
                i.putExtras(params);

                i.putExtra("Reembolso", Reembolso); //ENVIA O REEMBOLSO COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO
                i.putExtra("ReembolsoItem", ReembolsoItem); //ENVIA O REEMBOLSO COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO
                startActivity(i);
                getActivity().finish();

        }
        else if(item.getItemId() == 20)
        {
            if(CL_Conexao.verificaConexao()) {
                Util.showMsgConfirm(getActivity(), "Remover Reembolso", "Deseja realmente remover o registro?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int _iCVDI_ID = listareembolsoitem.get(_igPosicaoSelecionada).getCVDI_ID();
                        CAD_VIAGEM_DESPESA_ITEM DespesaItem = TB_CAD_VIAGEM_DESPESA_ITEM.prcRetornaDespesaItem(_iCVDI_ID);
                        TB_CAD_VIAGEM_DESPESA_ITEM.prcDeletar(getActivity(), _iCVDI_ID, DespesaItem, prpUsuario_ID, prpUsuario_Logado, _gsTIPOPESQUISA);
                        //prcSetArrayAdapterReembolsoItem();
                        //arrayadapter.notifyDataSetChanged(); //INFORMA QUE OS DADOS MUDARAM
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(), "Sem acesso a internet: Opção inválida!!", Toast.LENGTH_LONG).show();
            }
        }
        //return super.onContextItemSelected(item);
        return true;
    }

    @NonNull
    private void prcSetArrayAdapterReembolsoItem()
    {
        listareembolsoitem = TB_CAD_VIAGEM_DESPESA_ITEM.prcLista(_giCVD_ID);

        List<ListViewItemNovo> items = new ArrayList<ListViewItemNovo>();

        for(final CAD_VIAGEM_DESPESA_ITEM r : listareembolsoitem){

            final String _strDescricao = r.getCVDI_DESCRICAO();
            DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
            final String _strDataViagem = dateFormat.format(r.getCVDI_DATA());

            TB_CAD_PROPOSTA TB_CAD_PROPOSTA = new TB_CAD_PROPOSTA(getActivity());
            final String _strTipoDespesa = r.getCVDI_TIPO_DESPESA();

            items.add(new ListViewItemNovo()
            {{
                    try {
                        Foto = CLG_Fotos.prcSQLite_RetornaFoto(r.getCVDI_FOTO());
                    }
                    catch (Exception ex)
                    {}
                    TipoDespesa = _strTipoDespesa;
                    Descricao = _strDescricao;
                    Data = _strDataViagem;
                    if(_strTipoDespesa.equals("Km Rodado"))
                        Valor = "Valor - R$" + String.format("%.2f", r.getCVDI_VALOR()) + " - " + r.getCVDI_KM_DISTANCIA() + " Km";
                    else
                        Valor = "Valor - R$" + String.format("%.2f", r.getCVDI_VALOR());
                }});


        }

        CLG_CustomListViewItem adapter = new CLG_CustomListViewItem(getActivity(), items);
        listViewCustomItem.setAdapter(adapter);
    }

    public class ListViewItemNovo
    {
        public Bitmap Foto;
        public String Descricao;
        public String Valor;
        public String Data;
        public String TipoDespesa;
    }
}

class CLG_CustomListViewItem extends BaseAdapter {

    LayoutInflater inflater;
    List<FragmentDespesaItem_Dados.ListViewItemNovo> items;

    public CLG_CustomListViewItem(FragmentActivity context, List<FragmentDespesaItem_Dados.ListViewItemNovo> items) {
        super();
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        FragmentDespesaItem_Dados.ListViewItemNovo item = items.get(position);

        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.listview_despesa_item, null);


        TextView txtTitulo = (TextView) vi.findViewById(R.id.txtDescricao);
        TextView txtData = (TextView) vi.findViewById(R.id.txtData);
        TextView txtTipoDespesa = (TextView) vi.findViewById(R.id.txtTipoDespesa);
        TextView txtValores = (TextView) vi.findViewById(R.id.txtValor);

        txtTitulo.setText(item.Descricao);
        txtTipoDespesa.setText(item.TipoDespesa);
        txtValores.setText(item.Valor);
        txtData.setText(item.Data);


        try {
            ImageView imgThumbnail = (ImageView) vi.findViewById(R.id.imgFotoDespesa);
            imgThumbnail.setImageBitmap(item.Foto);
        }
        catch (Exception ex)
        {}

        return vi;
    }

}
