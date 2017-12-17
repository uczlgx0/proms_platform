import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import { PromsFormsModule } from '../../forms/promsforms.module';
//import { MoxfqComponent } from '../../forms/moxfq.component';
import {
    FollowupActionService,
    FollowupActionPopupService,
    FollowupActionComponent,
    FollowupOutcomesComponent,
    FollowupActionDetailComponent,
    FollowupActionDialogComponent,
    FollowupActionPopupComponent,
    FollowupActionDeletePopupComponent,
    FollowupActionDeleteDialogComponent,
    followupActionRoute,
    followupActionPopupRoute,
    FollowupActionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...followupActionRoute,
    ...followupActionPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        PromsFormsModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FollowupActionComponent,
        FollowupOutcomesComponent,
        FollowupActionDetailComponent,
        FollowupActionDialogComponent,
        FollowupActionDeleteDialogComponent,
        FollowupActionPopupComponent,
        FollowupActionDeletePopupComponent,
    ],
    entryComponents: [
        FollowupActionComponent,
        FollowupOutcomesComponent,
        FollowupActionDialogComponent,
        FollowupActionPopupComponent,
        FollowupActionDeleteDialogComponent,
        FollowupActionDeletePopupComponent,
    ],
    providers: [
        FollowupActionService,
        FollowupActionPopupService,
        FollowupActionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsFollowupActionModule {}
