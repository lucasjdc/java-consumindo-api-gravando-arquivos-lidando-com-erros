package br.com.alura.screenmatch.principal;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class PrincipalComBusca {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Scanner leitura = new Scanner(System.in);
		System.out.println("Digite um filme para busca: ");
		String busca = leitura.nextLine();
		
		Path path = Paths.get("config", "apikey.txt").toAbsolutePath();
		String apikey = Files.readString(path).trim();
		String url = "https://www.omdbapi.com/?t=" + busca + "&apikey=" + apikey;
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.build();
		
		HttpResponse<String> response = client
				.send(request, HttpResponse.BodyHandlers.ofString());		
		System.out.println(response.body());
	}
}