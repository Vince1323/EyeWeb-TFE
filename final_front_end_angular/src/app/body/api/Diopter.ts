export interface Diopter {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    iolPower: number | null;
    value: number | null;
    formula: string | null;
}
