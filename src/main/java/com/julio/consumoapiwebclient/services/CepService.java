package com.julio.consumoapiwebclient.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.julio.consumoapiwebclient.model.Endereco;

import reactor.core.publisher.Mono;

@Service
public class CepService {

	@Autowired
	private WebClient webClient;

	public Endereco buscaEnderecoPorCep(String cep) {

		String uri = "http://viacep.com.br/ws/{cep}/json/";

		Mono<Endereco> monoEndereco = webClient
				.method(HttpMethod.GET)
				.uri(uri, cep)
				.retrieve()
				.bodyToMono(Endereco.class);

		Endereco endereco = monoEndereco.block();

		return endereco;
	}

}
