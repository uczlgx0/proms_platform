import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NorthumbriapromsPatientModule } from './patient/patient.module';
import { NorthumbriapromsAddressModule } from './address/address.module';
import { NorthumbriapromsProcedureModule } from './procedure/procedure.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        NorthumbriapromsPatientModule,
        NorthumbriapromsAddressModule,
        NorthumbriapromsProcedureModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsEntityModule {}
