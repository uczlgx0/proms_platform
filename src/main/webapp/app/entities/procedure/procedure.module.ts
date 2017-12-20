import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    ProcedureService,
    ProcedurePopupService,
    ProcedureComponent,
    ProcedureDetailComponent,
    ProcedureDialogComponent,
    ProcedurePopupComponent,
    ProcedureDeletePopupComponent,
    ProcedureDeleteDialogComponent,
    procedureRoute,
    procedurePopupRoute,
    ProcedureResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...procedureRoute,
    ...procedurePopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProcedureComponent,
        ProcedureDetailComponent,
        ProcedureDialogComponent,
        ProcedureDeleteDialogComponent,
        ProcedurePopupComponent,
        ProcedureDeletePopupComponent,
    ],
    entryComponents: [
        ProcedureComponent,
        ProcedureDialogComponent,
        ProcedurePopupComponent,
        ProcedureDeleteDialogComponent,
        ProcedureDeletePopupComponent,
    ],
    providers: [
        ProcedureService,
        ProcedurePopupService,
        ProcedureResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsProcedureModule {}
