import { BaseEntity } from './../../shared';

export class ProcedureTimepoint implements BaseEntity {
    constructor(
        public id?: number,
        public procedure?: BaseEntity,
        public timepoint?: BaseEntity,
    ) {
    }
}
