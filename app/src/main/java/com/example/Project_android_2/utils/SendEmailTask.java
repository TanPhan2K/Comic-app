package com.example.Project_android_2.utils;
import android.os.AsyncTask;

public class SendEmailTask extends AsyncTask<Void, Void, Void> {
    private String recipientEmail;
    private int otpCode;
    private String messageContent;

    public SendEmailTask(String recipientEmail, int otpCode,String messageContent) {
        this.recipientEmail = recipientEmail;
        this.otpCode = otpCode;
        this.messageContent = messageContent;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Gửi email ở đây
        EmailSender.sendOTP(recipientEmail, otpCode,messageContent);
        return null;
    }
}