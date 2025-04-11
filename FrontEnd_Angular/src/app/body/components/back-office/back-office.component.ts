import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../service/UserService';
import { User } from '../../api/auth/User';
import { ConfirmationService, MessageService, SortEvent } from 'primeng/api';
import { Table } from 'primeng/table';

@Component({
    selector: 'back-office',
    templateUrl: './back-office.component.html',
    providers: [
        UserService,
        ConfirmationService,
        MessageService
    ],
})
export class BackOfficeComponent implements OnInit {

    @ViewChild('dt2') dt2: Table;

    users: User[];
    loading: boolean = true;
    isSorted: boolean = null;
    initialValue: User[];
    message: string[] = ['Do you want to activate this account ?', 'Do you want to disable this account ?'];
    query:boolean = false;

    constructor(
        private userService: UserService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ){}

    ngOnInit(): void {
        this.userService.getAllUsers().subscribe({
            next:(usersResp) => {
                this.users = usersResp;
                this.initialValue = [...usersResp];
                this.loading = false;
            },
        });
    }

    customSort(event: SortEvent) {
        if (this.isSorted == null || this.isSorted === undefined) {
            this.isSorted = true;
            this.sortTableData(event);
        } else if (this.isSorted == true) {
            this.isSorted = false;
            this.sortTableData(event);
        } else if (this.isSorted == false) {
            this.isSorted = null;
            this.users = [...this.initialValue];
            this.dt2.reset();
        }
    }

    sortTableData(event) {
        event.data.sort((data1, data2) => {
            let value1 = data1[event.field];
            let value2 = data2[event.field];
            let result = null;
            if (value1 == null && value2 != null) result = -1;
            else if (value1 != null && value2 == null) result = 1;
            else if (value1 == null && value2 == null) result = 0;
            else if (typeof value1 === 'string' && typeof value2 === 'string') result = value1.localeCompare(value2);
            else result = value1 < value2 ? -1 : value1 > value2 ? 1 : 0;

            return event.order * result;
        });
    }

    updateVerified(id: number) {
        const idMessage = !this.users[id].verified ? 0 : 1;
        this.confirmationService.confirm({
            message: this.message[idMessage],
            header: 'Account verification',
            icon: 'pi pi-info-circle',
            acceptButtonStyleClass:"p-button-danger p-button-text",
            rejectButtonStyleClass:"p-button-text p-button-text",
            acceptIcon:"none",
            rejectIcon:"none",

            accept: () => {
                this.query = true;
                this.users[id].verified = !this.users[id].verified;
                this.userService.updateVerifiedUser(this.users[id]).subscribe({
                    next:(usersResp) => {
                        this.query = false;
                        this.messageService.add({
                            severity: 'success',
                            summary: 'User Modified',
                            detail: 'User data has been successfully updated.',
                        });
                    },
                });
            }
        });
    }
}
