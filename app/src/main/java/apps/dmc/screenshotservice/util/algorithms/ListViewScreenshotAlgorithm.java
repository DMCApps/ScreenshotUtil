package apps.dmc.screenshotservice.util.algorithms;

import android.graphics.Bitmap;
import android.widget.ListView;

import apps.dmc.screenshotservice.util.algorithms.parent.ScreenshotAlgorithm;

/**
 * Created by DCarmo on 15-12-20.
 */
public class ListViewScreenshotAlgorithm extends ScreenshotAlgorithm<ListView> {

    @Override
    public Bitmap renderBitmap(ListView viewToScreenshot) {
        try {
            // TODO: ListViewScreenshotAlgorithm implementation
            return null;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
            throw outOfMemoryError;
        }
    }
}
