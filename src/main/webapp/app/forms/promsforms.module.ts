import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../shared';
import { MoxfqComponent } from './moxfq.component'
import { PainvasComponent } from './painvas.component'
import { AofasComponent } from './aofas.component'
import { AosComponent } from './aos.component'
import { FormsService } from './forms.service'

@NgModule({
    imports: [
        NorthumbriapromsSharedModule
    ],
    declarations: [
        MoxfqComponent,
        PainvasComponent,
        AofasComponent,
        AosComponent
    ],
    exports: [
        MoxfqComponent,
        PainvasComponent,
        AofasComponent,
        AosComponent
    ],
    providers: [
        FormsService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PromsFormsModule {}
