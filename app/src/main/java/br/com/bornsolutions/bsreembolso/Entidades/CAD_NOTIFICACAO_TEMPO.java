package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;

/**
 * Created by guilherme on 18/08/2016.
 */
public class CAD_NOTIFICACAO_TEMPO implements Serializable {

    private int CU_ID;
    private int CNT_TEMPO;

    public int getCU_ID() {
        return CU_ID;
    }

    public void setCU_ID(int CU_ID) {
        this.CU_ID = CU_ID;
    }

    public int getCNT_TEMPO() {
        return CNT_TEMPO;
    }

    public void setCNT_TEMPO(int CNT_TEMPO) {
        this.CNT_TEMPO = CNT_TEMPO;
    }
}
