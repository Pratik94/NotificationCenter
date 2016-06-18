package com.crackerjack.notificationcenter.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crackerjack.notificationcenter.R;
import com.crackerjack.notificationcenter.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuotesFragment extends BaseFragment {


    public QuotesFragment() {
        // Required empty public constructor
    }

    public static QuotesFragment newInstance(){

        QuotesFragment fragment = new QuotesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateKnifeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quotes, container, false);
    }

    @Override
    public String getTitle() {
        return null;
    }

}
