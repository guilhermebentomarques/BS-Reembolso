package br.com.bornsolutions.bsreembolso.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import br.com.bornsolutions.bsreembolso.Fragment.CadastroDespesaItemFragment;
import br.com.bornsolutions.bsreembolso.Fragment.CadastroDespesaItemFotoFragment;

/**
 * Created by guilherme on 15/05/2016.
 */
public class FragmentPagerAdapterReembolsoItem extends android.support.v4.app.FragmentPagerAdapter {

    private String  [] _nTabTitles;

    public FragmentPagerAdapterReembolsoItem(FragmentManager fm, String  [] _pnTabTitles) {
        super(fm);
        this._nTabTitles = _pnTabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CadastroDespesaItemFragment();
            case 1:
                return new CadastroDespesaItemFotoFragment();
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
