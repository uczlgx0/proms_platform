import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    FollowupPlanService,
    FollowupPlanPopupService,
    FollowupPlanComponent,
    FollowupPlanDetailComponent,
    FollowupPlanDialogComponent,
    FollowupPlanPopupComponent,
    FollowupPlanDeletePopupComponent,
    FollowupPlanDeleteDialogComponent,
    followupPlanRoute,
    followupPlanPopupRoute,
    FollowupPlanResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...followupPlanRoute,
    ...followupPlanPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FollowupPlanComponent,
        FollowupPlanDetailComponent,
        FollowupPlanDialogComponent,
        FollowupPlanDeleteDialogComponent,
        FollowupPlanPopupComponent,
        FollowupPlanDeletePopupComponent,
    ],
    entryComponents: [
        FollowupPlanComponent,
        FollowupPlanDialogComponent,
        FollowupPlanPopupComponent,
        FollowupPlanDeleteDialogComponent,
        FollowupPlanDeletePopupComponent,
    ],
    providers: [
        FollowupPlanService,
        FollowupPlanPopupService,
        FollowupPlanResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsFollowupPlanModule {}
