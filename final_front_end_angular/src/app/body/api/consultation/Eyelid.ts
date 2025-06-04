export interface Eyelid {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    entropion: boolean;
    ectropion: boolean;
    ptosis: boolean;
    anteriorBlepharitis: boolean;
    staphylococcus: boolean;
    demodex: boolean;
    other: boolean;
    otherText?: string | null;
}
