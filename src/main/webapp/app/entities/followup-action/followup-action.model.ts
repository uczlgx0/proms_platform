import { BaseEntity } from './../../shared';
import { ResponseItem } from './response-item.model';
import { CareEvent } from '../care-event/care-event.model';

export const enum ActionPhase {
    'PRE_OPERATIVE',
    'POST_OPERATIVE',
    'UKNOWN'
}

export enum ActionStatus {
    'PENDING',
    'UNINITIALISED',
    'COMPLETED',
    'STARTED'
}

export const enum ActionType {
    'QUESTIONNAIRE',
    'UKNOWN'
}

export class FollowupAction implements BaseEntity {
    constructor(
        public id?: number,
        public phase?: ActionPhase,
        public status?: string,
        public scheduledDate?: any,
        public name?: string,
        public type?: ActionType,
        public outcomeScore?: number,
        public outcomeComment?: string,
        public completedDate?: any,
        public careEvent?: CareEvent,
        public patient?: BaseEntity,
        public questionnaire?: BaseEntity,
        public responseItems?: ResponseItem[]
    ) {
        responseItems = [];
    }
}
