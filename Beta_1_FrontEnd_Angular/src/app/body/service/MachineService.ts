import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class MachineService {
    constructor(private http: HttpClient) {}

    getBiometers() {
        return this.http.get<any>('assets/body/data/biometers.json');
    }
}
