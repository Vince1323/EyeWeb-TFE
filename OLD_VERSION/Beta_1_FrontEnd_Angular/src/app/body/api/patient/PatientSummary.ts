export interface PatientSummary {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    lastname: string | null;
    firstname: string | null;
    birthDate: string | null;
}
