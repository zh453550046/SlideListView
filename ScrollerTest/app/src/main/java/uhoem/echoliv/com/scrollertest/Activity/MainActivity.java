package uhoem.echoliv.com.scrollertest.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uhoem.echoliv.com.scrollertest.R;
import uhoem.echoliv.com.scrollertest.adapter.MyAdapter;
import uhoem.echoliv.com.scrollertest.util.DisplayUtil;
import uhoem.echoliv.com.scrollertest.view.MyLayout;
import uhoem.echoliv.com.scrollertest.view.NoScrollListView;


public class MainActivity extends Activity {

    private ListView noScrollListView;
    private MyAdapter adapter;
    private List<String> list;
    private final String[] str = {
            "第一行",
            "第二行",
            "第三行",
            "第四行",
            "第五行",
            "第六行",
            "第七行",
            "第八行",
            "第九行",
            "第十行",
            "第十一行",
            "第十二行",
            "第十三行",
            "第十四行",
            "第十五行"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noScrollListView = (ListView) findViewById(R.id.lv);
        list = new ArrayList<>();
        list.addAll(Arrays.asList(str));
        adapter = new MyAdapter(this, list, noScrollListView);
        noScrollListView.setAdapter(adapter);
    }


}
