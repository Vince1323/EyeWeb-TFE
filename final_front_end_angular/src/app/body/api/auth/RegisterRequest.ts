import { Address } from "../Address";

export interface RegisterRequest {
  email: string;
  password: string;
  firstname: string;
  lastname: string;
  phone: string;
  birthDate: string;
  ntm: string;
  address: Address;
  hasReadTermsAndConditions: boolean;
}
