import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    QuestionnaireService,
    QuestionnairePopupService,
    QuestionnaireComponent,
    QuestionnaireDetailComponent,
    QuestionnaireDialogComponent,
    QuestionnairePopupComponent,
    QuestionnaireDeletePopupComponent,
    QuestionnaireDeleteDialogComponent,
    questionnaireRoute,
    questionnairePopupRoute,
    QuestionnaireResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...questionnaireRoute,
    ...questionnairePopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        QuestionnaireComponent,
        QuestionnaireDetailComponent,
        QuestionnaireDialogComponent,
        QuestionnaireDeleteDialogComponent,
        QuestionnairePopupComponent,
        QuestionnaireDeletePopupComponent,
    ],
    entryComponents: [
        QuestionnaireComponent,
        QuestionnaireDialogComponent,
        QuestionnairePopupComponent,
        QuestionnaireDeleteDialogComponent,
        QuestionnaireDeletePopupComponent,
    ],
    providers: [
        QuestionnaireService,
        QuestionnairePopupService,
        QuestionnaireResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsQuestionnaireModule {}
