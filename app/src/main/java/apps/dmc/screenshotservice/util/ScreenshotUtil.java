package apps.dmc.screenshotservice.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.GridView;
import android.widget.ScrollView;

import apps.dmc.screenshotservice.util.algorithms.ViewScreenshotAlgorithm;
import apps.dmc.screenshotservice.util.algorithms.GridViewScreenshotAlgorithm;
import apps.dmc.screenshotservice.util.algorithms.ScrollViewScreenshotAlgorithm;
import apps.dmc.screenshotservice.util.algorithms.parent.ScreenshotAlgorithm;

/**
 * Created by DCarmo on 15-12-20.
 */
public class ScreenshotUtil {

    // =========================================
    // Public
    // =========================================

    /**
     * Helper method that will figure out the appropriate algorithm to use for basic android views.
     * To create and run a custom screenshot algorithm, create the algorithm extending from ScreenshotAlgorithm
     * and use the <code>ScreenshotView(View view, ScreenshotAlgorithm algorithm)</code> method.
     *
     * Supported Views:
     *  - ScrollView
     *  - GridView
     *  - Linear Layout
     *  - Relative Layout
     *
     * Upcoming Support:
     *  - ListView
     *
     * @param
     *      view -> View to render.
     * @return
     *      bitmap rendering of the given <code>view</code>
     */
    public static Bitmap ScreenshotView(View view) {
        ScreenshotAlgorithm<?> screenshotAlgorithm = null;

        if (view instanceof ScrollView) {
            screenshotAlgorithm = new ScrollViewScreenshotAlgorithm();
        }
        else if (view instanceof GridView) {
            screenshotAlgorithm = new GridViewScreenshotAlgorithm();
        }
        /*else if (view instanceof FreezeColumnHeaderTableView) {
            screenshotRenderer = new FreezeColumnHeaderTableViewScreenshot((FreezeColumnHeaderTableView)view);
        }*/
        // TODO: ListViewScreenshotAlgorithm
        else {
            screenshotAlgorithm = new ViewScreenshotAlgorithm();
        }

        return ScreenshotView(view, screenshotAlgorithm);
    }

    public static Bitmap ScreenshotView(View view, ScreenshotAlgorithm algorithm) {
        return algorithm.renderBitmap(view);
    }
}


    /*
    private static class FreezeColumnHeaderTableViewScreenshot extends ScreenshotAlgorithm<FreezeColumnHeaderTableView> {

        public FreezeColumnHeaderTableViewScreenshot(FreezeColumnHeaderTableView view) {
            super(view);
        }

        @Override
        public Bitmap renderBitmap() {
            try {
                FreezeColumnHeaderTableAdapter adapter = mViewToScreenshot.getAdapter();
                int colCount = adapter.getColCount();
                int rowCount = adapter.getRowCount();

                int totalTableHeight = 0;
                int totalTableWidth = 0;

                // TODO: I hate to expose this but a quick fix for now...
                int[] headerCellWidths = mViewToScreenshot.getHeaderCellWidths();

                // Prepare the views to be rendered into bitmaps.
                // DO NOT store the bitmaps here like others have done. This can be very memory
                // intensive. It's best to prepare the views and get their heights then render them
                // later and release them immediately after.
                View[][] viewsToRender = new View[rowCount][];
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    viewsToRender[rowIndex] = new View[colCount];
                    for (int colIndex = 0; colIndex < colCount; colIndex++) {
                        View childView = adapter.getView(colIndex, rowIndex, null, null);
                        childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                        viewsToRender[rowIndex][colIndex] = childView;

                        if (rowIndex == 0) {
                            totalTableWidth += childView.getMeasuredWidth();
                        }

                        if (colIndex == 0) {
                            totalTableHeight += childView.getMeasuredHeight();
                        }
                    }
                }

                // Create the resulting bitmap
                Bitmap bigBitmap = Bitmap.createBitmap(totalTableWidth, totalTableHeight, Bitmap.Config.ARGB_8888);

                // Prepare the canvas to draw on.
                Canvas bigCanvas = new Canvas(bigBitmap);

                Paint paint = new Paint();

                int iHeight = 0;

                int heightOfRow = 0;

                // Draw the individual views to the canvas.
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    int iWidth = 0;
                    for (int colIndex = 0; colIndex < colCount; colIndex++) {
                        View viewToRender = viewsToRender[rowIndex][colIndex];

                        // TODO: This is bad calculating this is should be found out from the sub elements measure but it's not returning the proper height.
                        if (rowIndex > 0 && heightOfRow == 0) {
                            heightOfRow = (totalTableHeight - iHeight) / (rowCount - 1);
                        }

                        // Issue with calling measure for the second time on something that is not a viewgroup.
                        // Also this is bad need to find a better way or something else to resent the measure.
                        if (!(viewToRender instanceof ViewGroup)) {
                            LinearLayout layout = new LinearLayout(adapter.getContext());
                            layout.setLayoutParams(new LinearLayout.LayoutParams(headerCellWidths[colIndex], heightOfRow));
                            layout.setBackgroundColor(adapter.getContext().getResources().getColor(android.R.color.darker_gray));

                            layout.addView(viewToRender);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(headerCellWidths[colIndex], heightOfRow);
                            viewToRender.setLayoutParams(params);

                            if (viewToRender instanceof TextView) {
                                ((TextView)viewToRender).setGravity(Gravity.CENTER);
                            }

                            viewToRender = layout;
                        }

                        // Views seem to lose their measurements sometimes. Remeasure.
                        viewToRender.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                        // TODO: Stop having to calculate this ...
                        if (rowIndex == 0) {
                            heightOfRow = viewToRender.getMeasuredHeight();
                        }

                        viewToRender.layout(0, 0, headerCellWidths[colIndex], heightOfRow);
                        Bitmap bmp = loadBitmapFromViewDrawingCache(viewToRender);
                        bigCanvas.drawBitmap(bmp, iWidth, iHeight, paint);

                        iWidth += bmp.getWidth();

                        if (colIndex == (colCount - 1)) {
                            iHeight += bmp.getHeight();
                            // Last column of the first row.
                            // zero this value and allow it to be calculated once by the math above
                            if (rowIndex == 0) {
                                heightOfRow = 0;
                            }
                        }

                        bmp.recycle();
                        bmp = null;
                    }
                }

                return bigBitmap;
            }
            catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
                throw outOfMemoryError;
            }
        }
    }
    */