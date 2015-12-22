package apps.dmc.screenshotservice.util.screenshot.algorithms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import apps.dmc.screenshotservice.util.screenshot.algorithms.parent.ScreenshotAlgorithm;

/**
 * Created by DCarmo on 15-12-20.
 */
public class ListViewScreenshotAlgorithm extends ScreenshotAlgorithm<ListView> {

    private class ItemToRender {
        View view;

        Point point;
        int width;
        int height;

        public Rect drawRect() {
            return new Rect(point.x,
                    point.y,
                    point.x + width,
                    point.y + height);
        }
    }

    @Override
    public Bitmap renderBitmap(ListView viewToScreenshot) {
        try {
            ListAdapter adapter = viewToScreenshot.getAdapter();
            int itemscount = adapter.getCount();

            // Prepare the views to be rendered into bitmaps.
            // DO NOT store the bitmaps here like others have done. This can be very memory
            // intensive. It's best to prepare the views and get their heights then render them
            // later and release them immediately after.
            List<ItemToRender> itemsToRender = new ArrayList<ItemToRender>(itemscount);
            ItemToRender lastRenderedItem = null;
            for (int i = 0; i < itemscount; i++) {
                View childView = adapter.getView(i, null, viewToScreenshot);
                childView.measure(View.MeasureSpec.makeMeasureSpec(viewToScreenshot.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                ItemToRender item = new ItemToRender();
                item.view = childView;

                int x = 0;
                int y = 0;

                if (lastRenderedItem != null) {
                    y = lastRenderedItem.drawRect().bottom;
                    if (lastRenderedItem.drawRect().width() == viewToScreenshot.getWidth()) {
                        x = lastRenderedItem.drawRect().right;
                    }
                }

                item.point = new Point(x, y);

                item.width = childView.getMeasuredWidth();
                item.height = childView.getMeasuredHeight();

                itemsToRender.add(item);
                lastRenderedItem = item;
            }

            // Create the resulting bitmap
            Bitmap bigBitmap = Bitmap.createBitmap(viewToScreenshot.getMeasuredWidth(), lastRenderedItem.drawRect().bottom, Bitmap.Config.ARGB_8888);

            // Prepare the canvas to draw on.
            Canvas bigCanvas = new Canvas(bigBitmap);

            Paint paint = new Paint();
            int iHeight = 0;

            // Draw the individual views to the canvas.
            for (int index = 0; index < itemsToRender.size(); index++) {
                ItemToRender itemToRender = itemsToRender.get(index);

                itemToRender.view.layout(0, 0, itemToRender.width, itemToRender.height);
                Bitmap bmp = loadBitmapFromViewDrawingCache(itemToRender.view);
                bigCanvas.drawBitmap(bmp, itemToRender.point.x, itemToRender.drawRect().top, paint);

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
