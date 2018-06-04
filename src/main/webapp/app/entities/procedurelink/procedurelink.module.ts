import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    ProcedurelinkService,
    ProcedurelinkPopupService,
    ProcedurelinkComponent,
    ProcedurelinkDetailComponent,
    ProcedurelinkDialogComponent,
    ProcedurelinkPopupComponent,
    ProcedurelinkDeletePopupComponent,
    ProcedurelinkDeleteDialogComponent,
    procedurelinkRoute,
    procedurelinkPopupRoute,
    ProcedurelinkResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...procedurelinkRoute,
    ...procedurelinkPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProcedurelinkComponent,
        ProcedurelinkDetailComponent,
        ProcedurelinkDialogComponent,
        ProcedurelinkDeleteDialogComponent,
        ProcedurelinkPopupComponent,
        ProcedurelinkDeletePopupComponent,
    ],
    entryComponents: [
        ProcedurelinkComponent,
        ProcedurelinkDialogComponent,
        ProcedurelinkPopupComponent,
        ProcedurelinkDeleteDialogComponent,
        ProcedurelinkDeletePopupComponent,
    ],
    providers: [
        ProcedurelinkService,
        ProcedurelinkPopupService,
        ProcedurelinkResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsProcedurelinkModule {}
