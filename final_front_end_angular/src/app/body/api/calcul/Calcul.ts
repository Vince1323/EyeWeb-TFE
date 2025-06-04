import { Lens } from "../Lens";
import { Constant } from "../Constant";
import { Diopter } from "../Diopter";
import { Exam } from "../exam/Exam";

export interface Calcul {
    id?: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    eyeSide: 'OU' | 'OD' | 'OS' | null;
    exam: Exam | null;
    lens: Lens | null;
    constants: Constant[] | null;
    diopters: Diopter[] | null;
    targetRefraction: number | null;
    
    isSecondEye: boolean;
    precPowerSelected: number;
    se: number;
    idReferencePrecExam: number;
    idReferencePrecCalcul: number;

    constantType: string;
}
