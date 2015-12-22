package apps.dmc.screenshotservice.util.screenshot.algorithms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import apps.dmc.screenshotservice.util.screenshot.algorithms.parent.ScreenshotAlgorithm;

/**
 * Created by DCarmo on 15-12-20.
 */
public class GridViewScreenshotAlgorithm extends ScreenshotAlgorithm<GridView> {

    @Override
    public Bitmap renderBitmap(GridView viewToScreenshot) {
        try {
            // TODO: THIS ONLY WORKS IF THE ITEMS TAKE UP THE FULL WIDTH! DOES NOT DO SIDE BY SIDE GRID ITEMS.

            ListAdapter adapter = viewToScreenshot.getAdapter();
            int itemscount = adapter.getCount();

            int totalGridHeight = 0;

            // Prepare the views to be rendered into bitmaps.
            // DO NOT store the bitmaps here like others have done. This can be very memory
            // intensive. It's best to prepare the views and get their heights then render them
            // later and release them immediately after.
            List<View> viewsToRender = new ArrayList<View>();
            for (int i = 0; i < itemscount; i++) {
                View childView = adapter.getView(i, null, viewToScreenshot);
                childView.measure(View.MeasureSpec.makeMeasureSpec(viewToScreenshot.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalGridHeight += childView.getMeasuredHeight();

                viewsToRender.add(childView);
            }

            // Create the resulting bitmap
            Bitmap bigBitmap = Bitmap.createBitmap(viewToScreenshot.getMeasuredWidth(), totalGridHeight, Bitmap.Config.ARGB_8888);

            // Prepare the canvas to draw on.
            Canvas bigCanvas = new Canvas(bigBitmap);

            Paint paint = new Paint();
            int iHeight = 0;

            // Draw the individual views to the canvas.
            for (int index = 0; index < viewsToRender.size(); index++) {
                View viewToRender = viewsToRender.get(index);
                // Views seem to lose their measurements sometimes. Remeasure.
                viewToRender.measure(View.MeasureSpec.makeMeasureSpec(viewToScreenshot.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                viewToRender.layout(0, 0, viewToRender.getMeasuredWidth(), viewToRender.getMeasuredHeight());
                Bitmap bmp = loadBitmapFromViewDrawingCache(viewToRender);
                bigCanvas.drawBitmap(bmp, 0, iHeight, paint);
                iHeight += bmp.getHeight();

                bmp.recycle();
                bmp = null;
            }


            return bigBitmap;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
            throw outOfMemoryError;
        }
    }
}
