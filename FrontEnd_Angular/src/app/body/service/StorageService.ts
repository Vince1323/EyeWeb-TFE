import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class StorageService {
    constructor() {}

    get(key: string): any {
        const data = sessionStorage.getItem(key);
        return JSON.parse(data);
    }

    set(key: string, value: any): void {
        sessionStorage.setItem(key, JSON.stringify(value));
    }

    remove(key: string): void {
        sessionStorage.removeItem(key);
    }
}
