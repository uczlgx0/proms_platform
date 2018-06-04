import { BaseEntity } from './../../shared';
import { FollowupAction } from './followup-action.model';

export class ResponseItem implements BaseEntity {
    constructor(
        public id?: number,
        public question?: string,
        public value?: number,
        public localId?: string,
        public followupActionId?: number
    ) {
    }
}
