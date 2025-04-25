export interface Oct {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    opticNerveNormal: boolean;
    opticNerveText?: string | null;
    octNormal: boolean;
    octText?: string | null;
    macularNormal: boolean;
    macularText?: string | null;
}
