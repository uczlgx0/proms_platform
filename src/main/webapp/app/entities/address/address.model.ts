import { BaseEntity } from './../../shared';
import { Patient } from './../patient/patient.model';

export class Address implements BaseEntity {
    constructor(
        public id?: number,
        public street?: string,
        public lines?: Array<string>,
        public city?: string,
        public county?: string,
        public postalCode?: string,
        public country?: string,
        public patient?: Patient
    ) {
        this.lines = [];
        // set three empty lines to start
        this.lines.push('');
        this.lines.push('');
        this.lines.push('');
    }
}
