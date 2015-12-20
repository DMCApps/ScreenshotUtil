package apps.dmc.screenshotservice.util.algorithms;

import android.graphics.Bitmap;
import android.widget.ScrollView;

import apps.dmc.screenshotservice.util.algorithms.parent.ScreenshotAlgorithm;

/**
 * Created by DCarmo on 15-12-20.
 */
public class ScrollViewScreenshotAlgorithm extends ScreenshotAlgorithm<ScrollView> {

    @Override
    public Bitmap renderBitmap(ScrollView viewToScreenshot) {
        int viewContentWidth = viewToScreenshot.getChildAt(0).getWidth();
        int viewContentHeight = viewToScreenshot.getChildAt(0).getHeight();

        return loadBitmapFromView(viewToScreenshot, viewContentWidth, viewContentHeight);
    }
}
