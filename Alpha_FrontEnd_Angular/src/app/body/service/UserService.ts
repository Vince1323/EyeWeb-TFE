import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { User } from '../api/auth/User';
import { API_URL } from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class UserService {
    createUser // tap(() => console.log(`🗑️ Utilisateur supprimé (ID: ${userId})`)),
        (userData: any) {
            throw new Error('Method not implemented.');
    }
    private readonly API_URL = API_URL.DEV;
    private readonly ENDPOINTS_USERS = '/users';
    private readonly httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
        }),
    };

    constructor(private httpClient: HttpClient) {}

    // Récupérer la liste de tous les utilisateurs
    getAllUsers(): Observable<User[]> {
        return this.httpClient.get<User[]>(`${this.API_URL}${this.ENDPOINTS_USERS}`, this.httpOptions).pipe(
           // tap((users) => console.log("📡 Liste des utilisateurs récupérée :", users)),
            catchError(this.handleError)
        );
    }

    // Récupérer un utilisateur par ID
    getUserById(userId: number): Observable<User> {
        return this.httpClient.get<User>(`${this.API_URL}${this.ENDPOINTS_USERS}/${userId}`, this.httpOptions).pipe(
           //tap((user) => console.log(`📡 Utilisateur récupéré (ID: ${userId}) :`, user)),
            catchError(this.handleError)
        );
    }

    // Récupérer l'utilisateur connecté
    getCurrentUser(): Observable<User> {
        return this.httpClient.get<User>(`${this.API_URL}${this.ENDPOINTS_USERS}/me`, this.httpOptions).pipe(
            tap((user) => {
               // console.log("📡 Réponse de l'API getCurrentUser :", user);
                if (!user || !user.id) {
                   // console.warn("⚠️ Aucune donnée utilisateur retournée !");
                }
            }),
            catchError(this.handleError)
        );
    }

    // Mettre à jour un utilisateur
    updateUser(user: User): Observable<User> {
        return this.httpClient.put<User>(
            `${this.API_URL}${this.ENDPOINTS_USERS}/${user.id}`,
            user,
            this.httpOptions
        ).pipe(
            //tap((updatedUser) => console.log(`✅ Utilisateur mis à jour (ID: ${user.id}) :`, updatedUser)),
            catchError(this.handleError)
        );
    }

    // Mettre à jour le statut vérifié d'un utilisateur
    updateVerifiedUser(user: User): Observable<User> {
        return this.httpClient.put<User>(
            `${this.API_URL}${this.ENDPOINTS_USERS}/verified/${user.id}`,
            user,
            this.httpOptions
        ).pipe(
          //  tap(() => console.log(`✅ Utilisateur vérifié mis à jour (ID: ${user.id})`)),
            catchError(this.handleError)
        );
    }

    // Supprimer un utilisateur
    deleteUser(userId: number): Observable<void> {
        return this.httpClient.delete<void>(
            `${this.API_URL}${this.ENDPOINTS_USERS}/${userId}`,
            this.httpOptions
        ).pipe(
           // tap(() => console.log(`🗑️ Utilisateur supprimé (ID: ${userId})`)),
            catchError(this.handleError)
        );
    }

    // Fonction de gestion des erreurs
    private handleError(error: any) {
        console.error("❌ Erreur API :", error);
        return throwError(() => new Error(error.message || "Une erreur s'est produite"));
    }

    

}
