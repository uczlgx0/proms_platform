import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import { NorthumbriapromsProcedureBookingModule } from '../procedure-booking/procedure-booking.module';

import {
    PatientService,
    PatientPopupService,
    PatientComponent,
    PatientDetailComponent,
    PatientDialogComponent,
    PatientPopupComponent,
    PatientDeletePopupComponent,
    PatientDeleteDialogComponent,
    patientRoute,
    patientPopupRoute,
    PatientResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...patientRoute,
    ...patientPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        NorthumbriapromsProcedureBookingModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PatientComponent,
        PatientDetailComponent,
        PatientDialogComponent,
        PatientDeleteDialogComponent,
        PatientPopupComponent,
        PatientDeletePopupComponent,
    ],
    entryComponents: [
        PatientComponent,
        PatientDialogComponent,
        PatientPopupComponent,
        PatientDeleteDialogComponent,
        PatientDeletePopupComponent,
    ],
    providers: [
        PatientService,
        PatientPopupService,
        PatientResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsPatientModule {}
