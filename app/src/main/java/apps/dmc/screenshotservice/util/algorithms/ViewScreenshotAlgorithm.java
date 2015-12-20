package apps.dmc.screenshotservice.util.algorithms;

import android.graphics.Bitmap;
import android.view.View;

import apps.dmc.screenshotservice.util.algorithms.parent.ScreenshotAlgorithm;

/**
 * Created by DCarmo on 15-12-20.
 */
public class ViewScreenshotAlgorithm extends ScreenshotAlgorithm<View> {

    @Override
    public Bitmap renderBitmap(View viewToScreenshot) {
        return loadBitmapFromViewDrawingCache(viewToScreenshot);
    }
}
