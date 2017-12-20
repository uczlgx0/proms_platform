import { BaseEntity } from './../../shared';

export class Questionnaire implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public copyright?: string,
        public reference?: string,
    ) {
    }
}
