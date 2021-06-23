package com.omerfpekgoz.todoapp.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;


public class ImageTypeConverter {

    @TypeConverter
    public static byte[] convertImagetoByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @TypeConverter
    public static Bitmap convertByteArrayToImage(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}
