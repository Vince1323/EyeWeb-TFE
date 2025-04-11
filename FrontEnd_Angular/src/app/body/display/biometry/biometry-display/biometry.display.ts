import {
    Component,
    Input,
    OnChanges,
    OnInit,
    SimpleChanges,
} from '@angular/core';
import {Biometry} from "../../../api/exam/Biometry";

@Component({
    templateUrl: './biometry.display.html',
    selector: 'biometry-display',
    styles: [
        `
            ::ng-deep {
                .p-tooltip {
                    max-width: fit-content;
                }
            }
        `,
    ],
})
export class BiometryDisplay implements OnInit, OnChanges {
    constructor() {}

    @Input() biometry!: Biometry;
    messageWarnings!: any;

    ngOnInit() {
        this.verifyValueValidity();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['biometry']) {
            this.verifyValueValidity();
        }
    }

    verifyValueValidity() {
        this.messageWarnings = {};
        if (this.biometry.k1 && this.biometry.k1 < 41) {
            this.messageWarnings['k1'] = 'K1 < 41: Please check K values.';
        } else {
            if (this.biometry.k1 > 47) {
                this.messageWarnings['k1'] = 'K1 > 47: Please check K values.';
            }
        }
        if (this.biometry.k2 && this.biometry.k2 < 41) {
            this.messageWarnings['k2'] = 'K2 < 41 : Please check K values.';
        } else {
            if (this.biometry.k2 > 47) {
                this.messageWarnings['k2'] = 'K2 > 47: Please check K values.';
            }
        }
        if (this.biometry.kAvg && this.biometry.kAvg < 41) {
            this.messageWarnings['kAvg'] = 'Kavg < 41 : Please check K values.';
        } else {
            if (this.biometry.kAvg > 47) {
                this.messageWarnings['kAvg'] =
                    'Kavg > 47: Please check K values.';
            }
        }

        const kAvgVerif = parseFloat(
            (
                (this.biometry.k1 + this.biometry.k2) /
                2
            ).toFixed(2)
        );

        if (
            this.biometry.kAvg &&
            kAvgVerif !== this.biometry.kAvg
        ) {
            this.messageWarnings['kAvg'] == undefined
                ? (this.messageWarnings['kAvg'] =
                      'Please check K1, K2 and Km values.')
                : (this.messageWarnings['kAvg'] +=
                      '\nPlease check K1, K2 and Km values.');
        }
        if (this.biometry.kAstig && this.biometry.kAstig > 1) {
            if (this.biometry.kAstig > 2.5) {
                this.messageWarnings['kAstig'] =
                    'Astigmatism is greater than 2.5 D, please check K values.';
            } else if (this.biometry.kAstig > 3.5) {
                this.messageWarnings['kAstig'] =
                    'Astigmatism greater than 3.5 D, please check K values.';
            } else {
                this.messageWarnings['kAstig'] = 'Astigmatism greater than 1D.';
            }
        }
        if (this.biometry.al && this.biometry.al < 21.3) {
            this.messageWarnings['al'] =
                'Axial length under 21.30 mm, please check.';
        }
        if (this.biometry.al && this.biometry.al > 26.6) {
            this.messageWarnings['al'] =
                'Axial length greater than 26.60, please check.';
        }
        if (this.biometry.snr && this.biometry.snr < 10) {
            this.messageWarnings['snr'] =
                'SNR value is lower than 10, please check axial length.';
        }
        if (this.biometry.acd && this.biometry.acd < 2) {
            this.messageWarnings['acd'] = 'ACD less than 2 mm, please check.';
        }
        if (this.biometry.acd && this.biometry.acd > 4.2) {
            this.messageWarnings['acd'] = 'ACD greater than 4.2, please check.';
        }
        if (this.biometry.wtw && this.biometry.wtw < 10) {
            this.messageWarnings['wtw'] = 'WTW less than 10 mm, please check.';
        }
        if (this.biometry.wtw && this.biometry.wtw > 13) {
            this.messageWarnings['wtw'] =
                'WTW greater than 13 mm, please check.';
        }
    }
}
