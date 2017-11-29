import { BaseEntity } from './../../shared';

export const enum GenderType {
    'MALE',
    ' FEMALE',
    ' OTHER',
    ' UNKNOWN'
}

export class Patient implements BaseEntity {
    constructor(
        public id?: number,
        public familyName?: string,
        public givenName?: string,
        public birthDate?: any,
        public gender?: GenderType,
        public nhsNumber?: number,
        public email?: string,
        public addresses?: BaseEntity[],
    ) {
    }
}
