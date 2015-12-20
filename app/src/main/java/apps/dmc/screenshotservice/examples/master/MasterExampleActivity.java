package apps.dmc.screenshotservice.examples.master;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import apps.dmc.screenshotservice.R;

/**
 * Created by DCarmo on 15-12-20.
 */
public abstract class MasterExampleActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screenshot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_screenshot) {
            screenshotView();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public abstract void screenshotView();

}
