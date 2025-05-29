import { Exam } from "../exam/Exam";
import { Constant } from "../Constant";
import { Lens } from "../Lens";

export interface CalculMatrix {
    id: number;
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
    patientId: number | null;
    targetRefraction: string | null;
    formulas: string[] | null;
    powers: number[] | null;
    valueMatrix: number[][] | null;
    valueMatrixConversion: any | null;
    selectedPower: number | null;

    isSecondEye: boolean;
    precPowerSelected: number;
    se: number;
    idReferencePrecExam: number;
    idReferencePrecCalcul: number;

    filter: boolean | null;
}
