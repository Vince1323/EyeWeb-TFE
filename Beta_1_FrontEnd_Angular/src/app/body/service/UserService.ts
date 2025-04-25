import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { User } from '../api/auth/User';
import { API_URL } from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class UserService {

    //  URL principale de l'API
    private readonly API_URL = API_URL.DEV;

    //  Endpoint pour les utilisateurs
    private readonly ENDPOINTS_USERS = '/users';

    //  Options HTTP par défaut
    private readonly httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
        }),
    };

    constructor(private httpClient: HttpClient) {}

    //  Récupérer la liste de tous les utilisateurs
    getAllUsers(): Observable<User[]> {
        return this.httpClient.get<User[]>(
            `${this.API_URL}${this.ENDPOINTS_USERS}`, this.httpOptions
        ).pipe(
            catchError(this.handleError)
        );
    }

    //  Récupérer un utilisateur spécifique par son ID
    getUserById(userId: number): Observable<User> {
        return this.httpClient.get<User>(
            `${this.API_URL}${this.ENDPOINTS_USERS}/${userId}`, this.httpOptions
        ).pipe(
            catchError(this.handleError)
        );
    }

    //  Récupérer les informations de l'utilisateur actuellement connecté
    getCurrentUser(): Observable<User> {
        return this.httpClient.get<User>(
            `${this.API_URL}${this.ENDPOINTS_USERS}/me`, this.httpOptions
        ).pipe(
            catchError(this.handleError)
        );
    }

    //  Mettre à jour toutes les informations d’un utilisateur (y compris le rôle ou vérification)
    updateVerifiedUser(user: User): Observable<User> {
        return this.httpClient.put<User>(
            `${this.API_URL}${this.ENDPOINTS_USERS}/verified/${user.id}`,
            user,
            this.httpOptions
        ).pipe(
            catchError(this.handleError)
        );
    }
    
    

    //  Supprimer un utilisateur
    deleteUser(userId: number): Observable<void> {
        return this.httpClient.delete<void>(
            `${this.API_URL}${this.ENDPOINTS_USERS}/${userId}`, this.httpOptions
        ).pipe(
            catchError(this.handleError)
        );
    }

    //  Gestion centralisée des erreurs API
    private handleError(error: any) {
        console.error("❌ Erreur API :", error);
        return throwError(() => new Error(error.message || "Une erreur s'est produite"));
    }

    //  Création d'utilisateur à implémenter
    createUser(userData: any) {
        throw new Error('Method not implemented.');
    }
    
    //  Mettre à jour le rôle d’un utilisateur
    updateUserRole(userId: number, roleName: string): Observable<User> {
        return this.httpClient.put<User>(
          `${this.API_URL}${this.ENDPOINTS_USERS}/role/${userId}`,
          { role: roleName },
          this.httpOptions
        ).pipe(
          catchError(this.handleError)
        );
      }
      
    
}
