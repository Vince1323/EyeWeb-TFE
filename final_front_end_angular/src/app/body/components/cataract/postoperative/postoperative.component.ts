import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/api';
import {LayoutService} from "../../../../layout/service/app.layout.service";
import {DeviceDetectionService} from "../../../service/DeviceDetectionService";
import { StorageService } from '../../../service/StorageService';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'app-administrative',
    templateUrl: './postoperative.component.html',
    styleUrl: './postoperative.component.scss',
})
export class PostoperativeComponent implements OnInit {
    isMobileDevice: boolean = false;
    messages: Message[] | undefined;
    patient: any;
    birthDateValue: Date | null = null;


    constructor(
        public layoutService: LayoutService,
        private deviceService: DeviceDetectionService,
        private storageService: StorageService,
        private datePipe: DatePipe
        
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
        this.patient = this.storageService.get('patient');
            if (this.patient?.birthDate) {
        this.birthDateValue = new Date(this.patient.birthDate);
    }
  }
}
