import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/api';
import { LayoutService } from '../../../../layout/service/app.layout.service';
import { DeviceDetectionService } from '../../../service/DeviceDetectionService';
import { StorageService } from '../../../service/StorageService';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-preoperative',
  templateUrl: './preoperative.component.html',
  styleUrls: ['./preoperative.component.scss'],
  providers: [DatePipe]
})
export class PreoperativeComponent implements OnInit {
  isMobileDevice: boolean = false;
  messages: Message[] = [];
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

    if (this.patient?.lastname && this.patient?.firstname && this.patient?.birthDate) {
      const formattedDate = this.datePipe.transform(this.patient.birthDate, 'dd-MM-yyyy');

      // MAJ affichage local (layoutService)
      this.layoutService.config.update((config) => ({
        ...config,
        patientInfo: `Patient(e) : ${this.patient.lastname} ${this.patient.firstname} – ${formattedDate}`
      }));

      // 💬 Envoie les infos du patient à la topbar via un événement global
      const event = new CustomEvent('patientLoaded', {
        detail: {
          name: `${this.patient.lastname} ${this.patient.firstname}`,
          birthDate: formattedDate
        }
      });
      document.dispatchEvent(event);
    }
  }
}
