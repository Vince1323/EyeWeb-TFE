package com.jorami.eyeapp.service;

import reactor.core.publisher.Mono;


public interface FormulaApiService {

    Mono<String> callWithWebClient(Object body, String apiUrl, String formula);

    void callAndPrintBlocking(Object body, String apiUrl, String formula);
}
