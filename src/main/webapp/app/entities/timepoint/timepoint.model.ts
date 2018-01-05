import { BaseEntity } from './../../shared';

export const enum TimeUnit {
    'DAY',
    ' WEEK',
    ' MONTH',
    ' YEAR'
}

export class Timepoint implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public value?: number,
        public unit?: TimeUnit,
    ) {
    }
}
