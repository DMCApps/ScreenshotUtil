package apps.dmc.screenshotservice.examples;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apps.dmc.screenshotservice.R;
import apps.dmc.screenshotservice.examples.master.MasterExampleActivity;
import apps.dmc.screenshotservice.util.screenshot.ScreenshotUtil;
import apps.dmc.screenshotservice.util.email.EmailUtil;

public class ListViewExampleActivity extends MasterExampleActivity {

    private static final int NUM_ITEMS_TO_CREATE = 50;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.list_view_example_lv);

        List<String> items = new ArrayList<String>(NUM_ITEMS_TO_CREATE);

        for (int itemNumber = 0; itemNumber < NUM_ITEMS_TO_CREATE; itemNumber++) {
            items.add(String.format("Item #%d", itemNumber));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                items);

        mListView.setAdapter(arrayAdapter);
    }

    @Override
    public void screenshotView() {
        Bitmap screenshotBmp = ScreenshotUtil.ScreenshotView(mListView);

        File cache = getApplicationContext().getExternalCacheDir();

        File file = new File(cache, "share_file.png");
        try {
            FileOutputStream out = new FileOutputStream(file);
            screenshotBmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            Log.e("ERROR", String.valueOf(e.getMessage()));
        }
        EmailUtil.createShareIntent("Subject", "Text", Uri.fromFile(file));
    }
}
