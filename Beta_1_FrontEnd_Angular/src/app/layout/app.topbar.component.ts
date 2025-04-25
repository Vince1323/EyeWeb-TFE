import { Component, ElementRef, ViewChild } from '@angular/core';
import { LayoutService, ColorScheme } from 'src/app/layout/service/app.layout.service';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html',
})
export class AppTopbarComponent {
    @ViewChild('menubutton') menuButton!: ElementRef;

    // Initialise darkTheme en fonction du colorScheme actuel
    darkTheme: boolean = this.layoutService.config().colorScheme === 'dim';

    constructor(public layoutService: LayoutService) {}

    onMenuButtonClick() {
        this.layoutService.onMenuToggle();
    }

    onProfileButtonClick() {
        this.layoutService.showProfileSidebar();
    }

    changeTheme() {
        const newColorScheme: ColorScheme = this.darkTheme ? 'light' : 'dim';
        this.layoutService.config.update((config) => ({
            ...config,
            colorScheme: newColorScheme,
        }));
        this.darkTheme = !this.darkTheme;
    }
}
