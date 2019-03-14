package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;

/**
 * Created by guilherme on 21/04/2016.
 */
public class CAD_USUARIO { //ENVIA PESSOA COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO

    private int CU_ID;
    private String CU_LOGIN;
    private String CU_SENHA;

    public int getCU_ID() {
        return CU_ID;
    }

    public void setCU_ID(int CU_ID) {
        this.CU_ID = CU_ID;
    }

    public String getCU_LOGIN() {
        return CU_LOGIN;
    }

    public void setCU_LOGIN(String CU_LOGIN) {
        this.CU_LOGIN = CU_LOGIN;
    }

    public String getCU_SENHA() {
        return CU_SENHA;
    }

    public void setCU_SENHA(String CU_SENHA) {
        this.CU_SENHA = CU_SENHA;
    }


}
