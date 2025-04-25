import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/api';

@Component({
    selector: 'app-administrative',
    templateUrl: './exam.component.html',
    styleUrl: './exam.component.scss',
})
export class ExamComponent implements OnInit {
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
