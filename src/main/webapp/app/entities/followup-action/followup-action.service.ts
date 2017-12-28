import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { FollowupAction } from './followup-action.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FollowupActionService {

    private resourceUrl = SERVER_API_URL + 'api/followup-actions';
    private patientResourceUrl = SERVER_API_URL + 'api/patient/';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/followup-actions';
    private resourceIndexUrl = SERVER_API_URL + 'api/_index/followup-actions';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(followupAction: FollowupAction): Observable<FollowupAction> {
        const copy = this.convert(followupAction);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(followupAction: FollowupAction): Observable<FollowupAction> {
        const copy = this.convert(followupAction);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<FollowupAction> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    findByPatientId(id: any, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.patientResourceUrl+id+'/followup-actions', options)
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

    indexAll(): Observable<ResponseWrapper> {
        return this.http.get(this.resourceIndexUrl)
            .map((res: Response) => this.convertResponse(res));
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.post(this.resourceSearchUrl, req.query, options)
            .map((res: any) => this.convertSearchResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    private convertSearchResponse(res: Response): ResponseWrapper {
        console.log("res  = " , res );
        const jsonResponse = res.json();
        let items = [];
        if (jsonResponse.results !== undefined) {
            items = jsonResponse.results;
        }
        const result = [];
        for (let i = 0; i < items.length; i++) {
            result.push(this.convertItemFromServer(items[i]));
        }
        return new ResponseWrapper(res.headers, {results: result, categories: jsonResponse.categories}, res.status);
    }

    /**
     * Convert a returned JSON object to FollowupAction.
     */
    private convertItemFromServer(json: any): FollowupAction {
        const entity: FollowupAction = Object.assign(new FollowupAction(), json);
        entity.scheduledDate = this.dateUtils
            .convertLocalDateFromServer(json.scheduledDate);
        entity.completedDate = this.dateUtils
            .convertLocalDateFromServer(json.completedDate);
        return entity;
    }

    /**
     * Convert a FollowupAction to a JSON which can be sent to the server.
     */
    private convert(followupAction: FollowupAction): FollowupAction {
        const copy: FollowupAction = Object.assign({}, followupAction);

        if (followupAction.scheduledDate) {
            copy.scheduledDate = this.dateUtils.convertLocalDateToServer(followupAction.scheduledDate.date);
        }

        if (followupAction.completedDate) {
            copy.completedDate = this.dateUtils.convertLocalDateToServer(followupAction.completedDate.date);
        }
        return copy;
    }
}
