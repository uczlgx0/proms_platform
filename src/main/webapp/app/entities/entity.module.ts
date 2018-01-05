import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NorthumbriapromsPatientModule } from './patient/patient.module';
import { NorthumbriapromsAddressModule } from './address/address.module';
import { NorthumbriapromsProcedureModule } from './procedure/procedure.module';
import { NorthumbriapromsQuestionnaireModule } from './questionnaire/questionnaire.module';
import { NorthumbriapromsProcedurelinkModule } from './procedurelink/procedurelink.module';
import { NorthumbriapromsProcedureBookingModule } from './procedure-booking/procedure-booking.module';
import { NorthumbriapromsFollowupPlanModule } from './followup-plan/followup-plan.module';
import { NorthumbriapromsFollowupActionModule } from './followup-action/followup-action.module';
import { NorthumbriapromsHealthcareProviderModule } from './healthcare-provider/healthcare-provider.module';
import { NorthumbriapromsTimepointModule } from './timepoint/timepoint.module';
import { NorthumbriapromsProcedureTimepointModule } from './procedure-timepoint/procedure-timepoint.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        NorthumbriapromsPatientModule,
        NorthumbriapromsAddressModule,
        NorthumbriapromsProcedureModule,
        NorthumbriapromsQuestionnaireModule,
        NorthumbriapromsProcedurelinkModule,
        NorthumbriapromsProcedureBookingModule,
        NorthumbriapromsFollowupPlanModule,
        NorthumbriapromsFollowupActionModule,
        NorthumbriapromsHealthcareProviderModule,
        NorthumbriapromsTimepointModule,
        NorthumbriapromsProcedureTimepointModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NorthumbriapromsEntityModule {}
