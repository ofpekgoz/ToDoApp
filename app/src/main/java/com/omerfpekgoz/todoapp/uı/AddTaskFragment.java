package com.omerfpekgoz.todoapp.uı;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.omerfpekgoz.todoapp.R;
import com.omerfpekgoz.todoapp.databinding.AddTaskFragmentBinding;
import com.omerfpekgoz.todoapp.db.ImageTypeConverter;
import com.omerfpekgoz.todoapp.model.TaskModel;
import com.omerfpekgoz.todoapp.service.ToDoBroadcastReceiver;
import com.omerfpekgoz.todoapp.viewmodel.AddTaskViewModel;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends Fragment {

    private AddTaskViewModel mViewModel;
    private Context mContext;
    private AddTaskFragmentBinding addTaskFragmentBinding;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int year, month, dayOfMonth;
    private int hour, minute;
    private Intent intent;
    private Bitmap bitmap;
    private ToDoBroadcastReceiver toDoBroadcastReceiver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addTaskFragmentBinding = AddTaskFragmentBinding.inflate(getLayoutInflater());
        View view = addTaskFragmentBinding.getRoot();
        mContext = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        toDoBroadcastReceiver = new ToDoBroadcastReceiver();
        if (getArguments() != null) {

            AddTaskFragmentArgs args = AddTaskFragmentArgs.fromBundle(getArguments());
            TaskModel taskModel = args.getTask();

            if (taskModel != null) {
                addTaskFragmentBinding.editTextTaskTitle.setText(taskModel.getTaskTitle());
                addTaskFragmentBinding.editTextTaskDescription.setText(taskModel.getTaskDescription());
                addTaskFragmentBinding.buttonDate.setText(taskModel.getTaskDate());
                addTaskFragmentBinding.buttonTime.setText(taskModel.getTaskTime());
                addTaskFragmentBinding.imageViewBrowsePhoto.setImageBitmap(ImageTypeConverter.convertByteArrayToImage(taskModel.getTaskImage()));

                addTaskFragmentBinding.imageViewDeleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteTask(taskModel);
                        Toast.makeText(mContext, "Task Deleted", Toast.LENGTH_SHORT).show();
                        NavDirections action = AddTaskFragmentDirections.actionAddTaskFragmentToTaskListFragment();
                        Navigation.findNavController(view).navigate(action);
                    }
                });

                addTaskFragmentBinding.imageViewUpdateTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TaskModel taskModel1 = new TaskModel();
                        taskModel1.setTaskTitle(addTaskFragmentBinding.editTextTaskTitle.getText().toString().trim());
                        taskModel1.setTaskDescription(addTaskFragmentBinding.editTextTaskDescription.toString().trim());
                        taskModel1.setTaskDate(addTaskFragmentBinding.buttonDate.getText().toString().trim());
                        taskModel1.setTaskTime(addTaskFragmentBinding.buttonTime.getText().toString().trim());

                        if (bitmap == null) {
                            Snackbar.make(addTaskFragmentBinding.imageViewAddTask, R.string.fillblanks, Snackbar.LENGTH_SHORT).show();
                            return;
                        } else {
                            byte[] image = ImageTypeConverter.convertImagetoByteArray(bitmap);
                            taskModel1.setTaskImage(image);
                        }

                        updateTask(taskModel1);
                        Toast.makeText(mContext, "Task Update", Toast.LENGTH_SHORT).show();
                        NavDirections action = AddTaskFragmentDirections.actionAddTaskFragmentToTaskListFragment();
                        Navigation.findNavController(view).navigate(action);
                    }
                });

            } else {
            }
        } else {
        }

        addTaskFragmentBinding.imageViewAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }
        });

        addTaskFragmentBinding.buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerShow();
            }
        });

        addTaskFragmentBinding.buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerShow();
            }
        });

        addTaskFragmentBinding.buttonBrowseFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseFiles();
            }
        });
    }

    public void datePickerShow() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        addTaskFragmentBinding.buttonDate.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void timePickerShow() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectHour, int selectMinute) {
                hour = selectHour;
                minute = selectMinute;
                addTaskFragmentBinding.buttonTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, onTimeSetListener, hour, minute, true);
        timePickerDialog.show();
    }


    public void browseFiles() {
        intent = new Intent();
//        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);


        resultLauncher.launch("image/*");


    }

    ActivityResultLauncher<String> resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {

            if (result != null) {
                Uri uri = result;
                try {
                    bitmap = getBitmapFromUri(uri);
                    addTaskFragmentBinding.imageViewBrowsePhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                addTaskFragmentBinding.imageViewBrowsePhoto.setVisibility(View.VISIBLE);
                addTaskFragmentBinding.buttonBrowseFiles.setVisibility(View.INVISIBLE);
                addTaskFragmentBinding.imageViewBrowseIcon.setVisibility(View.INVISIBLE);
            }

        }
    });

    public void addTask(View view) {

        if (TextUtils.isEmpty(addTaskFragmentBinding.editTextTaskTitle.getText().toString())) {
            Snackbar.make(addTaskFragmentBinding.imageViewAddTask, R.string.fillblanks, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addTaskFragmentBinding.editTextTaskDescription.getText().toString())) {
            Snackbar.make(addTaskFragmentBinding.imageViewAddTask, R.string.fillblanks, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(addTaskFragmentBinding.buttonDate.getText().toString())) {
            Snackbar.make(addTaskFragmentBinding.imageViewAddTask, R.string.fillblanks, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(addTaskFragmentBinding.buttonTime.getText().toString())) {
            Snackbar.make(addTaskFragmentBinding.imageViewAddTask, R.string.fillblanks, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (bitmap == null) {
            Snackbar.make(addTaskFragmentBinding.imageViewAddTask, R.string.fillblanks, Snackbar.LENGTH_SHORT).show();
            return;
        }


        String title = addTaskFragmentBinding.editTextTaskTitle.getText().toString();
        String description = addTaskFragmentBinding.editTextTaskDescription.getText().toString();
        String date = addTaskFragmentBinding.buttonDate.getText().toString();
        String time = addTaskFragmentBinding.buttonTime.getText().toString();
        byte[] image = ImageTypeConverter.convertImagetoByteArray(bitmap);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskTitle(title);
        taskModel.setTaskDescription(description);
        taskModel.setTaskDate(date);
        taskModel.setTaskTime(time);
        taskModel.setTaskImage(image);
        taskModel.setTaskStatus(true);

        mViewModel.insertTask(taskModel);

        setAlarm();

        NavDirections action = AddTaskFragmentDirections.actionAddTaskFragmentToTaskListFragment();
        Navigation.findNavController(view).navigate(action);


    }

    public void updateTask(TaskModel taskModel) {
        mViewModel.updateTask(taskModel);
    }

    public void deleteTask(TaskModel taskModel) {
        mViewModel.deleteTask(taskModel);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = mContext.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void setAlarm() {
        toDoBroadcastReceiver.setOneTimeAlarm(mContext,
                addTaskFragmentBinding.editTextTaskTitle.getText().toString(),
                addTaskFragmentBinding.buttonDate.getText().toString(),
                addTaskFragmentBinding.buttonTime.getText().toString(),
                addTaskFragmentBinding.editTextTaskDescription.getText().toString());
    }

}