import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    ProcedureBookingService,
    ProcedureBookingPopupService,
    ProcedureBookingComponent,
    ProcedureBookingDetailComponent,
    ProcedureBookingDialogComponent,
    ProcedureBookingPopupComponent,
    ProcedureBookingDeletePopupComponent,
    ProcedureBookingDeleteDialogComponent,
    procedureBookingRoute,
    procedureBookingPopupRoute,
    ProcedureBookingResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...procedureBookingRoute,
    ...procedureBookingPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProcedureBookingComponent,
        ProcedureBookingDetailComponent,
        ProcedureBookingDialogComponent,
        ProcedureBookingDeleteDialogComponent,
        ProcedureBookingPopupComponent,
        ProcedureBookingDeletePopupComponent,
    ],
    entryComponents: [
        ProcedureBookingComponent,
        ProcedureBookingDialogComponent,
        ProcedureBookingPopupComponent,
        ProcedureBookingDeleteDialogComponent,
        ProcedureBookingDeletePopupComponent,
    ],
    exports: [
        ProcedureBookingComponent,
        ProcedureBookingPopupComponent,
        ProcedureBookingDeleteDialogComponent,
        ProcedureBookingDeletePopupComponent
    ],
    providers: [
        ProcedureBookingService,
        ProcedureBookingPopupService,
        ProcedureBookingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsProcedureBookingModule {}
