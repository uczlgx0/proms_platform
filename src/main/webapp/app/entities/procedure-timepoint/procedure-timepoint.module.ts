import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    ProcedureTimepointService,
    ProcedureTimepointPopupService,
    ProcedureTimepointComponent,
    ProcedureTimepointDetailComponent,
    ProcedureTimepointDialogComponent,
    ProcedureTimepointPopupComponent,
    ProcedureTimepointDeletePopupComponent,
    ProcedureTimepointDeleteDialogComponent,
    procedureTimepointRoute,
    procedureTimepointPopupRoute,
    ProcedureTimepointResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...procedureTimepointRoute,
    ...procedureTimepointPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProcedureTimepointComponent,
        ProcedureTimepointDetailComponent,
        ProcedureTimepointDialogComponent,
        ProcedureTimepointDeleteDialogComponent,
        ProcedureTimepointPopupComponent,
        ProcedureTimepointDeletePopupComponent,
    ],
    entryComponents: [
        ProcedureTimepointComponent,
        ProcedureTimepointDialogComponent,
        ProcedureTimepointPopupComponent,
        ProcedureTimepointDeleteDialogComponent,
        ProcedureTimepointDeletePopupComponent,
    ],
    providers: [
        ProcedureTimepointService,
        ProcedureTimepointPopupService,
        ProcedureTimepointResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsProcedureTimepointModule {}
