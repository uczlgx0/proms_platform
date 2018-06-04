import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    TimepointService,
    TimepointPopupService,
    TimepointComponent,
    TimepointDetailComponent,
    TimepointDialogComponent,
    TimepointPopupComponent,
    TimepointDeletePopupComponent,
    TimepointDeleteDialogComponent,
    timepointRoute,
    timepointPopupRoute,
    TimepointResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...timepointRoute,
    ...timepointPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TimepointComponent,
        TimepointDetailComponent,
        TimepointDialogComponent,
        TimepointDeleteDialogComponent,
        TimepointPopupComponent,
        TimepointDeletePopupComponent,
    ],
    entryComponents: [
        TimepointComponent,
        TimepointDialogComponent,
        TimepointPopupComponent,
        TimepointDeleteDialogComponent,
        TimepointDeletePopupComponent,
    ],
    providers: [
        TimepointService,
        TimepointPopupService,
        TimepointResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsTimepointModule {}
