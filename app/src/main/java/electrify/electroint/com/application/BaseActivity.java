package electrify.electroint.com.application;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import electrify.electroint.com.application.adapters.DrawerAdapter;
import electrify.electroint.com.application.adapters.TabPagerAdapter;

public class BaseActivity extends FragmentActivity {

    DrawerLayout drawerLayout;
    ListView drawerList;
    ArrayList<String> contents;
    ViewPager viewPager;
    TabPagerAdapter tabPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.drawer_list);

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.list_header, null, false);
        drawerList.addHeaderView(listHeaderView);

        contents = new ArrayList<String>();
        contents.add("Statics");contents.add("Setting");contents.add("Contact Us");contents.add("FAQs");contents.add("Logout");

        drawerList.setAdapter(new DrawerAdapter(this, contents));

        viewPager = (ViewPager)findViewById(R.id.pager);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return Statics.newInstance("FirstFragment, Instance 1");
                case 1: return Statics.newInstance("SecondFragment, Instance 1");
                case 2: return Statics.newInstance("ThirdFragment, Instance 1");
                case 3: return Statics.newInstance("ThirdFragment, Instance 2");
                case 4: return Statics.newInstance("ThirdFragment, Instance 3");
                default: return Statics.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
