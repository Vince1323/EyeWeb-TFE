import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';


@Component({
    templateUrl: './terms-display.html',
    selector: 'terms-display'
})
export class TermsDisplay {

    @Input() display: boolean = false;
    @Output() notDisplay = new EventEmitter<boolean>();

    onHide() {
        this.notDisplay.emit(false);
    }
}
