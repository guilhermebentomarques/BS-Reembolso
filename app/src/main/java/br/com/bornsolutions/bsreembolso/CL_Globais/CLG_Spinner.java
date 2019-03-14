package br.com.bornsolutions.bsreembolso.CL_Globais;

/**
 * Created by guilherme on 24/04/2016.
 */
public class CLG_Spinner  {

    private  int databaseId;
    private String databaseValue;

    public CLG_Spinner( int databaseId , String databaseValue ) {
        this.databaseId = databaseId;
        this.databaseValue = databaseValue;
    }

    public int getId () {
        return databaseId;
    }

    public String getValue () {
        return databaseValue;
    }

    @Override
    public String toString () {
        return databaseValue;
    }

}
