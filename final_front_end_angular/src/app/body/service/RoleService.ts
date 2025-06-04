import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';



@Injectable({
  providedIn: 'root'
})
export class RoleService {
  getRoleFromToken(token: string): string | null {
    try {
      const decoded: any = jwtDecode(token);
      return decoded?.role ?? null;
    } catch (error) {
      console.error('Token decoding failed:', error);
      return null;
    }
  }

  hasRole(token: string, allowedRoles: string[]): boolean {
    const role = this.getRoleFromToken(token);
    return role ? allowedRoles.includes(role) : false;
  }
}
