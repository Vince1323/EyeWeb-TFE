import { catchError, Observable, tap, throwError } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { API_URL } from "src/app/constants/app.constants";
import { Planning } from '../../body/api/operation/Planning';

@Injectable({
  providedIn: 'root',
})
export class PlanningService {
  private readonly API = `${API_URL.DEV}/planning`;

  constructor(private http: HttpClient) {}

  getAllByPatient(patientId: number): Observable<Planning[]> {
    return this.http.get<Planning[]>(`${this.API}/patient/${patientId}`);
  }

  getById(id: number): Observable<Planning> {
    return this.http.get<Planning>(`${this.API}/${id}`);
  }

  create(planning: Planning): Observable<Planning> {
    return this.http.post<Planning>(`${this.API}`, planning);
  }

  update(planning: Planning): Observable<Planning> {
    return this.http.put<Planning>(`${this.API}`, planning);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
