package com.omerfpekgoz.todoapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.omerfpekgoz.todoapp.model.TaskModel;

@Database(entities = {TaskModel.class}, version = 2, exportSchema = false)
@TypeConverters({ImageTypeConverter.class})
public abstract class ToDoDatabase extends RoomDatabase {

    public abstract ToDoTaskDao toDoTaskDao();

    private static ToDoDatabase toDoDatabase;

    public static synchronized ToDoDatabase getDb(Context context) {

        if (toDoDatabase == null) {
            toDoDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    ToDoDatabase.class, "ToDoDatabase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return toDoDatabase;

    }

}
