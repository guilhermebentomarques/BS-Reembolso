package br.com.bornsolutions.bsreembolso.CL_Negocios;

import android.widget.EditText;

/**
 * Created by guilherme on 16/04/2016.
 */
public class CL_Login extends  CL_Negocios_Master {

    public Boolean prcValidarLogin(String _psLogin, EditText _psEdtLogin, String _psSenha,EditText _psEdtSenha)
    {
        Boolean _bRetorno = true;

        if(_psLogin.equals(""))
        {
            _psEdtLogin.setError("Favor preencher o campo login!");
            _bRetorno = false;
        }
        if(_psSenha.equals(""))
        {
            _psEdtSenha.setError("Favor preencher o campo senha!");
            _bRetorno = false;
        }

        return _bRetorno;
    }

}
