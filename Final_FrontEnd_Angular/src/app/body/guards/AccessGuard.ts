import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../service/AuthService';
import { NAVIGATION } from 'src/app/constants/app.constants';

@Injectable({
  providedIn: 'root',
})
export class AccessGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.authService.isAuthenticate()) {
      this.router.navigate([NAVIGATION.LOGIN]);
      return false;
    }

    const userInfo = this.authService.getUserInfoFromToken();
    const userRole = userInfo?.role;
    const allowedRoles = route.data['roles'] as string[]; // ['ADMIN', 'DOCTOR', 'SECRETARY', 'USER']

    if (!allowedRoles || allowedRoles.includes(userRole)) {
      return true;
    }

    this.router.navigate([NAVIGATION.ACCESS]);
    return false;
  }
}
