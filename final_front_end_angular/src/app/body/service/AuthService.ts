import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthRequest } from '../api/auth/AuthRequest';
import { RegisterRequest } from '../api/auth/RegisterRequest';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { User } from '../api/auth/User';
import { SelectionService } from './SelectionService';
import { ResetPasswordRequest } from '../api/auth/ResetPasswordRequest';
import { API_URL, NAVIGATION } from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINT_AUTH = '/auth';

    constructor(
        private router: Router,
        private http: HttpClient,
        private selectionService: SelectionService
    ) {}

    authenticate(request: AuthRequest): Observable<any> {
        return this.http
            .post<any>(
                this.API_URL + this.ENDPOINT_AUTH + '/authenticate',
                request,
                {
                    headers: new HttpHeaders({
                        'Content-Type': 'application/json',
                    }),
                }
            )
            .pipe(
                map((resp) => {
                    sessionStorage.setItem('user', request.email);
                    sessionStorage.setItem('token', 'Bearer ' + resp.token);
                    return resp;
                })
            );
    }

    register(request: RegisterRequest): Observable<any> {
        return this.http.post<any>(
            this.API_URL + this.ENDPOINT_AUTH+ '/register',
            request,
            {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                }),
                responseType: 'json',
            }
        );
    }

    

    verifyEmail(code: String): Observable<any> {
        return this.http.put<any>(
            this.API_URL + this.ENDPOINT_AUTH + '/verify-email/' + code,
            {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                }),
                responseType: 'json',
            }
        );
    }

    regeneratesCodeValidationEmail(request: AuthRequest): Observable<any> {
        return this.http.put<any>(
            this.API_URL + this.ENDPOINT_AUTH + '/regenerates-code-email',
            request,
            {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                }),
                responseType: 'json',
            }
        );
    }

    sendCodeResetPassword(request: AuthRequest): Observable<any> {
        return this.http.post<any>(
            this.API_URL + this.ENDPOINT_AUTH + '/send-code-reset-password',
            request,
            {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                }),
                responseType: 'json',
            }
        );
    }

    logout() {
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('organization');
        this.selectionService.resetPatient();
        this.router.navigate([NAVIGATION.LOGIN]);
    }

    isAuthenticate() {
        if (sessionStorage.getItem('token') != null) {
            return true;
        } else {
            return false;
        }
    }

    getToken() {
        let token = sessionStorage.getItem('token') as string;
        return token;
    }

    getUserInfoFromToken(): any {
        let token = sessionStorage.getItem('token') as string;
        let decodedToken = jwtDecode(token) as User;
        return {
            id: decodedToken.id,
            firstname: decodedToken.firstname,
            lastname: decodedToken.lastname,
            email: decodedToken.email,
            role: decodedToken.role,
            organizations: decodedToken.organizations,
            hasReadTermsAndConditions: decodedToken.hasReadTermsAndConditions,
        };
    }

    isTokenExpired(): boolean {
        const token = sessionStorage.getItem('token');
        if (!token) return true;
        const decoded: any = jwtDecode(token.split(' ')[1]);
        const now = Date.now().valueOf() / 1000;
        return decoded.exp < now;
    }

    resetPassword(requestReset: ResetPasswordRequest): Observable<any> {
        return this.http.post<any>(
            this.API_URL + this.ENDPOINT_AUTH + '/forgot-password',
            requestReset,
            {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                }),
                responseType: 'json',
            }
        );
    }
}
