package apps.dmc.screenshotservice.util.algorithms.parent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by DCarmo on 15-12-20.
 */

public abstract class ScreenshotAlgorithm<T> {

    /**
     * This method will take a screenshot of the provided view
     *
     * @param
     *      view - The view to take a screenshot of
     * @param
     *      width - Width of the expected screenshot.
     * @param
     *      height - Height of the expected screenshot.
     * @return
     *      The screenshot of the view as a bitmap.
     */
    protected Bitmap loadBitmapFromView(View view, int width, int height) {
        Bitmap bmp = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
        view.draw(canvas);
        return bmp;
    }

    /**
     * Grabs the screenshot of the view from the views drawing cache.
     *
     * @param
     *      view - the view to get the drawing cache from
     * @return
     *      The resulting bitmap from the view's drawing cache
     */
    protected Bitmap loadBitmapFromViewDrawingCache(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return bmp;
    }

    /**
     * Renders the complete view passed as a bitmap.
     *
     * @return
     *      The resulting bitmap from the view.
     */
    public abstract Bitmap renderBitmap(T viewToScreenshot);
}
