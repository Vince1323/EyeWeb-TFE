import { Exam } from "../exam/Exam";
import { Address } from "../Address";
import { Organization } from "../Organization";

export interface Patient {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    lastname: string | null;
    firstname: string | null;
    fullname: string | null;
    birthDate: string | null;
    niss: string | null;
    gender: string | null;
    phone: string | null;
    mail: string | null;
    job: string | null;
    hobbies: string | null;
    address: Address | null;
    organizations: Organization[] | null;
    exams: Exam[] | null;
}
