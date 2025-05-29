export interface OpticNerve {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    anteriorIschemic: boolean;
    posterior: boolean;
    other: boolean;
    otherText?: string | null;
    glaucoma: boolean;
    glaucomaExcavation?: string | null;
    bleeding: boolean;
    neuropatia: boolean;
}
