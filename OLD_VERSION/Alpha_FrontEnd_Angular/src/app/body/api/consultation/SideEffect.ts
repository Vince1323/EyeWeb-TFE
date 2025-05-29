export interface SideEffect {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    normal: boolean;
    halos: boolean;
    glare: boolean;
    stardust: boolean;
    other: boolean;
    otherText?: string | null;
}
