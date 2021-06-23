package com.omerfpekgoz.todoapp.uı;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omerfpekgoz.todoapp.R;
import com.omerfpekgoz.todoapp.adapter.TaskAdapter;
import com.omerfpekgoz.todoapp.databinding.TaskListFragmentBinding;
import com.omerfpekgoz.todoapp.model.TaskModel;
import com.omerfpekgoz.todoapp.viewmodel.TaskListViewModel;

import java.util.List;

public class TaskListFragment extends Fragment {

    private TaskListViewModel mViewModel;
    private Context mContext;
    private TaskListFragmentBinding taskListFragmentBinding;
    private TaskAdapter taskAdapter;

    public static TaskListFragment newInstance() {
        return new TaskListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        taskListFragmentBinding = TaskListFragmentBinding.inflate(getLayoutInflater());
        View view = taskListFragmentBinding.getRoot();
        mContext = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);

        taskListFragmentBinding.recylerViewTaskList.setLayoutManager(new LinearLayoutManager(mContext));
        taskListFragmentBinding.recylerViewTaskList.setHasFixedSize(true);


        mViewModel.getAllTask().observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> taskModels) {
                taskAdapter = new TaskAdapter(mContext, taskModels);
                taskListFragmentBinding.recylerViewTaskList.setAdapter(taskAdapter);
            }
        });

        taskListFragmentBinding.imageViewAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NavDirections action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(null);
                Navigation.findNavController(view).navigate(action);

            }
        });
    }


}