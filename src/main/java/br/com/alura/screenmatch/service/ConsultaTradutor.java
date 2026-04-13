package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaTradutor {

    public static String obterTraducao(String texto) {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();

        // O MyMemory pede o texto codificado para URL e o par de línguas (en|pt-br)
        String textoCodificado = URLEncoder.encode(texto, StandardCharsets.UTF_8);
        String langPair = URLEncoder.encode("en|pt-br", StandardCharsets.UTF_8);

        String url = "https://api.mymemory.translated.net/get?q=" + textoCodificado + "&langpair=" + langPair;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());

            // O texto traduzido fica em responseData -> translatedText
            return root.path("responseData").path("translatedText").asText();

        } catch (Exception e) {
            System.err.println("Erro na tradução: " + e.getMessage());
            return texto; // Se falhar, retorna o original em inglês
        }
    }
}