import { BaseEntity } from './../../shared';

export class Procedure implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public externalCode?: string,
        public localCode?: number,
    ) {
    }
}
