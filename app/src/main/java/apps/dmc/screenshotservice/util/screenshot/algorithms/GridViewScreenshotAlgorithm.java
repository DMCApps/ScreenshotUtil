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
            int itemsCount = adapter.getCount();

            int totalViewHeight = 0;

            // Prepare the views to be rendered into bitmaps.
            // DO NOT store the bitmaps here like others have done. This can be very memory
            // intensive. It's best to prepare the views and get their heights then render them
            // later and release them immediately after.
            for (int index = 0; index < itemsCount; index++) {
                View childView = adapter.getView(index, null, viewToScreenshot);
                childView.measure(View.MeasureSpec.makeMeasureSpec(viewToScreenshot.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalViewHeight += childView.getMeasuredHeight();
            }

            // Create the resulting bitmap
            Bitmap bigBitmap = Bitmap.createBitmap(viewToScreenshot.getMeasuredWidth(), totalViewHeight, Bitmap.Config.ARGB_8888);

            // Prepare the canvas to draw on.
            Canvas bigCanvas = new Canvas(bigBitmap);
            Paint bgPaint = new Paint();
            bgPaint.setColor(getBackgroundColorOfView(viewToScreenshot));
            bgPaint.setStyle(Paint.Style.FILL);
            bigCanvas.drawRect(0, 0, viewToScreenshot.getMeasuredWidth(), totalViewHeight, bgPaint);

            Paint paint = new Paint();
            int iHeight = 0;

            // Draw the individual views to the canvas.
            for (int index = 0; index < itemsCount; index++) {
                View childView = adapter.getView(index, null, viewToScreenshot);
                // Views seem to lose their measurements sometimes. Remeasure.
                childView.measure(View.MeasureSpec.makeMeasureSpec(viewToScreenshot.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
                Bitmap bmp = loadBitmapFromViewDrawingCache(childView);
                bigCanvas.drawBitmap(bmp, 0, iHeight, paint);
                iHeight += bmp.getHeight();

                bmp.recycle();
            }

            return bigBitmap;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
            throw outOfMemoryError;
        }
    }
}
