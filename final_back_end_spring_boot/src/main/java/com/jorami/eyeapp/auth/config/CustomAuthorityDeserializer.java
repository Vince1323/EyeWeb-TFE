package com.jorami.eyeapp.auth.config;
/**
 * Utilisé pour désérialiser la liste des rôles/authorities depuis un JWT JSON en collection Java.
 * Cette classe permet de convertir les données JSON (présentes dans le token JWT) en objets Java de type `GrantedAuthority`.
 * Cela est nécessaire pour permettre à Spring Security de comprendre les rôles (ROLE_ADMIN, ROLE_USER, etc.) passés dans le JWT.
 */
// --- IMPORTS JACKSON : utilisés pour parser manuellement un objet JSON ---
import com.fasterxml.jackson.core.JsonParser; // Parseur de contenu JSON brut
import com.fasterxml.jackson.databind.DeserializationContext; // Contexte de désérialisation (géré automatiquement par Jackson)
import com.fasterxml.jackson.databind.JsonDeserializer; // Superclasse à étendre pour créer un désérialiseur personnalisé
import com.fasterxml.jackson.databind.JsonNode; // Représente un nœud JSON sous forme arborescente
import com.fasterxml.jackson.databind.ObjectMapper; // Outil Jackson principal pour transformer JSON ↔ Java

// --- IMPORTS SPRING SECURITY : représentent les rôles de sécurité dans l'application ---
import org.springframework.security.core.GrantedAuthority;  // Interface représentant une autorité dans Spring Security
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Implémentation simple prenant une chaîne comme "ROLE_ADMIN"

// --- IMPORTS JAVA UTILITAIRES ---
import java.io.IOException; // Gère les erreurs d'entrée/sortie lors de la lecture du flux JSON
import java.util.Iterator;  // Permet d’itérer sur une collection JSON
import java.util.LinkedList; // Implémentation d’une liste chaînée utilisée ici pour stocker les rôles extraits
import java.util.List; // Interface représentant une liste Java

/**
 * Cette classe est un désérialiseur personnalisé utilisé lors de la lecture d’un JWT contenant une liste d’autorisations.
 * Exemple de structure JSON désérialisée :
 * [
 *   { "authority": "ROLE_USER" },
 *   { "authority": "ROLE_ADMIN" }
 * ]
 * Jackson ne sait pas automatiquement comment convertir ceci en objets `GrantedAuthority` :
 * cette classe est donc nécessaire pour transformer chaque objet JSON en `SimpleGrantedAuthority`.
 */
public class CustomAuthorityDeserializer extends JsonDeserializer {

    /**
     * Méthode exécutée automatiquement par Jackson lorsqu’il désérialise un champ configuré avec ce désérialiseur.
     * Elle extrait une collection d’objets de type `{ "authority": "ROLE_XXX" }`
     * et retourne une liste de `SimpleGrantedAuthority` que Spring Security peut utiliser.
     *
     * @param jp      Le parseur JSON Jackson
     * @param context Le contexte global de désérialisation
     * @return        Une liste de GrantedAuthority à partir des données JSON
     * @throws IOException En cas de problème de lecture du flux JSON
     */
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        // On récupère l'ObjectMapper depuis le JsonParser (codec haut niveau)
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();

        // On lit le flux JSON complet en une structure arborescente (JsonNode)
        JsonNode jsonNode = mapper.readTree(jp);

        // On prépare la liste qui contiendra les rôles extraits
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();

        // On parcourt tous les objets JSON de la liste (chaque élément représente un rôle)
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            // On lit l'objet courant
            JsonNode next = elements.next();

            // On récupère le champ "authority" de l'objet JSON
            JsonNode authority = next.get("authority");

            // On transforme ce champ texte en une autorité reconnue par Spring Security
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
        }

        // On retourne la liste des rôles convertis
        return grantedAuthorities;
    }
}
