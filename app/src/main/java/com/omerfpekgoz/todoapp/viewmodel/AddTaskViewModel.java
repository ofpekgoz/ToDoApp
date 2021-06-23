package com.omerfpekgoz.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.omerfpekgoz.todoapp.model.TaskModel;
import com.omerfpekgoz.todoapp.repository.ToDoRepository;

import java.util.List;

public class AddTaskViewModel extends AndroidViewModel {

    private ToDoRepository toDoRepository;
    private LiveData<List<TaskModel>> allTaskLiveData;

    public AddTaskViewModel(@NonNull Application application) {
        super(application);
        toDoRepository = new ToDoRepository(application);
    }

    public void insertTask(TaskModel taskModel) {
        toDoRepository.insertTask(taskModel);
    }

    public void deleteTask(TaskModel taskModel) {
        toDoRepository.deleteTask(taskModel);
    }

    public void updateTask(TaskModel taskModel) {
        toDoRepository.updateTask(taskModel);
    }

    public void changeTaskStatus(int taskId, boolean status) {
        toDoRepository.changeTaskStatus(taskId, status);
    }

}