package com.example.koreantime;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Messaging extends AsyncTask<Void, Void, String> {

    Context context;
    String url = "";
    ContentValues values;

    public Messaging() {
        this.context = context;
        this.url = url;
        this.values = values;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";

        try {
            sendCommonMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("http request", "NetworkTask >> onPostExecute() - result : " + result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    private static HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.
        httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Authorization", "key=AAAACFmVlVw:APA91bHeq8NLYlnHpnfkpmmxBsTbGbCUOM6J7WjZVnX1ytYNvG5-T5h5JSvAUegVbM0EWEnG8iD2FPYfKj4U4qfzKo98jFRf_X1QeWuVC0L7kaghUFrsR4iU8e4PzDeo7j_4ri_ba6f9");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        return httpURLConnection;
        // [END use_access_token]
    }

    private static void sendMessage(JSONObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(fcmMessage.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String response = inputstreamToString(connection.getInputStream());
            System.out.println("Message sent to Firebase for delivery, response:");
            System.out.println(response);
        } else {
            System.out.println("Unable to send message to Firebase:");
            String response = inputstreamToString(connection.getErrorStream());
            System.out.println(response);
        }
    }

    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    public static JSONObject buildNotificationMessage() {
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("to", "dOEz-lAyR16jU6fhp0KzLd:APA91bEPoC5zfntlAaE6ztey2sQwMF8FV24TL59phuLv_4zDFB4mKlBSVhogUqJdkaBWfyTb0R_9BDTd2VJ1-1dbm_NNi8x2Hn0GaEyyWNmV4W-Vz5wKL6gRbA647VmMPNrUJNyss0rs");
            jNotification.put("priority", "high");
            jdata.put("title", "Postman");
            jdata.put("message", "Hello, World!");
            jNotification.put("data",jdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jNotification;
    }

    public void sendCommonMessage() throws IOException {
        JSONObject notificationMessage = buildNotificationMessage();
        System.out.println("FCM request body for message using common notification object:");
        sendMessage(notificationMessage);
    }
}