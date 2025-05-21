package br.com.alura.screenmatch.principal;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import br.com.alura.screenmatch.excecao.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;

public class PrincipalComBusca {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Scanner leitura = new Scanner(System.in); // Objeto para leitura do terminal
		String busca = "";
		List<Titulo> listaTitulos = new ArrayList<>(); // Lista para armazenar os filmes convertidos

		// Configurando o Gson para converter JSON em objetos Java
		Gson gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting()
				.create();
		
		// Loop principal para buscar filmes até que o usuário digite "sair"
		while (!busca.equalsIgnoreCase("sair")) {
			System.out.println("Digite um filme para busca: ");
			busca = leitura.nextLine(); // Lê o nome do filme digitado pelo usuário
			
			if (busca.equalsIgnoreCase("sair")) {
				break;
			}			
			
			// Lê a API key do arquivo "config/apikey.txt"
			Path path = Paths.get("config", "apikey.txt").toAbsolutePath();
			String apikey = Files.readString(path).trim(); // Remove espaços e quebras de linha
			String url = "https://www.omdbapi.com/?t=" + busca.replace(" ", "+") + "&apikey=" + apikey;
			
			try {
				// Cria cliente HTTP e prepara requisição
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create(url))
						.build();
				
				// Envia requisição e obtém resposta como string
				HttpResponse<String> response = client
						.send(request, HttpResponse.BodyHandlers.ofString());		
						
				String json = response.body(); // JSON retornado pela API
				System.out.println(json); // Exibe JSON no terminal
				
				// Converte JSON em objeto TituloOmdb
				TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
				System.out.println(meuTituloOmdb);
				
				// Converte o TituloOmdb em Titulo (modelo próprio do app)
				Titulo meuTitulo = new Titulo(meuTituloOmdb);
				System.out.println("Título já convertido");
				System.out.println(meuTitulo);
				
				// Adiciona o título convertido à lista
				listaTitulos.add(meuTitulo);
				
			} catch (NumberFormatException e) {
				System.out.println("Aconteceu um erro: ");
				System.out.println(e.getMessage());
			} catch (IllegalArgumentException e) {
				System.out.println("Algum erro de argumento na busca, verifique o endereço.");			
			} catch (ErroDeConversaoDeAnoException e) {
				System.out.println(e.getMessage());
			}			
		}
		
		// Exibe todos os títulos convertidos
		System.out.println(listaTitulos);

		// Salva os títulos no arquivo JSON "filmes.json"
		FileWriter escrita = new FileWriter("filmes.json");
		escrita.write(gson.toJson(listaTitulos));
		escrita.close();

		System.out.println("O programa finalizou corretamente!");		
		leitura.close();
	}
}
