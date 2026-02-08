import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Stack;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        String url = "http://hiring.axreng.com/internship/example1.html";
        if (args.length != 0)
            url = args[0];

        StringBuilder content = new StringBuilder();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        HttpResponse<String> response = null;

        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();
            response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new URISyntaxException(url, "URL connection error");
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println("URL connection error");
        }

        String[] lines = response.body().split("\\R");
        String result = analyzeHtml(lines);
        System.out.println(result);
    }

    private static String analyzeHtml(String[] lines) {
        Stack<String> stack = new Stack<>();
        String text = null;
        int maxDepth = -1;
        String malformed = "malformed HTML";

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("<") && !line.endsWith(">")) {
                return malformed;
            }

            if (line.startsWith("</")) {
                if (line.length() < 4) {
                    return malformed;
                }
                String tag = line.substring(2, line.length() - 1);
                if (stack.isEmpty() || !stack.peek().equals(tag)) {
                    return malformed;
                }
                stack.pop();
            } else if (line.startsWith("<")) {
                if (line.length() < 3) {
                    return malformed;
                }
                String tag = line.substring(1, line.length() - 1);
                if (tag.endsWith("/")) {
                    return malformed;
                }
                stack.push(tag);
            } else {
                int depth = stack.size();
                if (depth > maxDepth) {
                    maxDepth = depth;
                    text = line;
                }
            }
        }

        if (text != null) {
            return text;
        }
        // Stack is not empty
        return malformed;

    }
}