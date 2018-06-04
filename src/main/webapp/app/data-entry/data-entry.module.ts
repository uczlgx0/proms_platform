import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../shared';
import { PromsFormsModule } from '../forms/promsforms.module';

import { DATAENTRY_ROUTE, DataEntryComponent } from './';

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        PromsFormsModule,
        RouterModule.forChild([ DATAENTRY_ROUTE ])
    ],
    declarations: [
        DataEntryComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsDataEntryModule {}
