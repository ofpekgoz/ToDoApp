package com.omerfpekgoz.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.omerfpekgoz.todoapp.model.TaskModel;
import com.omerfpekgoz.todoapp.repository.ToDoRepository;

import java.util.List;

public class TaskListViewModel extends AndroidViewModel {

    private ToDoRepository toDoRepository;
    private LiveData<List<TaskModel>> allTaskLiveData;


    public TaskListViewModel(@NonNull Application application) {
        super(application);
        toDoRepository = new ToDoRepository(application);

    }

    public LiveData<List<TaskModel>> getAllTask() {
        allTaskLiveData = toDoRepository.getAllTask();
        return allTaskLiveData;
    }
}