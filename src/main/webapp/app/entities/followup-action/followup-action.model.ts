import { BaseEntity } from './../../shared';
import { ResponseItem } from './response-item.model';

export const enum ActionPhase {
    'PRE_OPERATIVE',
    ' POST_OPERATIVE',
    ' UKNOWN'
}

export const enum ActionType {
    'QUESTIONNAIRE',
    ' UKNOWN'
}

export class FollowupAction implements BaseEntity {
    constructor(
        public id?: number,
        public phase?: ActionPhase,
        public scheduledDate?: any,
        public name?: string,
        public type?: ActionType,
        public outcomeScore?: number,
        public outcomeComment?: string,
        public completedDate?: any,
        public followupPlan?: BaseEntity,
        public patient?: BaseEntity,
        public questionnaire?: BaseEntity,
        public responseItems?: ResponseItem[],
    ) {
        responseItems = [];
    }
}
