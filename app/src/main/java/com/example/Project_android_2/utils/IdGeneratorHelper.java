package com.example.Project_android_2.utils;

public class IdGeneratorHelper {

    public static int generateAutomaticId() {
        // Có thể cải tiến hàm này để đảm bảo tính duy nhất của ID
        // Ví dụ: có thể thêm thời gian hiện tại vào ID để đảm bảo duy nhất
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
