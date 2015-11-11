package uhoem.echoliv.com.scrollertest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uhoem.echoliv.com.scrollertest.R;
import uhoem.echoliv.com.scrollertest.util.DisplayUtil;
import uhoem.echoliv.com.scrollertest.view.MyLayout;

/**
 * Created by Administrator on 2015/11/11.
 */
public class MyAdapter extends BaseAdapter {

    private Scroller scroller;
    private Context context;
    private List<String> list;
    private List<MyLayout> list_my_layout;

    public MyAdapter(Context context, List<String> list) {
        scroller = new Scroller(context);
        this.context = context;
        this.list = list;
        list_my_layout = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ll = (MyLayout) convertView.findViewById(R.id.ll);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            viewHolder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 70), ViewGroup.LayoutParams.MATCH_PARENT);
        viewHolder.tv_right.setLayoutParams(layoutParams);
        viewHolder.ll.setRightWidth(DisplayUtil.dip2px(context, 70));
        viewHolder.ll.setOnHideRightListener(new MyLayout.OnHideRightListener() {
            @Override
            public void onHiderRight(boolean remove) {
                if (remove) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        viewHolder.tv.setText(list.get(position));
        viewHolder.tv.setWidth(DisplayUtil.getWidth(context));
        viewHolder.tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ll.removeAfterhideRight(true);
            }
        });
        list_my_layout.add(viewHolder.ll);
        if (position == list.size() - 1) {
            for (MyLayout myLayout : list_my_layout) {
                myLayout.setList(list_my_layout);
            }
        }
        return convertView;
    }

    class ViewHolder {
        MyLayout ll;
        TextView tv, tv_right;
    }
}
