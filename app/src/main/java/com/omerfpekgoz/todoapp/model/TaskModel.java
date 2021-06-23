package com.omerfpekgoz.todoapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.omerfpekgoz.todoapp.db.ImageTypeConverter;

import java.io.Serializable;
import java.util.Arrays;

@Entity
public class TaskModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int taskId;
    @ColumnInfo(name = "taskTitle")
    private String taskTitle;
    @ColumnInfo(name = "taskDescription")
    private String taskDescription;
    @ColumnInfo(name = "taskDate")
    private String taskDate;
    @ColumnInfo(name = "taskTime")
    private String taskTime;
    @ColumnInfo(name = "taskStatus")
    private boolean taskStatus;
    @TypeConverters(ImageTypeConverter.class)
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] taskImage;

    boolean expandable;


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public byte[] getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(byte[] taskImage) {
        this.taskImage = taskImage;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskDate='" + taskDate + '\'' +
                ", taskTime='" + taskTime + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskImage=" + Arrays.toString(taskImage) +
                ", expandable=" + expandable +
                '}';
    }
}
