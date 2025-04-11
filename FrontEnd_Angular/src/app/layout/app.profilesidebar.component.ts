import {Component, OnInit} from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { AuthService } from '../body/service/AuthService';
import { User } from '../body/api/auth/User';
import { Router } from '@angular/router';
import {NAVIGATION} from "../constants/app.constants";

@Component({
    selector: 'app-profilemenu',
    templateUrl: './app.profilesidebar.component.html',
})
export class AppProfileSidebarComponent implements OnInit {
    constructor(
        public layoutService: LayoutService,
        private authService: AuthService,
        private route: Router
    ) {}

    user!: User;

    ngOnInit() {
        this.user = this.authService.getUserInfoFromToken();
    }

    get visible(): boolean {
        return this.layoutService.state.profileSidebarVisible;
    }

    set visible(_val: boolean) {
        this.layoutService.state.profileSidebarVisible = _val;
    }

    logout() {
        this.visible = false;
        this.authService.logout();
    }

    goToBackOffice() {
        this.visible = false;
        this.route.navigate([NAVIGATION.BACKOFFICE]);
    }

    goToProfile() {
        this.visible = false;
        this.route.navigate([NAVIGATION.PROFILE]);
    }
}
