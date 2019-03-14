package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by guilherme on 15/08/2016.
 */
public class CLG_Conexao
{
    Context _gContext;

    public CLG_Conexao(Context _psContext)
    {
        _gContext = _psContext;
    }

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) _gContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }
}
