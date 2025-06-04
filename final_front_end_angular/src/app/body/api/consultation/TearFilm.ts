export interface TearFilm {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    breakUptime: boolean;
    breakUptimeText?: string | null;
    other: boolean;
    otherText?: string | null;
}
