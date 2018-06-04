import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    CareEventService,
    CareEventPopupService,
    CareEventComponent,
    CareEventDetailComponent,
    CareEventDialogComponent,
    CareEventPopupComponent,
    CareEventDeletePopupComponent,
    CareEventDeleteDialogComponent,
    careEventRoute,
    careEventPopupRoute,
    CareEventResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...careEventRoute,
    ...careEventPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CareEventComponent,
        CareEventDetailComponent,
        CareEventDialogComponent,
        CareEventDeleteDialogComponent,
        CareEventPopupComponent,
        CareEventDeletePopupComponent,
    ],
    entryComponents: [
        CareEventComponent,
        CareEventDialogComponent,
        CareEventPopupComponent,
        CareEventDeleteDialogComponent,
        CareEventDeletePopupComponent,
    ],
    providers: [
        CareEventService,
        CareEventPopupService,
        CareEventResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsCareEventModule {}
