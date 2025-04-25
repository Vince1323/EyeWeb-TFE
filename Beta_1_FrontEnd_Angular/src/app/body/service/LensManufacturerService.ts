import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LensManufacturer } from '../api/LensManufacturer';
import { API_URL } from 'src/app/constants/app.constants';

@Injectable({
    providedIn: 'root',
})
export class LensManufacturerService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINTS_LENSMANUFACTURERS = '/lens-manufacturers';

    constructor(private httpClient: HttpClient) {}

    //GET
    allLenseManufacturers(): Observable<LensManufacturer[]> {
        return this.httpClient.get<LensManufacturer[]>(
            `${this.API_URL}${this.ENDPOINTS_LENSMANUFACTURERS}`
        );
    }

    //PUT

    //POST

    //DELETE
}
