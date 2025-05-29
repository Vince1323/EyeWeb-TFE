import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    templateUrl: "./success.component.html",
})
export class SuccessComponent implements OnInit {
    message: string = "N/A";

    constructor(private router: Router) {}

    ngOnInit(): void {
        const navigation = this.router.getCurrentNavigation();
        const caller = navigation?.extras.state?.["caller"];

        if (caller === "NewPasswordComponent") {
            this.message = "Password has been reset";
        } else {
            this.message = "Your account is awaiting verification, you will receive an email once it has been validated.";
        }
    }
}
