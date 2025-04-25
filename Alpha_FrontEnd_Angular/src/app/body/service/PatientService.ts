import { catchError, Observable, tap, throwError } from 'rxjs';
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { API_URL } from "src/app/constants/app.constants";
import { Patient } from "../api/patient/Patient";


@Injectable({
    providedIn: "root",
})
export class PatientService {
    readonly API_URL = API_URL.DEV;
    readonly ENDPOINT_PATIENTS = "/patients";

    constructor(private httpClient: HttpClient) {}

    /**
     * Crée une nouvelle adresse en base de données.
     * @param address - Objet contenant les informations de l'adresse.
     * @returns Observable contenant l'adresse créée avec un ID.
     */
    createAddress(address: any): Observable<any> {
        const url = `${this.API_URL}/patients/address`;
        
        return this.httpClient.post<any>(url, address).pipe(
            tap((createdAddress) => {
                if (!createdAddress || !createdAddress.id) {
                    throw new Error("❌ L'API n'a pas retourné d'adresse valide !");
                }
                console.log("Adresse créée avec succès :", createdAddress);
            }),
            catchError((error) => {
                console.error("Erreur API createAddress :", error);
                return throwError(() => new Error("Error creating the address"));
            })
        );
    }

     /**
     * Récupère la liste des patients d'une organisation donnée.
     * @param organizationsId - Liste des ID des organisations
     * @returns Observable contenant un tableau de patients
     */
    getPatientsByOrganizationId(
        organizationsId: number[]
    ): Observable<Patient[]> {
        return this.httpClient
            .get<Patient[]>(
                this.API_URL +
                    this.ENDPOINT_PATIENTS +
                    '/organizations/' +
                    organizationsId.join(',')
            );
    }

     /**
     * Récupère un patient par son ID et son organisation.
     * @param idPatient - ID du patient
     * @param organizationId - Liste des ID des organisations
     * @returns Observable contenant un patient
     */
    getPatientById(
        idPatient: number,
        organizationId: number[]
    ): Observable<Patient> {
        return this.httpClient
            .get<Patient>(
                this.API_URL +
                    this.ENDPOINT_PATIENTS +
                    '/' +
                    idPatient +
                    '/organizations/' +
                    organizationId.join(',')
            );
    }

  
        /**
         * Met à jour un patient existant en base de données.
         * @param patient - Objet patient avec les modifications
         * @param organizationsId - Liste des IDs des organisations associées
         * @returns Observable contenant le patient mis à jour
         */
        updatePatient(patient: Patient, organizationsId: number[]): Observable<Patient> {
            if (!patient.id) {
                throw new Error("ERROR : Patient ID is required for update.");
            }

            if (!organizationsId || organizationsId.length === 0) {
                throw new Error("ERROR : At least one organization ID is required for update.");
            }

            const url = `${this.API_URL}${this.ENDPOINT_PATIENTS}/${patient.id}/organizations/${organizationsId.join(',')}`;

            return this.httpClient.put<Patient>(url, patient).pipe(
                tap((updatedPatient: any) => {
                    if (!updatedPatient) {
                        throw new Error("The API did not return an updated patient!");
                    }
                }),
                catchError(() => {
                    return throwError(() => new Error("Error updating the patient"));
                })
            );
        }



    /**
     * Supprime un patient en mettant son attribut `deleted` à `true`.
     * @param idPatient - ID du patient à supprimer
     * @param organizationsId - Liste des IDs des organisations associées
     * @returns Observable<Patient> contenant le patient marqué comme supprimé
     */
    deletePatient(idPatient: number, organizationsId: number[]): Observable<Patient> {
        if (!idPatient) {
            throw new Error("ERROR : Patient ID is required for deletion.");
        }

        if (!organizationsId || organizationsId.length === 0) {
            throw new Error("ERROR : At least one organization ID is required for deletion.");
        }

        const url = `${this.API_URL}${this.ENDPOINT_PATIENTS}/${idPatient}/organizations/${organizationsId.join(',')}/delete`;

        return this.httpClient.put<Patient>(url, {}).pipe(
            tap((deletedPatient: any) => {
                if (!deletedPatient) {
                    throw new Error("The API did not return a deleted patient!");
                }
            }),
            catchError(() => {
                return throwError(() => new Error("Error deleting the patient"));
            })
        );
    }
    
    

    /**
     * Récupère la liste des genres pour le dropdown.
     * @returns Observable contenant la liste des genres
     */

    getGender() {
        return this.httpClient.get<any>("assets/body/data/gender.json");
    }

    //POST
    addPatient(patient: Patient, organizationsId: number[]): Observable<Patient> {
        return this.httpClient.post<Patient>(
            `${this.API_URL}${this.ENDPOINT_PATIENTS}/organizations/${organizationsId.join(",")}`,
            patient
        );
    }
}
