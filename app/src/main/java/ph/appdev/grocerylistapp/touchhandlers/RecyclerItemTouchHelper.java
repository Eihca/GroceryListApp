package ph.appdev.grocerylistapp.touchhandlers;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import ph.appdev.grocerylistapp.adapter.AdtnllistAdapter;
import ph.appdev.grocerylistapp.adapter.ChecklistAdapter;
import ph.appdev.grocerylistapp.adapter.RecyclerAdapter;

/**
 * Created by ravi on 29/09/17.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if(viewHolder instanceof RecyclerAdapter.MyViewHolder){
                final View foregroundView = ((RecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }else if(viewHolder instanceof ChecklistAdapter.MyViewHolder){
                final View foregroundView = ((ChecklistAdapter.MyViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }else {
                final View foregroundView = ((AdtnllistAdapter.MyViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }

        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof RecyclerAdapter.MyViewHolder){
            final View foregroundView = ((RecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }else if(viewHolder instanceof ChecklistAdapter.MyViewHolder){
            final View foregroundView = ((ChecklistAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }else {
            final View foregroundView = ((AdtnllistAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof RecyclerAdapter.MyViewHolder){
            final View foregroundView = ((RecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }else if(viewHolder instanceof ChecklistAdapter.MyViewHolder){
            final View foregroundView = ((ChecklistAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }else {
            final View foregroundView = ((AdtnllistAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof RecyclerAdapter.MyViewHolder){
            final View foregroundView = ((RecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }else if(viewHolder instanceof ChecklistAdapter.MyViewHolder){
            final View foregroundView = ((ChecklistAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }else {
            final View foregroundView = ((AdtnllistAdapter.MyViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
