import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Patient } from './patient.model';
import { PatientService } from './patient.service';
import { ProcedureBooking } from '../procedure-booking/procedure-booking.model';
import { ProcedureBookingService } from '../procedure-booking/procedure-booking.service';
import { FollowupAction } from '../followup-action/followup-action.model';
import { FollowupActionService } from '../followup-action/followup-action.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-patient-detail',
    templateUrl: './patient-detail.component.html'
})
export class PatientDetailComponent implements OnInit, OnDestroy {

    patient: Patient;
    procedureBookings: Array<ProcedureBooking>;
    followupActions: Array<FollowupAction>;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    routeData: any;
    bookingLinks: any;
    bookingTotalItems: any;
    bookingQueryCount: any;
    followupgLinks: any;
    followupTotalItems: any;
    followupQueryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private patientService: PatientService,
        private procedureBookingService: ProcedureBookingService,
        private followupActionService: FollowupActionService,
        private route: ActivatedRoute
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPatients();
    }

    load(id) {
        this.patientService.find(id).subscribe((patient) => {
            this.patient = patient;
            console.log("this.patient  = " , this.patient );
        });
        // find procedure bookings
        this.procedureBookingService.findByPatientId(id, {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onBookingsSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
        // find procedure bookings
        this.followupActionService.findByPatientId(id, {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onFollowupSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPatients() {
        this.eventSubscriber = this.eventManager.subscribe(
            'patientListModification',
            (response) => this.load(this.patient.id)
        );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onBookingsSuccess(data, headers) {
        this.bookingLinks = this.parseLinks.parse(headers.get('link'));
        this.bookingTotalItems = headers.get('X-Total-Count');
        this.bookingQueryCount = this.bookingTotalItems;
        // this.page = pagingParams.page;
        this.procedureBookings = data;
    }

    private onFollowupSuccess(data, headers) {
        this.followupgLinks = this.parseLinks.parse(headers.get('link'));
        this.followupTotalItems = headers.get('X-Total-Count');
        this.followupQueryCount = this.followupTotalItems;
        // this.page = pagingParams.page;
        this.followupActions = data;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
