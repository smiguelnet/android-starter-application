package net.smiguel.app.view.adapter.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import net.smiguel.app.R;

public abstract class ListItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    public ListItemTouchHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            getDefaultUIUtil().onSelected(getForegroundView(viewHolder));
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(c, recyclerView, getForegroundView(viewHolder), dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(getForegroundView(viewHolder));
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDraw(c, recyclerView, getForegroundView(viewHolder), dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.itemView.findViewById(R.id.customerLayoutView);
    }
}
