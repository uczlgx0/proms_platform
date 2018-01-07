import { BaseEntity } from './../../shared';

export const enum EventType {
    'SCHEDULED',
    ' AD_HOC'
}

export class CareEvent implements BaseEntity {
    constructor(
        public id?: number,
        public type?: EventType,
        public timepoint?: BaseEntity,
        public patient?: BaseEntity,
        public followupActions?: BaseEntity[],
        public followupPlan?: BaseEntity,
    ) {
    }
}
