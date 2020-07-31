package com.ssw.linkedinmanager.common;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer {
    public static String serialize(Serializable object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
            return "";
        }
    }

    public static Object deserialize(String data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object result = objectInputStream.readObject();
            objectInputStream.close();
            return result;
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
            return null;
        }
    }
}
