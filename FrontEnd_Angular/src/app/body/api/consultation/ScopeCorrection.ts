export interface ScopeCorrection {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    glassesSphere: number | null;
    glassesCylinder: number | null;
    glassesAxis: number | null;
    glassesVisualAcuity: number | null;
    lensesSphere: number | null;
    lensesCylinder: number | null;
    lensesAxis: number | null;
    lensesVisualAcuity: number | null;
    glassesEqSphere: number | null;
    glassesEqCylinder: number | null;
    glassesEqAxis: number | null;
    glassesEqVisualAcuity: number | null;
}
