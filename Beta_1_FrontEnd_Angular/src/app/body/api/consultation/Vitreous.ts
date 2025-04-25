export interface Vitreous {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    vitrectomy: boolean;
    weissRing: boolean;
    floatingBody: boolean;
    vitrite: boolean;
    bleeding: boolean;
    other: boolean;
    otherText?: string | null;
}
