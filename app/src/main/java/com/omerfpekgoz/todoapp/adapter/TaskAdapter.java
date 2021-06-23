package com.omerfpekgoz.todoapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.omerfpekgoz.todoapp.R;
import com.omerfpekgoz.todoapp.databinding.CardviewTaskitemBinding;
import com.omerfpekgoz.todoapp.db.ImageTypeConverter;
import com.omerfpekgoz.todoapp.model.TaskModel;
import com.omerfpekgoz.todoapp.uı.AddTaskFragmentDirections;
import com.omerfpekgoz.todoapp.uı.TaskListFragmentDirections;
import com.omerfpekgoz.todoapp.viewmodel.AddTaskViewModel;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.cardViewHolder> {

    CardviewTaskitemBinding cardviewTaskitemBinding;
    Context mContext;
    List<TaskModel> taskModelList;
    private AddTaskViewModel addTaskViewModel;

    public TaskAdapter(Context mContext, List<TaskModel> taskModelList) {
        this.mContext = mContext;
        this.taskModelList = taskModelList;
        addTaskViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(AddTaskViewModel.class);
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        cardviewTaskitemBinding = CardviewTaskitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = cardviewTaskitemBinding.getRoot();
        mContext = view.getContext();

        return new cardViewHolder(cardviewTaskitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {

        TaskModel taskModel = taskModelList.get(position);
        if (taskModelList.size() != 0) {
            if (taskModel.getTaskStatus()) {
                cardviewTaskitemBinding.view.setBackgroundColor(mContext.getResources().getColor(R.color.viewbgcolor));
            } else {
                cardviewTaskitemBinding.view.setBackgroundColor(mContext.getResources().getColor(R.color.viewbgcolor2));
            }

        }

        holder.cardviewTaskitemBinding.textViewItemTaskTitle.setText(taskModel.getTaskTitle());
        holder.cardviewTaskitemBinding.textViewItemTaskDescription.setText(taskModel.getTaskDescription());
        holder.cardviewTaskitemBinding.textViewItemTaskDate.setText(taskModel.getTaskDate());
        holder.cardviewTaskitemBinding.textViewItemTaskTime.setText(taskModel.getTaskTime());
        holder.cardviewTaskitemBinding.imageViewTaskPhoto.setImageBitmap(ImageTypeConverter.convertByteArrayToImage(taskModel.getTaskImage()));


        boolean isExpandable = taskModelList.get(position).isExpandable();
        holder.cardviewTaskitemBinding.imageViewTaskPhoto.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        cardviewTaskitemBinding.imageView.setImageResource(R.drawable.icon_down);

        holder.cardviewTaskitemBinding.cardViewTaskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(taskModel);
                Navigation.findNavController(v).navigate(action);
            }
        });

        holder.cardviewTaskitemBinding.checkBoxItemTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cardviewTaskitemBinding.checkBoxItemTask.isChecked()) {
                    cardviewTaskitemBinding.cardViewTaskItem.setEnabled(false);
                    addTaskViewModel.changeTaskStatus(taskModel.getTaskId(), false);
                    cardviewTaskitemBinding.view.setBackgroundColor(mContext.getResources().getColor(R.color.viewbgcolor2));
                } else {
                    cardviewTaskitemBinding.cardViewTaskItem.setEnabled(true);
                    cardviewTaskitemBinding.view.setBackgroundColor(mContext.getResources().getColor(R.color.viewbgcolor));
                    addTaskViewModel.changeTaskStatus(taskModel.getTaskId(), true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    class cardViewHolder extends RecyclerView.ViewHolder {

        CardviewTaskitemBinding cardviewTaskitemBinding;

        public cardViewHolder(@NonNull CardviewTaskitemBinding cardviewTaskitemBinding) {
            super(cardviewTaskitemBinding.getRoot());
            this.cardviewTaskitemBinding = cardviewTaskitemBinding;

            cardviewTaskitemBinding.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskModel taskModel = taskModelList.get(getAdapterPosition());
                    taskModel.setExpandable(!taskModel.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                    cardviewTaskitemBinding.imageView.setImageResource(R.drawable.icon_up);
                }
            });
        }
    }

}
