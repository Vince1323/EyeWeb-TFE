export interface DominantDirector {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    dominantSide: 'OU' | 'OD' | 'OS' | null;
    directorSide: 'OU' | 'OD' | 'OS' | null;
}
