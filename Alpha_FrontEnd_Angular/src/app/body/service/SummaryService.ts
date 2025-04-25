import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { API_URL } from 'src/app/constants/app.constants';

@Injectable({ providedIn: 'root' })
export class SummaryService {
  private readonly URL = `${API_URL.DEV}/cataract/patients`;

  constructor(private http: HttpClient) {}

  getSummary(patientId: number): Observable<any> {
    return this.http.get<any>(`${this.URL}/${patientId}/summary`);
  }

  saveSummary(patientId: number, payload: any): Observable<any> {
    return this.http.post<any>(`${this.URL}/${patientId}/summary`, payload);
  }
}
