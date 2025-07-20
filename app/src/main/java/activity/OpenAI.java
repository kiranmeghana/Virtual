package com.example.virtual;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenAIAPI {

    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public static void fetchResponse(String userMessage, ResponseCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private String error = null;

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("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");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "Bearer YOUR_OPENAI_API_KEY");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    String payload = "{\n" +
                            "  \"model\": \"gpt-3.5-turbo\",\n" +
                            "  \"messages\": [\n" +
                            "    {\"role\": \"system\", \"content\": \"You are a helpful mental health assistant.\"},\n" +
                            "    {\"role\": \"user\", \"content\": \"" + userMessage + "\"}\n" +
                            "  ]\n" +
                            "}";

                    OutputStream os = connection.getOutputStream();
                    os.write(payload.getBytes());
                    os.flush();
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Extract the assistant's message from the JSON
                        String json = response.toString();
                        String content = extractContentFromResponse(json);
                        return content;
                    } else {
                        error = "HTTP error code: " + responseCode;
                    }
                } catch (Exception e) {
                    error = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    callback.onResponse(result);
                } else {
                    callback.onError(error != null ? error : "Unknown error");
                }
            }
        }.execute();
    }

    // Extract assistant message from response
    private static String extractContentFromResponse(String json) {
        try {
            int contentIndex = json.indexOf("\"content\":\"");
            if (contentIndex != -1) {
                int start = contentIndex + 10;
                int end = json.indexOf("\"", start);
                return json.substring(start, end).replace("\\n", "\n");
            }
        } catch (Exception e) {
            return "Could not extract response.";
        }
        return "No response found.";
    }
}
