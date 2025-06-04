package com.jorami.eyeapp.config;

// Importation des classes nécessaires à l’audit et à la sécurité
import org.springframework.data.domain.AuditorAware; // Interface de Spring Data pour fournir des informations d’audit (qui a créé/modifié).
import org.springframework.security.core.Authentication; // Représente l’authentification de l’utilisateur.
import org.springframework.security.core.context.SecurityContextHolder; // Fournit l'accès au contexte de sécurité courant.
import org.springframework.stereotype.Service; // Marque la classe comme un bean de service géré par Spring.

import java.util.Optional; // Type de retour de l’auditeur, pouvant contenir ou non une valeur.

/**
 * Classe permettant à Spring JPA de savoir qui est l'utilisateur courant.
 * Utilisée pour remplir automatiquement les champs `createdBy` et `updatedBy` via @CreatedBy, @LastModifiedBy.
 */
@Service
public class AuditorAwareStringImpl implements AuditorAware<String> {

    /**
     * Méthode appelée automatiquement par Spring Data lorsqu'il doit enregistrer l'auteur d'une entité.
     * @return un Optional contenant le nom (email) de l'utilisateur authentifié ou vide si non connecté.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        // Récupère l'objet Authentication courant dans le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si aucun utilisateur n’est authentifié ou s’il s’agit d’un utilisateur anonyme
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // Retourne le nom d’utilisateur authentifié (souvent l’email)
        return Optional.of(authentication.getName());
    }
}
