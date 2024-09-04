package com.cd.statussaver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallBack extends ItemTouchHelper.Callback {
    private int backgroundColor = Color.parseColor("#b80f0a");
    private Drawable deleteDrawable;
    private int intrinsicHeight;
    private int intrinsicWidth;
    private ColorDrawable mBackground = new ColorDrawable();
    private Paint mClearPaint;
    private Context mContext;

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    }

    public SwipeToDeleteCallBack(Context context) {
        mContext = context;
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, 4);
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
        super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        View view = viewHolder.itemView;
        int height = view.getHeight();
        if (f == 0.0f && !z) {
            clearCanvas(canvas, Float.valueOf(((float) view.getRight()) + f), Float.valueOf((float) view.getTop()), Float.valueOf((float) view.getRight()), Float.valueOf((float) view.getBottom()));
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
            return;
        }
        mBackground.setColor(backgroundColor);
        mBackground.setBounds(view.getRight() + ((int) f), view.getTop(), view.getRight(), view.getBottom());
        mBackground.draw(canvas);
        int top = view.getTop();
        int i3 = top + ((height - intrinsicHeight) / 2);
        int i4 = (height - intrinsicHeight) / 2;
        deleteDrawable.setBounds((view.getRight() - i4) - intrinsicWidth, i3, view.getRight() - i4, intrinsicHeight + i3);
        deleteDrawable.draw(canvas);
    }



    private void clearCanvas(Canvas canvas, Float f, Float f2, Float f3, Float f4) {
        canvas.drawRect(f.floatValue(), f2.floatValue(), f3.floatValue(), f4.floatValue(), mClearPaint);
    }
}

