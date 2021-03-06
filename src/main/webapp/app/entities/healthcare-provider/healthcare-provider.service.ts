import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { HealthcareProvider } from './healthcare-provider.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class HealthcareProviderService {

    private resourceUrl = SERVER_API_URL + 'api/healthcare-providers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/healthcare-providers';

    constructor(private http: Http) { }

    create(healthcareProvider: HealthcareProvider): Observable<HealthcareProvider> {
        const copy = this.convert(healthcareProvider);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(healthcareProvider: HealthcareProvider): Observable<HealthcareProvider> {
        const copy = this.convert(healthcareProvider);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<HealthcareProvider> {
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

    allAsSelectOptions(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertToSelectOption(res));
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
     * Convert the result into an array of {value, label} items for use in UI select
     * @param res
     * @returns {any}
     */
    private convertToSelectOption(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push({value: jsonResponse[i].name, label: jsonResponse[i].name});
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to HealthcareProvider.
     */
    private convertItemFromServer(json: any): HealthcareProvider {
        const entity: HealthcareProvider = Object.assign(new HealthcareProvider(), json);
        return entity;
    }

    /**
     * Convert a HealthcareProvider to a JSON which can be sent to the server.
     */
    private convert(healthcareProvider: HealthcareProvider): HealthcareProvider {
        const copy: HealthcareProvider = Object.assign({}, healthcareProvider);
        return copy;
    }
}
