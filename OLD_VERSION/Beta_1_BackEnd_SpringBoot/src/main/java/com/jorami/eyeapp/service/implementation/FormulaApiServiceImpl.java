package com.jorami.eyeapp.service.implementation;

import com.google.gson.Gson;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.formula.Pearl;
import com.jorami.eyeapp.service.FormulaApiService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FormulaApiServiceImpl implements FormulaApiService {

    private final WebClient webClient;
    @Value("${api.username}")
    private String username;
    @Value("${api.password}")
    private String password;

    private final static Logger LOGGER = Logger.getLogger(FormulaApiServiceImpl.class);

    public FormulaApiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<String> callWithWebClient(Object body, String apiUrl, String formula) {
        String jsonBody = new Gson().toJson(body);
        LOGGER.info(jsonBody);
        return webClient
                .post()
                .uri(apiUrl)
                .headers(headers -> {
                    headers.setBasicAuth(username, password);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(new Gson().toJson(body))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response ->
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            String formattedErrors;
                            formattedErrors = getError(formula, errorBody);
                            LOGGER.error("Error response: "+formattedErrors);
                            return Mono.error(new RuntimeException(formula+" error -> " + formattedErrors));
                        })
                )
                .bodyToMono(String.class);
    }

    @Override
    public void callAndPrintBlocking(Object body, String apiUrl, String formula) {
        String response = callWithWebClient(body, apiUrl, "Pearl").block();
        System.out.println("Réponse de l'API Pearl: " + response);
    }

    private String getError(String formula, String errorBody) {
        return switch (Enum.Formula.getKeyFromValue(formula)) {
            case PEARL -> Pearl.extractErrors(errorBody);
            case COOKE -> errorBody;
            default -> errorBody;
        };
    }
}
