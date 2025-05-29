export interface StereoscopicVision {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    synoptophore: number | null;
    randot: number | null;
    nearOther: number | null;
    titmus: number | null;
    tno: number | null;
    distanceOther: number | null;
}
