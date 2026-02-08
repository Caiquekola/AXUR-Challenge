import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class HtmlAnalyzer {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Hello world, i'm back!");
        System.out.println(Duration.ofSeconds(10));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("http://hiring.axreng.com/internship/example1.html"))
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.body());
        String[] lines = response.body().split("\\R");
        /*
            Varrer 
         */
        int tagsCount = 0, idxI = 0, idxE = 0;
        String text = "";
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].contains("</")&& lines[i].contains(">")) {
                                idxE = i;
                        }
                if(lines[i].contains("<") && lines[i].contains(">")) {
                        idxI = i;
                }
                System.out.println("Linha "+i+":"+lines[i]); //aw  3
        }
        System.out.println(response.body().substring(idxI,idxE).trim());
    }
}