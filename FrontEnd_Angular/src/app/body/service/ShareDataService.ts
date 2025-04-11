import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class ShareDataService {
    private userData: any;

    constructor() {}

    setUserData(userData: any) {
        this.userData = userData;
    }

    getUserData() {
        return this.userData;
    }

    clearUserData() {
        this.userData = null;
    }
}
