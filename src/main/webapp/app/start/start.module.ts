import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../shared';

import { START_ROUTE, StartComponent } from './';

@NgModule({
    imports: [
        NorthumbriapromsSharedModule,
        RouterModule.forChild([ START_ROUTE ])
    ],
    declarations: [
        StartComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsStartModule {}
