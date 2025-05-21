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
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;

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
				
		String json = response.body();
		System.out.println(json);
		
		Gson gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.create();
		
		TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);		
		
		System.out.println(meuTituloOmdb);
		
		Titulo meuTitulo = new Titulo(meuTituloOmdb);
		System.out.println("Título já convertido");
		System.out.println(meuTitulo);
		
		leitura.close();
	}
}