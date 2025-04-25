export interface FreeEdge {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    normal: boolean;
    telangiectasia: boolean;
    meibomiusGlandsDysfunction: number;
    other: boolean;
    otherText?: string | null;
}
