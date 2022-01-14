package com.julio.consumoapiwebclient.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.julio.consumoapiwebclient.model.Post;

import reactor.core.publisher.Mono;

@Service
public class PostService {
	

	private final String URI_BASE = "https://jsonplaceholder.typicode.com/posts";
	
	@Autowired
	private WebClient webClient;

	public List<Post> findAll() {
		Mono<Post[]> fluxPosts = webClient
			.method(HttpMethod.GET)
			.uri(URI_BASE)
			.retrieve()
			.bodyToMono(Post[].class);
		
		Post[] posts = fluxPosts.block();
		
		return Arrays.asList(posts);
	}

	public Post findById(String id) {
		
		Mono<Post> monoPost = webClient
			.method(HttpMethod.GET)
			.uri(URI_BASE + "/{id}", id)
			.retrieve()
			.bodyToMono(Post.class);
		
		Post post = monoPost.block();
		
		return post;
	}

	public Post save(Post post) {
		
		// Forma 1 post
		Mono<Post> monoPost1 = webClient
			.post()
			.uri(URI_BASE)
			.body(BodyInserters.fromValue(post))
			.retrieve()
			.bodyToMono(Post.class);
		
		// Forma 2 method
		Mono<Post> monoPost2 = webClient
				.method(HttpMethod.POST)
				.uri(URI_BASE)
				.body(BodyInserters.fromValue(post))
				.retrieve()
				.bodyToMono(Post.class);
		
		// DESTA MANEIRA RODA DE FORMA NAO REATIVA, NAO TERIA PQ USAR		
//		Post post1 = monoPost1.block();
//		Post post2 = monoPost2.block();
		
		// DESTA MANEIRA RODA DE FORMA REATIVA
		post = Mono.zip(monoPost1, monoPost2).map(tuple -> {
			return tuple.getT1();
		}).block();
		
		return post;
	}

	public Post edit(Post post) {
		
		String uri = URI_BASE + "/{id}";
		
		// Forma 1 post
		Mono<Post> monoPost1 = webClient
			.put()
			.uri(uri, post.getId())
			.body(BodyInserters.fromValue(post))
			.retrieve()
			.bodyToMono(Post.class);
		
		// Forma 2 method
		Mono<Post> monoPost2 = webClient
				.method(HttpMethod.PUT)
				.uri(uri, post.getId())
				.body(BodyInserters.fromValue(post))
				.retrieve()
				.bodyToMono(Post.class);
		
		// DESTA MANEIRA RODA DE FORMA REATIVA
		post = Mono.zip(monoPost1, monoPost2).map(tuple -> {
			return tuple.getT1();
		}).block();
		
		return post;
		
	}

	public void delete(String id) {

		String uri = URI_BASE + "/{id}";
		
		// Forma 1 post
		webClient
			.delete()
			.uri(uri, id)
			.retrieve()
			.bodyToMono(Post.class);
		
		// Forma 2 method
		webClient
			.method(HttpMethod.DELETE)
			.uri(uri, id)
			.retrieve()
			.bodyToMono(Post.class);
		
	}

}
