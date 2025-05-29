import {Component, OnInit} from '@angular/core';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import {Message} from "primeng/api";

@Component({
    templateUrl: './contact.component.html',
})
export class ContactComponent implements OnInit {
    options: any;

    name: string = '';

    email: string = '';

    message: string = '';

    messages: Message[] | undefined;

    content: any[] = [
        { icon: 'pi pi-fw pi-phone', title: 'Phone', info: '+32 491 125 100' },
        {
            icon: 'pi pi-fw pi-map-marker',
            title: 'Our Head Office',
            info: 'Clos du Manoir 8, 6940 Durbuy',
        },
        { icon: 'pi pi-fw pi-link', title: 'Our Website', info: 'jorami.be' },
    ];

    constructor(private layoutService: LayoutService) {}

    ngOnInit(): void {
    }
}
