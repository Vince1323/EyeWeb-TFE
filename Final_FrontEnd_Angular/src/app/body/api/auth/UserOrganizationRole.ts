import {Organization} from "../Organization";
import {User} from "./User";

export interface UserOrganizationRole {
    user: User;
    isAdmin: boolean;
    organization: Organization;
}
