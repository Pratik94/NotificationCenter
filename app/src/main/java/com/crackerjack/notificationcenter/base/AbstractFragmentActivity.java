package com.crackerjack.notificationcenter.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

/**
 * Created by pratik on 05/06/16.
 */
public abstract class AbstractFragmentActivity extends BaseActivity implements BaseFragment.FragmentTransacListener, BaseFragment.FragmentAttachListener{

    protected BaseFragment currentFragment;
    private String TAG = "AbstractFragmentActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v("onCreate");
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }

        if (currentFragment != null) {
            if (!currentFragment.onBackPressed()) {
                super.onBackPressed();
                return;
            }
        }

        super.onBackPressed();
    }

    private void transact(BaseFragment baseFragment, boolean shouldReplace) {
        if (getContainerId() == -1) {
            Logger.e("Specify container");
            return;
        }

        if (shouldReplace) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(getContainerId(), baseFragment).addToBackStack(null);
        transaction.commit();
    }

    public void addFragment(BaseFragment baseFragment) {
        baseFragment.animate(baseFragment);
        transact(baseFragment, false);
    }

    public void replaceFragment(BaseFragment baseFragment) {
        baseFragment.animate(baseFragment);
        transact(baseFragment, true);
    }

    @Override
    public void requestTransaction(BaseFragment baseFragment, boolean shouldReplace) {
        baseFragment.animate(baseFragment);
        transact(baseFragment, shouldReplace);
    }

    @Override
    public void onAttached(BaseFragment fragment) {
        currentFragment = fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
            getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    protected abstract int getContainerId();

    protected void animateFragment() {
    }

}
