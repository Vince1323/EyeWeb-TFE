// src/app/service/ExamService.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from 'src/app/constants/app.constants';
import { Exam } from '../api/exam/Exam';

@Injectable({
  providedIn: 'root'
})
export class ExamService {
  private apiUrl = `${API_URL.DEV}/exams`;

  constructor(private http: HttpClient) {}

  /**
   * Récupère un examen par son ID.
   */
  getExamById(id: number, orgaIds: number[]): Observable<Exam> {
    const params = new HttpParams().set('orgaIds', orgaIds.join(','));
    return this.http.get<Exam>(`${this.apiUrl}/${id}`, { params });
  }

  /**
   * Récupère tous les examens d’un patient, avec filtres optionnels (eyeSide et selectedOnly).
   */
  getExamsByPatient(patientId: number, orgaIds: number[]): Observable<Exam[]> {
  const url = `${this.apiUrl}/by-patient/${patientId}`;
  const params = new HttpParams().set('orgaIds', orgaIds.join(','));
  return this.http.get<Exam[]>(url, { params });
}


  /**
   * Crée un nouvel examen pour un patient.
   */
  createExam(patientId: number, orgaIds: number[], exam: Exam): Observable<Exam> {
    const params = new HttpParams().set('orgaIds', orgaIds.join(','));
    return this.http.post<Exam>(`${this.apiUrl}/patient/${patientId}`, exam, { params });
  }

  /**
   * Met à jour un examen existant.
   */
 updateExam(id: number, exam: Exam, orgaIds: number[]): Observable<Exam> {
  const url = `${this.apiUrl}/${id}`;
  const params = new HttpParams().set('orgaIds', orgaIds.join(','));
  return this.http.put<Exam>(url, exam, { params });
}

  /**
   * Supprime un examen.
   */
  deleteExam(id: number, orgaIds: number[]): Observable<void> {
  const url = `${this.apiUrl}/${id}`;
  const params = new HttpParams().set('orgaIds', orgaIds.join(','));
  return this.http.delete<void>(url, { params });
}
  addExam(exam: Exam, patientId: number, orgaIds: number[]): Observable<Exam> {
  const url = `${this.apiUrl}/patient/${patientId}`;
  const params = new HttpParams().set('orgaIds', orgaIds.join(','));
  return this.http.post<Exam>(url, exam, { params });
}






}
