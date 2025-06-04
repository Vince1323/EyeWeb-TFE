export interface LensManufacturer {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    name: string | null;
}
