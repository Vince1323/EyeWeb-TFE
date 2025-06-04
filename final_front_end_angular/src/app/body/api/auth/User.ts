import {Role} from "../Role";
import {Address} from "../Address";
import {Organization} from "../Organization";

export interface User {
    birthDate: any;
    zip: string;
    city: string;
    id: number;
    version: number;
    firstname: string;
    lastname: string;
    email: string;
    phone: string;
    role: Role;
    organizations: Organization[];
    hasReadTermsAndConditions: boolean;
    verified: boolean;
    validEmail: boolean;
    address: Address;
    createdAt: string;
}
