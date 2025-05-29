import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/api';

@Component({
    selector: 'app-administrative',
    templateUrl: './planning.component.html',
    styleUrl: './planning.component.scss',
})
export class PlanningComponent implements OnInit {
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
