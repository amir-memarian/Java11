package http;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import player.Player;
import player.Players;
import player.ResponseStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpClientShould {
    @Test
    void call_a_web_server_and_request_information() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request=HttpRequest.newBuilder()
                .uri(new URI("https://extendsclass.com/mock/rest/hello/players"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        out.println(body);

        Gson gson = new Gson();
        Players players = gson.fromJson(body, Players.class);
        players.getPlayers().forEach(out::println);

        List<Player> topScorers = players.getPlayers().stream()
                .filter(player -> player.getGoal() > 210)
                .collect(Collectors.toUnmodifiableList());

        assertThat(topScorers).contains(new Player("Ali karimi",250));
    }

    @Test
    void send_a_request_to_add_a_player() throws URISyntaxException, IOException, InterruptedException {
        String player=new Gson().toJson(new Player("Farhad Majidi",275));
        out.println(player);
        HttpRequest request=HttpRequest.newBuilder()
                .uri(new URI("https://extendsclass.com/mock/rest/hello/players/add"))
                .header("Contect-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(player))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        out.println(body);

        Gson gson = new Gson();
        ResponseStatus responseStatus = gson.fromJson(body, ResponseStatus.class);

        Assertions.assertThat(responseStatus.getStatus()).isEqualTo("200");
    }

    @Test
    void do_asynch_calls() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        HttpRequest postRequest=HttpRequest.newBuilder()
                .uri(new URI("https://extendsclass.com/mock/rest/hello/players/add"))
                .header("Contect-Type","application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

//        HttpResponse<String> postResponse = HttpClient.newHttpClient()
//                .send(postRequest, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<HttpResponse<String>> postResponse = HttpClient.newHttpClient()
                .sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest GetRequest=HttpRequest.newBuilder()
                .uri(new URI("https://extendsclass.com/mock/rest/hello/players"))
                .GET()
                .build();

//        HttpResponse<String> getResponse = HttpClient.newHttpClient()
//                .send(GetRequest, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<HttpResponse<String>> getResponse = HttpClient.newHttpClient()
                .sendAsync(GetRequest, HttpResponse.BodyHandlers.ofString());

        out.println(postResponse.get());
        out.println(getResponse.get());
    }
}
