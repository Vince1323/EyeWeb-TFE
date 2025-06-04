package com.jorami.eyeapp.auth.model;
/**
 * Ce DTO représente la réponse retournée après une authentification réussie.
 * Il contient le token JWT que le client devra utiliser dans les requêtes futures.
 * Ce token est généralement retourné par l’endpoint POST `/auth/authenticate`.
 */

// --- LOMBOK ---
import lombok.AllArgsConstructor; // Génère un constructeur avec tous les champs
import lombok.Builder;            // Permet la création d’un objet via le pattern Builder (ex: AuthenticationResponse.builder().token(...).build())
import lombok.Data;               // Génère automatiquement les getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor;  // Génère un constructeur vide (utile pour Jackson ou tests)

@Data
// Fournit automatiquement les accesseurs (getters/setters), equals/hashCode, et toString

@Builder
// Permet de créer des objets facilement avec une syntaxe fluide : AuthenticationResponse.builder().token("xxx").build()

@AllArgsConstructor
// Crée un constructeur qui prend tous les champs en paramètres

@NoArgsConstructor
// Crée un constructeur vide (nécessaire pour la désérialisation JSON → Java dans certaines situations)

public class AuthenticationResponse {
    /**
     * Token JWT retourné après authentification réussie.
     * Ce token doit être stocké côté client (ex: dans localStorage) puis renvoyé dans l’en-tête Authorization.
     */
    private String token;
}
