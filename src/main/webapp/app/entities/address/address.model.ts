import { BaseEntity } from './../../shared';

export class Address implements BaseEntity {
    constructor(
        public id?: number,
        public street?: string,
        public line?: string,
        public city?: string,
        public county?: string,
        public postalCode?: string,
        public country?: string,
        public patient?: BaseEntity,
    ) {
    }
}
