package com.omerfpekgoz.todoapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.omerfpekgoz.todoapp.db.ToDoDatabase;
import com.omerfpekgoz.todoapp.db.ToDoTaskDao;
import com.omerfpekgoz.todoapp.model.TaskModel;

import java.util.List;

public class ToDoRepository {

    private ToDoTaskDao toDoTaskDao;
    private LiveData<List<TaskModel>> allTaskModelLiveData;

    public ToDoRepository(Application application) {
        ToDoDatabase toDoDatabase = ToDoDatabase.getDb(application);
        toDoTaskDao = toDoDatabase.toDoTaskDao();
    }

    public LiveData<List<TaskModel>> getAllTask() {
        allTaskModelLiveData = toDoTaskDao.getAllTask();
        return allTaskModelLiveData;
    }

    public void insertTask(TaskModel taskModel) {
        toDoTaskDao.insertTask(taskModel);
    }

    public void updateTask(TaskModel taskModel) {
        toDoTaskDao.updateTask(taskModel);
    }

    public void deleteTask(TaskModel taskModel) {
        toDoTaskDao.deleteTask(taskModel);
    }

    public void changeTaskStatus(int taskId, boolean status) {
        toDoTaskDao.changeTaskStatus(taskId, status);
    }

}
