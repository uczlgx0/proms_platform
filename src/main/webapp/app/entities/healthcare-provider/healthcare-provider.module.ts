import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../../shared';
import {
    HealthcareProviderService,
    HealthcareProviderPopupService,
    HealthcareProviderComponent,
    HealthcareProviderDetailComponent,
    HealthcareProviderDialogComponent,
    HealthcareProviderPopupComponent,
    HealthcareProviderDeletePopupComponent,
    HealthcareProviderDeleteDialogComponent,
    healthcareProviderRoute,
    healthcareProviderPopupRoute,
    HealthcareProviderResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...healthcareProviderRoute,
    ...healthcareProviderPopupRoute,
];

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        HealthcareProviderComponent,
        HealthcareProviderDetailComponent,
        HealthcareProviderDialogComponent,
        HealthcareProviderDeleteDialogComponent,
        HealthcareProviderPopupComponent,
        HealthcareProviderDeletePopupComponent,
    ],
    entryComponents: [
        HealthcareProviderComponent,
        HealthcareProviderDialogComponent,
        HealthcareProviderPopupComponent,
        HealthcareProviderDeleteDialogComponent,
        HealthcareProviderDeletePopupComponent,
    ],
    providers: [
        HealthcareProviderService,
        HealthcareProviderPopupService,
        HealthcareProviderResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsHealthcareProviderModule {}
