import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/api';

@Component({
    selector: 'app-administrative',
    templateUrl: './consultation.component.html',
    styleUrl: './consultation.component.scss',
})
export class ConsultationComponent implements OnInit {
    messages: Message[] | undefined;

    ngOnInit() {
        this.messages = [
            {
                severity: 'warn',
                detail: 'This is a premium feature. You currently have access to a basic preview only.',
            },
        ];
    }
}
