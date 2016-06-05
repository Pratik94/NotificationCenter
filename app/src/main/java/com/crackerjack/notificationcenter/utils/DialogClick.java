package com.crackerjack.notificationcenter.utils;

import android.view.View;

import com.crackerjack.notificationcenter.R;

/**
 * Created by pratik on 05/06/16.
 */
public abstract class DialogClick implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAllow:
                //write action for on press of btnAccept
                btnClickYes();
                break;
            case R.id.btnSkip:
                //write action for on press of btnCancel
                btnClickNo();
                break;

        }
    }

    abstract public void btnClickYes();

    abstract public void btnClickNo();


}
