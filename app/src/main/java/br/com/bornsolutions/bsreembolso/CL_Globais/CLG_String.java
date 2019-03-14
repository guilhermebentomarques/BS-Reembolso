package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.widget.Adapter;

/**
 * Created by guilherme on 31/08/2016.
 */
public class CLG_String  {

    public int indexOf(final Adapter adapter, Object value)
    {
        for (int index = 0, count = adapter.getCount(); index < count; ++index)
        {
            if (adapter.getItem(index).toString().trim().equals(value.toString().trim()))
            {
                return index;
            }
        }
        return -1;
    }

    public String prcTransformaValoremDouble(String _sValor)
    {
        //TRATATIVA NUMERO - PEGO A VIRGULA E DOU SPLIT, DEPOIS JUNTO, RETORNANDO O TEXTO CORRETO
        String [] _strValor = _sValor.split(",");
        _sValor = _strValor[0].replace(".","") + "," + _strValor[1];
        _sValor = String.format("%.2f", Double.parseDouble(_sValor.replace(",", ".")));
        _sValor = _sValor.replace(",",".");

        return _sValor;
    }
}
