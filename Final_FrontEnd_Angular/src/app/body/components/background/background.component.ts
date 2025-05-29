import { Component, OnInit } from '@angular/core';
import { DeviceDetectionService} from "../../service/DeviceDetectionService";
import { LayoutService} from "../../../layout/service/app.layout.service";
import { Message } from 'primeng/api';

@Component({
    selector: 'app-background',
    templateUrl: './background.component.html',
    styleUrl: './background.component.scss',
})
export class BackgroundComponent implements OnInit {
    //VAR RESPONSIVE
    isMobileDevice: boolean = false;
    messages: Message[] | undefined;

    constructor(
        public layoutService: LayoutService,
        private deviceService: DeviceDetectionService
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
    }
}
