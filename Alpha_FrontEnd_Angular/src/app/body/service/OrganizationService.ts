import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Organization } from '../api/Organization';
import { Observable, catchError, throwError } from 'rxjs';
import { API_URL } from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class OrganizationService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINT_ORGANIZATIONS = '/organizations';

    constructor(private httpClient: HttpClient) {}

    //GET
    getOrganizationsByUserId(userId: number): Observable<Organization[]> {
        return this.httpClient
            .get<Organization[]>(
                this.API_URL + this.ENDPOINT_ORGANIZATIONS + '/users/' + userId
            );
    }
}
