package electrify.electroint.com.application.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import electrify.electroint.com.application.R;

/**
 * Created by mayur kharche on 3/1/2016.
 */
public class DrawerAdapter extends BaseAdapter{

    private ArrayList<String> drawerList;
    private LayoutInflater drawerInf;

    public DrawerAdapter(Context c, ArrayList<String> list){
        drawerList = list;
        drawerInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return drawerList.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LinearLayout drawerLay = (LinearLayout)drawerInf.inflate
                (R.layout.drawer, parent, false);

        final ViewHolder holder = new ViewHolder();

        holder.itemName = (TextView)drawerLay.findViewById(R.id.dwr_title);
        holder.image = (ImageView)drawerLay.findViewById(R.id.dwr_icon);

        holder.itemName.setText(drawerList.get(position));

        switch(position) {
            case 0:
                holder.image.setBackgroundResource(R.drawable.statics_drawer_icon);
                break;
            case 1:
                holder.image.setBackgroundResource(R.drawable.setting_drawer_icon);
                break;
            case 2:
                holder.image.setBackgroundResource(R.drawable.contactus_drawer_icon);
                break;
            case 3:
                holder.image.setBackgroundResource(R.drawable.faqs_drawer_icon);
                break;
            case 4:
                holder.image.setBackgroundResource(R.drawable.logout_drawer_icon);
                break;
        }

        drawerLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                drawerLay.setBackgroundColor(Color.parseColor("#CCCCCC"));
                notifyDataSetChanged();

                if(position == 0) {
                    //Statics
                }
                else if(position == 1){
                    //Setting

                }
                else if(position == 2){
                    //FAQs

                }
                else if(position == 3){
                    //Logout

                }
                else if(position == 4) {
                    //

                }
            }
        });

        return drawerLay;
    }

    public class ViewHolder {
        TextView itemName;
        ImageView image;
    }
}
