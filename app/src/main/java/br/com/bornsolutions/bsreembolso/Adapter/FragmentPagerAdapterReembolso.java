package br.com.bornsolutions.bsreembolso.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import br.com.bornsolutions.bsreembolso.Fragment.FragmentDespesaItem_Dados;
import br.com.bornsolutions.bsreembolso.Fragment.FragmentDespesaItem_Valores;

/**
 * Created by guilherme on 02/05/2016.
 */
public class FragmentPagerAdapterReembolso extends android.support.v4.app.FragmentPagerAdapter {

    private String  [] _nTabTitles;

    public FragmentPagerAdapterReembolso(FragmentManager fm, String[] _pnTabTitles) {
        super(fm);
        this._nTabTitles = _pnTabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentDespesaItem_Dados();
            case 1:
                return new FragmentDespesaItem_Valores();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this._nTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this._nTabTitles[position]; //seta titulo pela posicao
    }
}
