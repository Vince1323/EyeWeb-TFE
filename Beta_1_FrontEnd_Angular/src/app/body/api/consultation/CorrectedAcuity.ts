export interface CorrectedAcuity {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    distanceVisualAcuity: number | null;
    distanceVisualAcuityOU: number | null;
    intermediateVisualAcuity: number | null;
    intermediateVisualAcuityOU: number | null;
    nearVisualAcuity: number | null;
    nearVisualAcuityOU: number | null;
}
