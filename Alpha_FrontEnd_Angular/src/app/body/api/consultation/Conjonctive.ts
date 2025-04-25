export interface Conjonctive {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    injection: number;
    colorantIntake: number;
    other: boolean;
    otherText?: string | null;
}
