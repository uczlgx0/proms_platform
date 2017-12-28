import { BaseEntity } from './../../shared';
import { Patient } from '../patient/patient.model';
import { FollowupPlan } from '../followup-plan/followup-plan.model';

export const enum Laterality {
    'RIGHT',
    ' LEFT',
    ' BILATERAL',
    ' NOT_APPLICABLE',
    ' UNKNOWN'
}

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
        public side?: Laterality
    ) {
        this.patient = new Patient();
        //this.side = Laterality['UNKNOWN'];
    }
}
