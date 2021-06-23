package com.omerfpekgoz.todoapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.omerfpekgoz.todoapp.model.TaskModel;

import java.util.List;

@Dao
public interface ToDoTaskDao {

    @Query("SELECT * FROM TaskModel")
    LiveData<List<TaskModel>> getAllTask();


    @Query("UPDATE TaskModel SET taskStatus=:status WHERE taskId=:taskId")
    void changeTaskStatus(int taskId, boolean status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskModel task);

    @Update
    void updateTask(TaskModel task);

    @Delete
    void deleteTask(TaskModel task);

}
