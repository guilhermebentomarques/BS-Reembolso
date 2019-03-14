package br.com.bornsolutions.bsreembolso.CL_Negocios;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by guilherme on 16/04/2016.
 */
public class CL_Preferences extends CL_Negocios_Master {

    //OBJETO PARA SALVAR DADOS DA APLICAÇÃO, COMO COOKIES DA WEB OU SESSÕES
    private SharedPreferences preferences;

    public void PrcRemove_SharedPreferences(Activity activity, String _psNomeSharedPreferences)
    {
        SharedPreferences settings = activity.getSharedPreferences("CAD_USUARIO", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public SharedPreferences PrcGet_SharedPreferences_Login(Activity activity, String _psNomeSharedPreferences)
    {
        //SE O USUÁRIO TIVER FEITO LOGIN, FECHADO O APLICATIVO E FIZER LOGIN DE NOVO
        // AS VARIÁVEIS DO PREFERENCES ESTARÃO PREENCHIDAS ENTÃO ELE NÃO TERÁ QUE FAZER O LOGIN NOVAMENTE.
        preferences = activity.getSharedPreferences("CAD_USUARIO", Context.MODE_PRIVATE); //PREF É O NOME DO ARQUIVO

        return preferences;
    }

    public void prcSet_SharedPreferences_Login(Activity activity, String _psLogin, String _psSenha, String _psID)
    {
        SharedPreferences.Editor editor = activity.getSharedPreferences("CAD_USUARIO", Context.MODE_PRIVATE).edit();
        editor.putString("CU_LOGIN", _psLogin);
        editor.putString("CU_SENHA", _psSenha);
        editor.putString("CU_ID", _psID);
        editor.commit();
    }


}
