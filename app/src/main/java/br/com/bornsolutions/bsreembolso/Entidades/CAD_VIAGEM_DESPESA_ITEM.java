package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * Created by guilherme on 01/05/2016.
 */
public class CAD_VIAGEM_DESPESA_ITEM implements Serializable {

    private Integer CVDI_ID;
    private Integer CVDI_ID_BORN;
    private Integer CVD_ID;
    private Date CVDI_DATA;
    private String CVDI_TIPO_DESPESA;
    private Double CVDI_VALOR;
    private Double CVDI_KM_VALOR;
    private Double CVDI_KM_DISTANCIA;
    private String CVDI_DESCRICAO;
    private String CVDI_DESPESA_BORN;

    public String getCVDI_DESPESA_BORN() {
        return CVDI_DESPESA_BORN;
    }

    public void setCVDI_DESPESA_BORN(String CVDI_DESPESA_BORN) {
        this.CVDI_DESPESA_BORN = CVDI_DESPESA_BORN;
    }


    public Integer getCVDI_ID_BORN() {
        return CVDI_ID_BORN;
    }

    public void setCVDI_ID_BORN(Integer CVDI_ID_BORN) {
        this.CVDI_ID_BORN = CVDI_ID_BORN;
    }

    public byte[] getCVDI_FOTO() {
        return CVDI_FOTO;
    }

    public void setCVDI_FOTO(byte[] CVDI_FOTO) {
        this.CVDI_FOTO = CVDI_FOTO;
    }

    private byte [] CVDI_FOTO;

    public Integer getCVDI_ID() {
        return CVDI_ID;
    }

    public void setCVDI_ID(Integer CVDI_ID) {
        this.CVDI_ID = CVDI_ID;
    }

    public Integer getCVD_ID() {
        return CVD_ID;
    }

    public void setCVD_ID(Integer CVD_ID) {
        this.CVD_ID = CVD_ID;
    }

    public Date getCVDI_DATA() {
        return CVDI_DATA;
    }

    public void setCVDI_DATA(Date CVDI_DATA) {
        this.CVDI_DATA = CVDI_DATA;
    }

    public String getCVDI_TIPO_DESPESA() {
        return CVDI_TIPO_DESPESA;
    }

    public void setCVDI_TIPO_DESPESA(String CVDI_TIPO_DESPESA) {
        this.CVDI_TIPO_DESPESA = CVDI_TIPO_DESPESA;
    }

    public Double getCVDI_VALOR() {
        return CVDI_VALOR;
    }

    public void setCVDI_VALOR(Double CVDI_VALOR) {
        this.CVDI_VALOR = CVDI_VALOR;
    }

    public Double getCVDI_KM_VALOR() {
        return CVDI_KM_VALOR;
    }

    public void setCVDI_KM_VALOR(Double CVDI_KM_VALOR) {
        this.CVDI_KM_VALOR = CVDI_KM_VALOR;
    }

    public Double getCVDI_KM_DISTANCIA() {
        return CVDI_KM_DISTANCIA;
    }

    public void setCVDI_KM_DISTANCIA(Double CVDI_KM_DISTANCIA) {
        this.CVDI_KM_DISTANCIA = CVDI_KM_DISTANCIA;
    }

    public String getCVDI_DESCRICAO() {
        return CVDI_DESCRICAO;
    }

    public void setCVDI_DESCRICAO(String CVDI_DESCRICAO) {
        this.CVDI_DESCRICAO = CVDI_DESCRICAO;
    }
}
