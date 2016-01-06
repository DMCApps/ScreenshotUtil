package apps.dmc.screenshotservice.examples;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apps.dmc.screenshotservice.R;
import apps.dmc.screenshotservice.examples.master.MasterExampleActivity;
import apps.dmc.screenshotservice.util.email.EmailUtil;
import apps.dmc.screenshotservice.util.screenshot.ScreenshotUtil;

public class GridViewExampleActivity extends MasterExampleActivity {

    private static final int NUM_ITEMS_TO_CREATE = 50;

    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_example_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGridView = (GridView)findViewById(R.id.grid_view_example_gv);

        List<String> items = new ArrayList<String>(NUM_ITEMS_TO_CREATE);

        for (int itemNumber = 0; itemNumber < NUM_ITEMS_TO_CREATE; itemNumber++) {
            items.add(String.format("Item #%d", itemNumber));
        }

        mGridView.setAdapter(new GridAdapter(items));
    }

    // Assume it's known
    private static final int ROW_ITEMS = 3;

    public static final class GridAdapter extends BaseAdapter {

        final ArrayList<String> mItems;
        final int mCount;

        /**
         * Default constructor
         * @param items to fill data to
         */
        private GridAdapter(final List<String> items) {

            mCount = items.size();
            mItems = new ArrayList<String>(items);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Object getItem(final int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            final TextView text = (TextView) view.findViewById(android.R.id.text1);

            text.setText(mItems.get(position));

            return view;
        }
    }

    @Override
    public void screenshotView() {
        Bitmap screenshotBmp = ScreenshotUtil.ScreenshotView(mGridView);

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
        Intent emailItent = EmailUtil.createShareIntent("Screenshot Utility - Grid View", "Check out this screenshot I took!", Uri.fromFile(file));
        startActivity(Intent.createChooser(emailItent, "Share Screenshot!"));
    }
}
