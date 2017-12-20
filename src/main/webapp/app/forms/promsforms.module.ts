import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NorthumbriapromsSharedModule } from '../shared';
import { MoxfqComponent } from './moxfq.component'
import { PainvasComponent } from './painvas.component'
import { AofasComponent } from './aofas.component'
import { FormsService } from './forms.service'

@NgModule({
    imports: [
        NorthumbriapromsSharedModule
    ],
    declarations: [
        MoxfqComponent,
        PainvasComponent,
        AofasComponent
    ],
    exports: [
        MoxfqComponent,
        PainvasComponent,
        AofasComponent
    ],
    providers: [
        FormsService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PromsFormsModule {}
