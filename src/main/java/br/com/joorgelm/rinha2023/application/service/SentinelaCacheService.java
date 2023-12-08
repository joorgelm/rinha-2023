package br.com.joorgelm.rinha2023.application.service;

import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SentinelaCacheService {

    private final String instanceUrl = System.getenv("OTHER_API_URL");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public SentinelaCacheService() {}

    public boolean apelidoExists(String apelido) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(instanceUrl + "pessoas/apelidos/" + apelido + "?sibling=true"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == HttpStatus.OK.value();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pessoa> buscaPorTermo(String termo) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(instanceUrl + "/pessoas?t=" + termo + "&sibling=true"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            ObjectMapper objectMapper = new ObjectMapper();

            Pessoa[] pessoas = objectMapper.readValue(body, Pessoa[].class);

            return Arrays.stream(pessoas).toList();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Pessoa buscaPorId(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(instanceUrl + "/pessoas/" + id + "?sibling=true"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new ObjectNotFoundException(Optional.of(Pessoa.class.getName()), id);
            }

            if (response.statusCode() == HttpStatus.OK.value()) {
                String body = response.body();

                ObjectMapper objectMapper = new ObjectMapper();

                return objectMapper.readValue(body, Pessoa.class);
            }

            throw new ObjectNotFoundException(Pessoa.class.getName(), UUID.fromString(id));

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int contagem() {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(instanceUrl + "/contagem-pessoas" + "?sibling=true"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return Integer.parseInt(response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
