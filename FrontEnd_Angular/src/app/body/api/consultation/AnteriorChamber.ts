export interface AnteriorChamber {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    pseudoexfoliation: boolean;
    pigmentDispersion: boolean;
    iridotomy: boolean;
    iridotomyLocation?: string | null;
    lower: boolean;
    uveitis: boolean;
    coloboma: boolean;
    other: boolean;
    otherText?: string | null;
}
