import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class LensService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINTS_LENSES = '/lenses';

    constructor(private httpClient: HttpClient) {}

    //GET
    allLensesByManufacturer(id:number): Observable<any> {
        return this.httpClient.get<any>(
            `${this.API_URL}${this.ENDPOINTS_LENSES}/manufacturers/${id}`
        );
    }

    //PUT

    //POST

    //DELETE
}
