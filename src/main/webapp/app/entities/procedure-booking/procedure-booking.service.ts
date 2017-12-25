import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ProcedureBooking } from './procedure-booking.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProcedureBookingService {

    private resourceUrl = SERVER_API_URL + 'api/procedure-bookings';
    private patientResourceUrl = SERVER_API_URL + 'api/patient/';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/procedure-bookings';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(procedureBooking: ProcedureBooking): Observable<ProcedureBooking> {
        let patientId = procedureBooking.patient.id;
        const copy = this.convert(procedureBooking);
        //copy.patient = null;
        return this.http.post(this.patientResourceUrl+patientId+'/procedure-bookings', copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(procedureBooking: ProcedureBooking): Observable<ProcedureBooking> {
        let patientId = procedureBooking.patient.id;
        const copy = this.convert(procedureBooking);
        //copy.patient = null;
        return this.http.put(this.patientResourceUrl+patientId+'/procedure-bookings', copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ProcedureBooking> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    findByPatientId(id: any, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.patientResourceUrl+id+'/procedure-bookings', options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to ProcedureBooking.
     */
    private convertItemFromServer(json: any): ProcedureBooking {
        const entity: ProcedureBooking = Object.assign(new ProcedureBooking(), json);
        entity.scheduledDate = this.dateUtils
            .convertLocalDateFromServer(json.scheduledDate);
        entity.performedDate = this.dateUtils
            .convertLocalDateFromServer(json.performedDate);
        return entity;
    }

    /**
     * Convert a ProcedureBooking to a JSON which can be sent to the server.
     */
    private convert(procedureBooking: ProcedureBooking): ProcedureBooking {
        const copy: ProcedureBooking = Object.assign({}, procedureBooking);
        copy.scheduledDate = this.dateUtils.convertLocalDateToServer(procedureBooking.scheduledDate);

        if (procedureBooking.performedDate) {
            copy.performedDate = this.dateUtils.convertLocalDateToServer(procedureBooking.performedDate);
        }
        return copy;
    }
}
