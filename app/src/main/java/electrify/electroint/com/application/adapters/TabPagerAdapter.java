package electrify.electroint.com.application.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Fragment f = null;

        switch(index) {

            case 0://tab = Category
                //f = new Statics();
                break;
            case 1://tab = All Songs
                //f = new Setting();
                break;
            case 2://tab = Albums
                //f = new ContanctUs();
                break;
            case 3://tab = Artists
                //f = new FAQs();
                break;
        }
        return f;
    }

    @Override
    public int getCount() {

        return 4;
    }
}