package activity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIAPI {

    private static final String API_KEY = "sk-proj-PBSJ-UNbNNJiZFBwxdsVi3sM_Ep1TaQEZgdyU-t4llmr4ZquAvDhnSJy7oXRIpkps0XSCNieqKT3BlbkFJz_LroTwz6-L6xu8B2OwoxlUPbbmPBXuKO0chwmutAvfljkttpsN3JhAixV6T6-MqLoW5RB6Y";  // Replace with your key
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    public static String getResponse(String userMessage) {
        try {
            URL url = new URL(ENDPOINT);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();
            JSONObject userObj = new JSONObject();
            userObj.put("role", "user");
            userObj.put("content", userMessage);
            messages.put(userObj);
            jsonBody.put("messages", messages);

            OutputStream os = connection.getOutputStream();
            os.write(jsonBody.toString().getBytes());
            os.flush();
            os.close();

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content").trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I'm having trouble responding right now.";
        }
    }
}
