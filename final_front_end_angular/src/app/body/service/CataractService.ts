import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { API_URL } from "src/app/constants/app.constants";
import { Exam } from "../api/exam/Exam";
import { ExamSummary } from "../api/exam/ExamSummary";

@Injectable({
    providedIn: "root",
})
export class CataractService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINTS_CATARACT = "/cataract";

    constructor(private httpClient: HttpClient) {}

    /**
     * GET /{id}/organizations/{organizationsId}
     * Récupérer un examen par ID
     */
    getExamById(examId: number, organizationsId: number[]): Observable<Exam> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/${examId}/organizations/${organizationsId.join(",")}`;
        return this.httpClient.get<Exam>(url);
    }

    /**
     * GET /patients/{patientId}/organizations/{organizationsId}
     * Récupérer tous les examens par patient ID
     */
    getAllExamsByPatientId(patientId: number, organizationsId: number[], eyeSide: string, isSelected?: boolean): Observable<Exam[]> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/patients/${patientId}/organizations/${organizationsId.join(",")}`;
        let params = new HttpParams().set("eyeSide", eyeSide);
        if (isSelected !== undefined) {
            params = params.set("isSelected", isSelected.toString());
        }
        return this.httpClient.get<Exam[]>(url, { params });
    }

    /**
     * GET /patients/{patientId}/organizations/{organizationsId}/lists
     * Récupérer tous les examens par patient ID
     */
    getAllExamsListByPatientId(patientId: number, organizationsId: number[], eyeSide: string, isSelected?: boolean): Observable<ExamSummary[]> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/patients/${patientId}/organizations/${organizationsId.join(",")}/lists`;
        let params = new HttpParams().set("eyeSide", eyeSide);
        if (isSelected !== undefined) {
            params = params.set("isSelected", isSelected.toString());
        }
        return this.httpClient.get<ExamSummary[]>(url, { params });
    }

    /**
     * GET /patients/{patientId}/organizations/{organizationsId}/prec/lists
     * Récupérer tous les examens par patient ID qui ont un calcul avec power true
     */
    getAllExamsByPatientIdEyeSideCalculPowerTrue(patientId: number, organizationsId: number[], eyeSide: string, isSelected?: boolean): Observable<ExamSummary[]> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/patients/${patientId}/organizations/${organizationsId.join(",")}/prec/lists`;
        let params = new HttpParams().set("eyeSide", eyeSide);
        if (isSelected !== undefined) {
            params = params.set("isSelected", isSelected.toString());
        }
        return this.httpClient.get<ExamSummary[]>(url, { params });
    }

    /**
     * POST /organizations/{organizationsId}
     * Ajouter un examen
     */
    addExam(examDto: Exam, organizationsId: number[]): Observable<Exam> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/organizations/${organizationsId.join(",")}`;
        return this.httpClient.post<Exam>(url, examDto);
    }

    /**
     * POST /organizations/{organizationsId}
     * Ajouter un examen
     */
    addExams(examDtos: Exam[], patientId: number, organizationsId: number[]): Observable<Exam[]> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/add/${patientId}/organizations/${organizationsId.join(",")}`;
        return this.httpClient.post<Exam[]>(url, examDtos);
    }

    /**
     * POST /imports
     * Importer des fichiers de biométrie
     */
    insertFile(file: File, biometer: string, organizationsId: number[]): Observable<void> {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("biometer", biometer);
        organizationsId.forEach((id) => {
            formData.append(`organizationsId`, id.toString());
        });

        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/imports`;
        return this.httpClient.post<void>(url, formData);
    }

    /**
     * POST /patients/{patientId}/organizations/{organizationsId}/average
     * Calculer la moyenne
     */
    calculAverage(patientId: number, organizationsId: number[], examsDto: Exam[]): Observable<Exam> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/patients/${patientId}/organizations/${organizationsId.join(",")}/average`;
        return this.httpClient.post<Exam>(url, examsDto);
    }

    /**
     * PUT /organizations/{organizationsId}/update
     * Modifier un examen
     */
    editExam(examDto: Exam, organizationsId: number[]): Observable<Exam> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/organizations/${organizationsId.join(",")}/update`;
        return this.httpClient.put<Exam>(url, examDto);
    }

    editSelectExam(examDto: Exam, organizationsId: number[]): Observable<Exam> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/organizations/${organizationsId.join(",")}/select`;
        return this.httpClient.put<Exam>(url, examDto);
    }

    /**
     * DELETE /{id}/organizations/{organizationsId}/delete
     * Supprimer un examen
     */
    deleteExam(examId: number, organizationsId: number[]): Observable<void> {
        const url = `${this.API_URL}${this.ENDPOINTS_CATARACT}/${examId}/organizations/${organizationsId.join(",")}/delete`;
        return this.httpClient.delete<void>(url);
    }
}
