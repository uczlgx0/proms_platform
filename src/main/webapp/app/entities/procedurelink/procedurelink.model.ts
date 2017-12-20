import { BaseEntity } from './../../shared';

export class Procedurelink implements BaseEntity {
    constructor(
        public id?: number,
        public procedure?: BaseEntity,
        public questionnaire?: BaseEntity,
    ) {
    }
}
