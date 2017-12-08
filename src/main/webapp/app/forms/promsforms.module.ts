import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../shared';
import { MoxfqComponent } from './moxfq.component'
import { PainvasComponent } from './painvas.component'

@NgModule({
    imports: [
        NorthumbriapromsSharedModule
    ],
    declarations: [
        MoxfqComponent,
        PainvasComponent
    ],
    exports: [
        MoxfqComponent,
        PainvasComponent
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PromsFormsModule {}
