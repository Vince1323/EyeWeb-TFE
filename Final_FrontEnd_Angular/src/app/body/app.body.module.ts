import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BackgroundDisplay } from './display/background/background.display';
import { AnteriorChamberDisplay } from './display/consultation/anterior-chamber/anterior-chamber';
import { ConclusionDisplay } from './display/consultation/conclusion/conclusion';
import { ConjunctivaDisplay } from './display/consultation/conjunctiva/conjunctiva';
import { CorneaDisplay } from './display/consultation/cornea/cornea';
import { CorrectivePrescriptionsDisplay } from './display/consultation/corrective-prescriptions/corrective-prescriptions';
import { DiagnosisDisplay } from './display/consultation/diagnosis/diagnosis';
import { DliDisplay } from './display/consultation/dli/dli';
import { DominantEyeDisplay } from './display/consultation/dominant-eye/dominant-eye';
import { EyelidDisplay } from './display/consultation/eyelid/eyelid';
import { KeratometryDisplay } from './display/consultation/keratometry/keratometry';
import { LensDisplay } from './display/consultation/lens/lens';
import { LidMarginDisplay } from './display/consultation/lid-margin/lid-margin';
import { ObjectiveRefractionDisplay } from './display/consultation/objective-refraction/objective-refraction';
import { OctDisplay } from './display/consultation/oct/oct';
import { OpticNerveDisplay } from './display/consultation/optic-nerve/optic-nerve';
import { PentacamDisplay } from './display/consultation/pentacam/pentacam';
import { PupillometryDisplay } from './display/consultation/pupillometry/pupillometry';
import { RemarksDisplay } from './display/consultation/remarks/remarks';
import { RetinaDisplay } from './display/consultation/retina/retina';
import { SpecularDisplay } from './display/consultation/specular/specular';
import { SubjectiveRefractionDisplay } from './display/consultation/subjective-refraction/subjective-refraction';
import { TearFilmDisplay } from './display/consultation/tear-film/tear-film';
import { UcvaDisplay } from './display/consultation/ucva/ucva';
import { VitreousDisplay } from './display/consultation/vitreous/vitreous';
import { TimelineModule } from 'primeng/timeline';
import { AccordionModule } from 'primeng/accordion';
import { TagModule } from 'primeng/tag';
import { MultiSelectModule } from 'primeng/multiselect';
import { InputSwitchModule } from 'primeng/inputswitch';
import { DropdownModule } from 'primeng/dropdown';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { InputMaskModule } from 'primeng/inputmask';
import { KeyFilterModule } from 'primeng/keyfilter';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { MessagesModule } from 'primeng/messages';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { ProgressBarModule } from 'primeng/progressbar';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { TableModule } from 'primeng/table';
import { StepsModule } from 'primeng/steps';
import { PasswordModule } from 'primeng/password';
import { CheckboxModule } from 'primeng/checkbox';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { TreeModule } from 'primeng/tree';
import { DecimalFormatPipe } from './utils/pipes/decimal-format.pipe';
import { AllowNegativeDirective } from './utils/directives/allow-negative.directive';
import {SelectButtonModule} from "primeng/selectbutton";
import { TermsDisplay } from './display/terms/terms-display';
import {DialogImportExcelDisplay} from "./display/import/excel/dialog-import-excel-display";
import { FileUploadModule } from 'primeng/fileupload';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { PanelModule } from 'primeng/panel';
import {BiometryDisplay} from "./display/biometry/biometry-display/biometry.display";
import {BiometriesForm} from "./display/biometry/biometries-form/biometries-form";
import {BiometryForm} from "./display/biometry/biometry-form/biometry-form";
import {AverageDisplay} from "./display/biometry/average/average";
import { DialogImportPDFDisplay } from './display/import/PDF-image/display-import-pdf-display';
import {NewCalculDisplay} from "./display/calcul/new-calcul/new-calcul.display";
import {NewCalculForm} from "./display/calcul/new-calcul-form/new-calcul-form";
import {PatientForm} from "./display/patient/patient-form/patient-form";
import {DialogImportEncodeDisplay} from "./display/import/encode/dialog-import-encode-display";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import { ConfirmationDisplay } from './display/confirmation/confirmation-display';
import { CalculDisplay } from './display/calcul/calcul-display/calcul-display';
import { SecondEyeForm } from './display/calcul/second-eye-form/second-eye-form';

