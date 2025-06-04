import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from 'src/app/constants/app.constants';
import {Calcul} from "../api/calcul/Calcul";
import { CalculMatrix } from '../api/calcul/CalculMatrix';
import { CalculSummary } from '../api/calcul/CalculSummary';

@Injectable({
    providedIn: 'root',
})
export class CalculService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINTS_CALCULS = '/calculs';

    constructor(private httpClient: HttpClient) { }

    //GET
    getAllCalculsByPatientId(
        patientId: number,
        organizationsId: number[],
        eyeSide: string,
    ): Observable<any[]> {
        let params = new HttpParams().set('eyeSide', eyeSide);
        return this.httpClient.get<any[]>(
            `${this.API_URL}${this.ENDPOINTS_CALCULS}/patients/${patientId}/organizations/${organizationsId.join(',')}`,
            { params }
        );
    }

    getAllCalculsByExamIdEyeSidePowerTrue(
        examId: number,
        organizationsId: number[],
        eyeSide: string,
    ): Observable<CalculSummary[]> {
        let params = new HttpParams().set('eyeSide', eyeSide);
        return this.httpClient.get<any[]>(
            `${this.API_URL}${this.ENDPOINTS_CALCULS}/exam/${examId}/organizations/${organizationsId.join(',')}/prec/list`,
            { params }
        );
    }

    getCalculById(
        id: number,
        organizationsId: number[]
    ): Observable<CalculMatrix> {
        return this.httpClient.get<CalculMatrix>(
            `${this.API_URL}${this.ENDPOINTS_CALCULS}/${id}/organizations/${organizationsId.join(',')}`
        );
    }

    //PUT
    selectPower(idCalcul: number, power: number, organizationsId: number[]) {
        let params = new HttpParams()
            .set('idCalcul', idCalcul)
            .append('power', power);
        return this.httpClient.put(
            `${this.API_URL}${this.ENDPOINTS_CALCULS}/organizationsId/${organizationsId.join(',')}/select-power`, params
        );
    }

    //POST
    injectionOnEachWebSite(calculs: Calcul[], organizationsId: number[]) {
        return this.httpClient.post(
            `${this.API_URL}${this.ENDPOINTS_CALCULS}/organizationsId/${organizationsId.join(',')}`, calculs
        );
    }

    //DELETE
}
