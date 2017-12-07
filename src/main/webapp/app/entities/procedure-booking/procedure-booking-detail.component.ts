import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import * as _ from 'underscore';

import { ProcedureBooking } from './procedure-booking.model';
import { ProcedureBookingService } from './procedure-booking.service';
import { ProcedureService } from '../procedure/procedure.service';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-procedure-booking-detail',
    templateUrl: './procedure-booking-detail.component.html'
})
export class ProcedureBookingDetailComponent implements OnInit, OnDestroy {

    procedureBooking: ProcedureBooking;
    proceduresLookup: any;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        private procedureBookingService: ProcedureBookingService,
        private procedureService: ProcedureService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProcedureBookings();
    }

    load(id) {
        // load procedures lookup
        this.procedureService.allAsSelectOptions().subscribe((res: ResponseWrapper) => {
                this.proceduresLookup = _.indexBy(res.json, 'value');
            },
            (res: ResponseWrapper) => this.onError(res.json()));

        this.procedureBookingService.find(id).subscribe((procedureBooking) => {
            this.procedureBooking = procedureBooking;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProcedureBookings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'procedureBookingListModification',
            (response) => this.load(this.procedureBooking.id)
        );
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