@NgModule({
    declarations: [
        BackgroundDisplay,
        AnteriorChamberDisplay,
        ConclusionDisplay,
        ConjunctivaDisplay,
        CorneaDisplay,
        CorrectivePrescriptionsDisplay,
        DiagnosisDisplay,
        DliDisplay,
        DominantEyeDisplay,
        EyelidDisplay,
        KeratometryDisplay,
        LensDisplay,
        LidMarginDisplay,
        ObjectiveRefractionDisplay,
        OctDisplay,
        OpticNerveDisplay,
        PentacamDisplay,
        PupillometryDisplay,
        RemarksDisplay,
        RetinaDisplay,
        SpecularDisplay,
        SubjectiveRefractionDisplay,
        TearFilmDisplay,
        UcvaDisplay,
        VitreousDisplay,
        TermsDisplay,
        DialogImportExcelDisplay,
        BiometryDisplay,
        BiometriesForm,
        BiometryForm,
        AverageDisplay,
        DialogImportPDFDisplay,
        DialogImportEncodeDisplay,
        PatientForm,
        NewCalculDisplay,
        NewCalculForm,
        ConfirmationDisplay,
        CalculDisplay,
        SecondEyeForm,


        DecimalFormatPipe,
        AllowNegativeDirective,
    ],
    imports: [
        CommonModule,
        TimelineModule,
        AccordionModule,
        TagModule,
        MultiSelectModule,
        InputSwitchModule,
        DropdownModule,
        ReactiveFormsModule,
        FormsModule,
        InputGroupModule,
        InputGroupAddonModule,
        ButtonModule,
        CalendarModule,
        InputMaskModule,
        KeyFilterModule,
        RadioButtonModule,
        InputNumberModule,
        DialogModule,
        ToastModule,
        DividerModule,
        MessagesModule,
        AutoCompleteModule,
        InputTextModule,
        TooltipModule,
        ProgressBarModule,
        BreadcrumbModule,
        TableModule,
        StepsModule,
        PasswordModule,
        CheckboxModule,
        ToggleButtonModule,
        TreeModule,
        SelectButtonModule,
        FileUploadModule,
        ScrollPanelModule,
        PanelModule,
        ConfirmDialogModule
    ],
    exports: [
        BackgroundDisplay,
        TermsDisplay,
        DialogImportExcelDisplay,
        BiometryDisplay,
        BiometriesForm,
        BiometryForm,
        AverageDisplay,
        DialogImportPDFDisplay,
        AnteriorChamberDisplay,
        ConclusionDisplay,
        ConjunctivaDisplay,
        CorneaDisplay,
        CorrectivePrescriptionsDisplay,
        DiagnosisDisplay,
        DliDisplay,
        DominantEyeDisplay,
        EyelidDisplay,
        KeratometryDisplay,
        LensDisplay,
        LidMarginDisplay,
        ObjectiveRefractionDisplay,
        OctDisplay,
        OpticNerveDisplay,
        PentacamDisplay,
        PupillometryDisplay,
        RemarksDisplay,
        RetinaDisplay,
        SpecularDisplay,
        SubjectiveRefractionDisplay,
        TearFilmDisplay,
        UcvaDisplay,
        VitreousDisplay,
        DialogImportEncodeDisplay,
        PatientForm,
        NewCalculDisplay,
        NewCalculForm,
        ConfirmationDisplay,
        CalculDisplay,
        SecondEyeForm,


        DecimalFormatPipe,
        AllowNegativeDirective,
    ],
})
export class SharedModule {}
