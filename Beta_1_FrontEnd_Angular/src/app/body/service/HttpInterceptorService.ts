import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpHeaders,
    HttpErrorResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './AuthService';
import {MessageService} from "primeng/api";

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {
    constructor(private authService: AuthService,
                private messageService: MessageService) {}

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        if (this.authService.isAuthenticate()) {
            // Si l'utilisateur est authentifié, on ajoute le token à l'en-tête
            const request = req.clone({
                headers: new HttpHeaders({
                    Authorization: this.authService.getToken(),
                }),
            });
            return next.handle(request).pipe(
                catchError((error: HttpErrorResponse) => {
                    return this.manageErrors(error);
                })
            );
        } else {
            //Si l'utilisateur n'est pas authentifié, on gère quand même les erreurs
            return next.handle(req).pipe(
                catchError((error: HttpErrorResponse) => {
                    return this.manageErrors(error)
                })
            );
        }
    }

    private manageErrors(error: HttpErrorResponse) {
        let newError = {
            status: error.status,
            summary: "ERROR",
            detail: error.error.message,
            severity: "error",
            error: error
        }
        console.log("IN HttpInterceptor", newError);
        if (error.error instanceof ErrorEvent) {
            // Erreur côté client
            newError.detail = "Une erreur inattendue est survenue. Veuillez nous excuser.";
        } else {
            if (!error.error.message) {
                switch (error.status) {
                    case 400:
                        newError.detail = "Bad Request. Veuillez réessayer plus tard.";
                        break;
                    case 401:
                        newError.detail = "Unauthorized. Veuillez réessayer plus tard.";
                        break;
                    case 402:
                        newError.detail = "Payment Required. Veuillez réessayer plus tard.";
                        break;
                    case 403:
                        newError.detail = "Forbidden. Veuillez réessayer plus tard.";
                        break;
                    case 404:
                        newError.detail = "Not Found. Veuillez réessayer plus tard.";
                        break;
                    case 405:
                        newError.detail = "Method Not Allowed. Veuillez réessayer plus tard.";
                        break;
                    case 406:
                        newError.detail = "Not Acceptable. Veuillez réessayer plus tard.";
                        break;
                    case 407:
                        newError.detail = "Proxy Authentication Required. Veuillez réessayer plus tard.";
                        break;
                    case 408:
                        newError.detail = "Request Time-out. Veuillez réessayer plus tard.";
                        break;
                    case 409:
                        newError.detail = "Conflict. Veuillez réessayer plus tard.";
                        break;
                    default:
                        newError.detail = "Impossible d'atteindre le serveur. Veuillez réessayer plus tard.";
                        break;
                }
            }
        }
        //Pour afficher les toasts d'erreurs.
        this.messageService.add({
            severity: newError.severity,
            summary: newError.summary,
            detail: newError.detail,
        });
        return throwError(() => newError);
    }
}
