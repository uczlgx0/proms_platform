import { BaseEntity } from './../../shared';

export class HealthcareProvider implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}
