import { Injectable } from '@angular/core';
import {
    Router,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../service/AuthService';
import { NAVIGATION } from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class AuthGuard {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {
        if (
            this.authService.isAuthenticate() &&
            !this.authService.isTokenExpired()
        ) {
            return true;
        } else {
            this.authService.logout();
            this.router.navigate([NAVIGATION.LOGIN]);
            return false;
        }
    }
}
