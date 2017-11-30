import { BaseEntity } from './../../shared';

export class FollowupPlan implements BaseEntity {
    constructor(
        public id?: number,
        public procedureBooking?: BaseEntity,
        public patient?: BaseEntity,
        public followupActions?: BaseEntity[],
    ) {
    }
}
