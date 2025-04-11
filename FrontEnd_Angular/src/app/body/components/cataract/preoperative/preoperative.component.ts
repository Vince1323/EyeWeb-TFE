import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/api';
import {LayoutService} from "../../../../layout/service/app.layout.service";
import {DeviceDetectionService} from "../../../service/DeviceDetectionService";

@Component({
    selector: 'app-preoperative',
    templateUrl: './preoperative.component.html',
    styleUrl: './preoperative.component.scss',
})
export class PreoperativeComponent implements OnInit {
    isMobileDevice: boolean = false;
    messages: Message[] | undefined;

    constructor(
        public layoutService: LayoutService,
        private deviceService: DeviceDetectionService
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
        this.messages = [
            {
                severity: 'warn',
                detail: 'This is a premium feature. You currently have access to a basic preview only.',
            },
        ];
    }
}
