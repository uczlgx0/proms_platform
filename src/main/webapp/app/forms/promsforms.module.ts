import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../shared';
import { MoxfqComponent } from './moxfq.component'
import { PainvasComponent } from './painvas.component'
import { AofasComponent } from './aofas.component'
import { AosComponent } from './aos.component'
import { CofasComponent } from './cofas.component'
import { Eq5d3lComponent } from './eq5d3l.component'
import { FormsService } from './forms.service'

@NgModule({
    imports: [
        NorthumbriapromsSharedModule
    ],
    declarations: [
        MoxfqComponent,
        PainvasComponent,
        AofasComponent,
        CofasComponent,
        Eq5d3lComponent,
        AosComponent
    ],
    exports: [
        MoxfqComponent,
        PainvasComponent,
        AofasComponent,
        CofasComponent,
        Eq5d3lComponent,
        AosComponent
    ],
    providers: [
        FormsService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PromsFormsModule {}
