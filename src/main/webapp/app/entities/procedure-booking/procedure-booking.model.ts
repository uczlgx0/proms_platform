import { BaseEntity } from './../../shared';
import { Patient } from '../patient/patient.model';
import { FollowupPlan } from '../followup-plan/followup-plan.model';

export class ProcedureBooking implements BaseEntity {
    constructor(
        public id?: number,
        public consultantName?: string,
        public hospitalSite?: string,
        public scheduledDate?: any,
        public performedDate?: any,
        public primaryProcedure?: string,
        public otherProcedures?: string,
        public patient?: Patient,
        public followupPlan?: FollowupPlan,
    ) {
        this.patient = new Patient();
    }
}
