import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { CareEvent } from './care-event.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CareEventService {

    private resourceUrl = SERVER_API_URL + 'api/care-events';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/care-events';
    private patientResourceUrl = SERVER_API_URL + 'api/patient/';
    private planResourceUrl = SERVER_API_URL + 'api/followup-plan/';

    constructor(private http: Http) { }

    create(careEvent: CareEvent): Observable<CareEvent> {
        const copy = this.convert(careEvent);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(careEvent: CareEvent): Observable<CareEvent> {
        const copy = this.convert(careEvent);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<CareEvent> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    findByPatientId(id: any, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.patientResourceUrl+id+'/care-events', options)
            .map((res: Response) => this.convertResponse(res));
    }

    findByPlanId(id: any, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.planResourceUrl+id+'/care-events', options)
            .map((res: Response) => this.convertResponse(res));
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
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
     * Convert a returned JSON object to CareEvent.
     */
    private convertItemFromServer(json: any): CareEvent {
        const entity: CareEvent = Object.assign(new CareEvent(), json);
        return entity;
    }

    /**
     * Convert a CareEvent to a JSON which can be sent to the server.
     */
    private convert(careEvent: CareEvent): CareEvent {
        const copy: CareEvent = Object.assign({}, careEvent);
        return copy;
    }
}
