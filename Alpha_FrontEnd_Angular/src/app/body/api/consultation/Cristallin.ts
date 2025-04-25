export interface Cristallin {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    cataract: boolean;
    cortical: number;
    nuclear: number;
    white: boolean;
    trauma: boolean;
    phacodonesis: boolean;
    luxation: boolean;
    icl: boolean;
    iol: boolean;
    other: boolean;
    otherText?: string | null;
}
