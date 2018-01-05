import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { ProcedureTimepoint } from './procedure-timepoint.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProcedureTimepointService {

    private resourceUrl = SERVER_API_URL + 'api/procedure-timepoints';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/procedure-timepoints';

    constructor(private http: Http) { }

    create(procedureTimepoint: ProcedureTimepoint): Observable<ProcedureTimepoint> {
        const copy = this.convert(procedureTimepoint);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(procedureTimepoint: ProcedureTimepoint): Observable<ProcedureTimepoint> {
        const copy = this.convert(procedureTimepoint);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ProcedureTimepoint> {
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
     * Convert a returned JSON object to ProcedureTimepoint.
     */
    private convertItemFromServer(json: any): ProcedureTimepoint {
        const entity: ProcedureTimepoint = Object.assign(new ProcedureTimepoint(), json);
        return entity;
    }

    /**
     * Convert a ProcedureTimepoint to a JSON which can be sent to the server.
     */
    private convert(procedureTimepoint: ProcedureTimepoint): ProcedureTimepoint {
        const copy: ProcedureTimepoint = Object.assign({}, procedureTimepoint);
        return copy;
    }
}
