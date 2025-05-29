export interface SubjectiveRefraction {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    sphere: number | null;
    cylinder: number | null;
    axis: number | null;
    visualAcuity: number | null;
    visualAcuityOU: number | null;
    addition: number | null;
    nearVisualAcuity: number | null;
    nearVisualAcuityOU: number | null;
}
