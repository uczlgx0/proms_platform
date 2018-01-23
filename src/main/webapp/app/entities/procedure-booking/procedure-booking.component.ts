import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import * as _ from 'underscore';

import { ProcedureBooking } from './procedure-booking.model';
import { ProcedureBookingService } from './procedure-booking.service';
import { ProcedureService } from '../procedure/procedure.service';

import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-procedure-booking',
    templateUrl: './procedure-booking.component.html'
})
export class ProcedureBookingComponent implements OnInit, OnDestroy {

    currentAccount: any;
    procedureBookings: ProcedureBooking[];
    proceduresLookup: any;
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
    @Input() selectedPatientId: number;

    constructor(
        private procedureBookingService: ProcedureBookingService,
        private procedureService: ProcedureService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
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
    }

    loadAll() {
        if (this.currentSearch) {
            this.procedureBookingService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        // if patient is selected, then only load bookings for patient
        if(this.selectedPatientId) {
            // find procedure bookings
            this.procedureBookingService.findByPatientId(this.selectedPatientId, {
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
            );
        } else {
            this.procedureBookingService.query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
            );
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        if(!this.selectedPatientId) {
            this.router.navigate(['/procedure-booking'], {queryParams:
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
        this.router.navigate(['/procedure-booking', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    search(query) {
        if (!query || query.length < 2) {
            return;
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/procedure-booking', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    //search(query) {
    //    if (!query || query.length < 2) {
    //        return;
    //    }
    //    this.page = 0;
    //    this.currentSearch = query;
    //    this.router.navigate(['/patient', {
    //        search: this.currentSearch,
    //        page: this.page,
    //        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    //    }]);
    //    this.loadAll();
    //}

    ngOnInit() {
        // load procedures lookup
        this.procedureService.allAsSelectOptions().subscribe((res: ResponseWrapper) => {
                this.proceduresLookup = _.indexBy(res.json, 'value');
            },
            (res: ResponseWrapper) => this.onError(res.json()));
        console.log("this.selectedPatientId  = " , this.selectedPatientId );
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProcedureBookings();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProcedureBooking) {
        return item.id;
    }

    registerChangeInProcedureBookings() {
        this.eventSubscriber = this.eventManager.subscribe('procedureBookingListModification', (response) => this.loadAll());
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
        this.procedureBookings = data;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
