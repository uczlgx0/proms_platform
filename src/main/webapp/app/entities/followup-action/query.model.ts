export class Query {
    constructor(public locations?:Array<string>,
                public procedures?:Array<string>,
                public genders?:Array<string>,
                public consultants?:Array<string>,
                public phases?:Array<string>,
                public statuses?:Array<string>,
                public sides?:Array<string>,
                public types?:Array<string>,
                public patientIds?:Array<string>,
                public careEvents?:Array<string>,
                public minAge?:number,
                public maxAge?:number,
                public token?:string) {
        this.procedures = [];
        this.locations = [];
        this.genders = [];
        this.consultants = [];
        this.phases = [];
        this.statuses = [];
        this.sides = [];
        this.types = [];
        this.patientIds = [];
        this.careEvents = [];
        this.token = '';
    }
}
