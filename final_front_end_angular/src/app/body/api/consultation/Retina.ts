export interface Retina {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    dmla: boolean;
    dmlaText?: string | null;
    diabeticRetinopathy: boolean;
    epiretinalMembrane: boolean;
    vitreomacularTraction: boolean;
    crscSequel: boolean;
    ovcr: boolean;
    oacr: boolean;
    macularOedema: boolean;
    diabetes: boolean;
    oedemDdmla: boolean;
    avcr: boolean;
    oedemaOther: boolean;
    oedemaOtherText?: string | null;
    other: boolean;
    otherText?: string | null;
}
