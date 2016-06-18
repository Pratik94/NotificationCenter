package com.crackerjack.notificationcenter.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crackerjack.notificationcenter.R;
import com.crackerjack.notificationcenter.base.AbstractFragmentActivity;

import butterknife.Bind;

public class MainActivity extends AbstractFragmentActivity {

    private Toolbar tbMain;

    @Bind(R.id.tvActivityTitle)
    TextView tvActivityTitle;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    public void initView(){

        setUpTolbar();

        setUpViewPager();

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/GothamRoundedBook.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);

        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {

            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {

                View tabViewChild = vgTab.getChildAt(i);

                if (tabViewChild instanceof TextView) {

//                    ((TextView) tabViewChild).setTypeface(tf);
                    ((TextView) tabViewChild).setAllCaps(false);
                }
            }
        }
    }

    public void setUpTolbar(){
        baseToolbar.setVisibility(View.GONE);
        tvActivityTitle.setText(R.string.app_name);
        tbMain = (Toolbar) findViewById(R.id.tbMain);
        tbMain.setTitle("");
        setSupportActionBar(tbMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    public void setUpViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NewJobFragment.newInstance(),"New Jobs");
        adapter.addFragment(QuotesFragment.newInstance(), "Quotes");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContainerId() {
        return 0;
    }

}
