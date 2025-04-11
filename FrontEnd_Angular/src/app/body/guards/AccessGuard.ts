import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../service/AuthService';
import { User } from '../api/auth/User';
import {NAVIGATION, ROLE} from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class AccessGuard {
    user = {} as User;

    constructor(private authService: AuthService, private router: Router) {}

    canActivate(): Observable<boolean> | Promise<boolean> | boolean {
        if (this.authService.isAuthenticate()) {
            if (this.isUserAuthorized()) {
                return true;
            } else {
                this.router.navigate([NAVIGATION.ACCESS]);
                return false;
            }
        } else {
            this.router.navigate([NAVIGATION.LOGIN]);
            return false;
        }
    }

    private isUserAuthorized(): boolean {
        if(this.authService.getUserInfoFromToken().role.includes(ROLE.ADMIN)) {
            return true;
        } else {
            return false;
        }
    }
}
