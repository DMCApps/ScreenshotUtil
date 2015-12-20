package apps.dmc.screenshotservice.examples;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import apps.dmc.screenshotservice.R;

public class MainActivity extends AppCompatActivity {

    private enum ExampleItemPositions {
        RelativeLayout,
        LinearLayout,
        ScrollView,
        ListView,
        GridView
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView examplesList = (ListView)findViewById(R.id.main_activity_lv_examples);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.example_titles));

        examplesList.setAdapter(arrayAdapter);

        examplesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == ExampleItemPositions.ListView.ordinal()) {
                    Intent intent = new Intent(MainActivity.this, ListViewExampleActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
