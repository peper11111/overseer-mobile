package pl.edu.pw.ee.overseer.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnectionUtility {
    private static final String HOST = "http://78.11.142.138:8080";

    public static JSONObject doPost(String path, JSONObject request) throws IOException, JSONException {
        URL url = new URL(HOST + path);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.connect();

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
        bufferedWriter.write(request.toString());
        bufferedWriter.flush();

        BufferedReader bufferedReader = null;
        if (httpURLConnection.getResponseCode() == 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        }

        String response = "";
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            response += line;
        }

        return new JSONObject(response);
    }
}