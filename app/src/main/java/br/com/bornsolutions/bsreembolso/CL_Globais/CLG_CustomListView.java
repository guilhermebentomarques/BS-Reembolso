package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.bornsolutions.bsreembolso.Activity.ListaDespesa;
import br.com.bornsolutions.bsreembolso.R;

/**
 * Created by guilherme on 26/04/2016.
 */
public class CLG_CustomListView extends BaseAdapter {

    LayoutInflater inflater;
    List<ListaDespesa.ListViewItem> items;

    public CLG_CustomListView(Activity context, List<ListaDespesa.ListViewItem> items) {
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

        ListaDespesa.ListViewItem item = items.get(position);

        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.listview_despesa, null);

        ImageView imgThumbnail = (ImageView) vi.findViewById(R.id.imgThumbnail);
        TextView txtTitulo = (TextView) vi.findViewById(R.id.txtTitulo);
        TextView txtData = (TextView) vi.findViewById(R.id.txtData);
        TextView txtCliente = (TextView) vi.findViewById(R.id.txtProposta);
        TextView txtValores = (TextView) vi.findViewById(R.id.txtValores);
        TextView txtCodInterno = (TextView) vi.findViewById(R.id.txtCodInterno);

        imgThumbnail.setImageResource(item.ThumbnailResource);
        txtTitulo.setText(item.Titulos);
        txtCliente.setText(item.Proposta);
        txtValores.setText(item.Valores);
        txtData.setText(item.Data);
        txtCodInterno.setText(item.CodInterno);

        return vi;
    }

}
