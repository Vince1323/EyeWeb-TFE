import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/api';

@Component({
    selector: 'app-administrative',
    templateUrl: './protocol.component.html',
    styleUrl: './protocol.component.scss',
})
export class ProtocolComponent implements OnInit {
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
