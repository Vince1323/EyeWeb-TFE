export interface Address {
    
    id?: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;
    boxNumber: string | null;
    street: string | null;
    streetNumber: string | null;
    zipCode: string | null;
    city: string | null;
    country: string | null;
}
