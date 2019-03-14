package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guilherme on 18/08/2016.
 */
public class CAD_NOTIFICACAO implements Serializable {

    private int CN_ID;
    private int CU_ID;
    private Date CN_DATA;
    private String CN_NOTIFICACAO;

    public int getCN_ID() {
        return CN_ID;
    }

    public void setCN_ID(int CN_ID) {
        this.CN_ID = CN_ID;
    }

    public int getCU_ID() {
        return CU_ID;
    }

    public void setCU_ID(int CU_ID) {
        this.CU_ID = CU_ID;
    }

    public Date getCN_DATA() {
        return CN_DATA;
    }

    public void setCN_DATA(Date CN_DATA) {
        this.CN_DATA = CN_DATA;
    }

    public String getCN_NOTIFICACAO() {
        return CN_NOTIFICACAO;
    }

    public void setCN_NOTIFICACAO(String CN_NOTIFICACAO) {
        this.CN_NOTIFICACAO = CN_NOTIFICACAO;
    }
}
