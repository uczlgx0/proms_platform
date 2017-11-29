import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProcedureBooking } from './procedure-booking.model';
import { ProcedureBookingPopupService } from './procedure-booking-popup.service';
import { ProcedureBookingService } from './procedure-booking.service';
import { Patient, PatientService } from '../patient';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-procedure-booking-dialog',
    templateUrl: './procedure-booking-dialog.component.html'
})
export class ProcedureBookingDialogComponent implements OnInit {

    procedureBooking: ProcedureBooking;
    isSaving: boolean;

    patients: Patient[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private procedureBookingService: ProcedureBookingService,
        private patientService: PatientService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.patientService.query()
            .subscribe((res: ResponseWrapper) => { this.patients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.procedureBooking.id !== undefined) {
            this.subscribeToSaveResponse(
                this.procedureBookingService.update(this.procedureBooking));
        } else {
            this.subscribeToSaveResponse(
                this.procedureBookingService.create(this.procedureBooking));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProcedureBooking>) {
        result.subscribe((res: ProcedureBooking) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProcedureBooking) {
        this.eventManager.broadcast({ name: 'procedureBookingListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPatientById(index: number, item: Patient) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-procedure-booking-popup',
    template: ''
})
export class ProcedureBookingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private procedureBookingPopupService: ProcedureBookingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.procedureBookingPopupService
                    .open(ProcedureBookingDialogComponent as Component, params['id']);
            } else {
                this.procedureBookingPopupService
                    .open(ProcedureBookingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
