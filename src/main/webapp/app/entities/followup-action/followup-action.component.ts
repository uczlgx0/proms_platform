import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import * as _ from 'underscore';

import { FollowupAction } from './followup-action.model';
import { Query } from './query.model';
import { FollowupActionService } from './followup-action.service';
import { ProcedureService } from '../procedure/procedure.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-followup-action',
    templateUrl: './followup-action.component.html'
})
export class FollowupActionComponent implements OnInit, OnDestroy {

    currentAccount: any;
    followupActions: FollowupAction[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    @Input() selectedPatientId;
    proceduresLookup: any;
    query: Query;
    model: any = {};
    includeStarted: boolean = true;
    includePending: boolean = false;
    includeUninitialised: boolean = false;

    constructor(
        private followupActionService: FollowupActionService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private procedureService: ProcedureService,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.query = new Query();
        this.query.statuses.push('STARTED');
    }

    loadAll() {
        console.log("this.currentSearch  = " , this.currentSearch );
        if (this.currentSearch) {
            //this.currentSearch = '';
            this.query.token = this.currentSearch;
        } else {
            this.query.token = '';
        }
        console.log("this.query  = " , this.query );
        //if (this.currentSearch) {
        //    this.followupActionService.search({
        //        page: this.page - 1,
        //        query: this.currentSearch,
        //        size: this.itemsPerPage,
        //        sort: this.sort()}).subscribe(
        //            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
        //            (res: ResponseWrapper) => this.onError(res.json)
        //        );
        //    return;
        //}
        // load followup actions for patient if id is set
        if(this.selectedPatientId) {
            this.query.patientIds = [];
            this.query.patientIds.push(this.selectedPatientId);
        } else {
            this.query.patientIds = [];
        }

        this.followupActionService.search({
            page: this.page - 1,
            query: this.query,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        if(!this.selectedPatientId) {
            this.router.navigate(['/followup-action'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
            });
        }
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        //this.router.navigate(['/followup-action', {
        //    page: this.page,
        //    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //}]);
        this.loadAll();
    }

    search(query) {
        console.log("query  = " , query );
        if(query) {
            this.currentSearch = query;
        }
        //this.query.token = query;
        //if (this.currentSearch) {
        //    //this.currentSearch = '';
        //    this.query.token = this.currentSearch;
        //}
        this.page = 0;
        //this.router.navigate(['/followup-action', {
        //    search: this.currentSearch,
        //    page: this.page,
        //    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //}]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        // load procedures
        this.procedureService.allAsSelectOptions()
            .subscribe((res: ResponseWrapper) => {
                this.proceduresLookup = _.indexBy(res.json, 'value');
            },
            (res: ResponseWrapper) => this.onError(res.json()));
        this.registerChangeInFollowupActions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: FollowupAction) {
        return item.id;
    }

    toggleState(status: any) {
        let localState: boolean = false;
        if(status === 'STARTED') {
            this.includeStarted = !this.includeStarted;
            localState = this.includeStarted;
        } else if (status === 'PENDING') {
            this.includePending = !this.includePending;
            localState = this.includePending;
        } else if (status === 'UNINITIALISED') {
            this.includeUninitialised = !this.includeUninitialised;
            localState = this.includeUninitialised;
        }

        // update query and execute search
        if(!localState){
            this.query.statuses = this.query.statuses.filter(function(v){return v != status;});
        } else {
            this.query.statuses.push(status);
        }
        // search
        this.search(null);
    }

    indexAll() {
        this.followupActionService.indexAll().subscribe(
            (res: ResponseWrapper) => {console.log("this.res  = " , res.json )},
            (res: ResponseWrapper) => this.onError(res.json)
        )
    }

    registerChangeInFollowupActions() {
        this.eventSubscriber = this.eventManager.subscribe('followupActionListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.followupActions = data.results;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
