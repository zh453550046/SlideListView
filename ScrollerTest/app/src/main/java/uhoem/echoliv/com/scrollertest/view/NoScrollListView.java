package uhoem.echoliv.com.scrollertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * Created by Administrator on 2015/11/10.
 */
public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int hegihtSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, hegihtSpec);
    }
}
