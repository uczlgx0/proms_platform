import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FollowupPlan } from './followup-plan.model';
import { FollowupPlanPopupService } from './followup-plan-popup.service';
import { FollowupPlanService } from './followup-plan.service';
import { ProcedureBooking, ProcedureBookingService } from '../procedure-booking';
import { Patient, PatientService } from '../patient';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-followup-plan-dialog',
    templateUrl: './followup-plan-dialog.component.html'
})
export class FollowupPlanDialogComponent implements OnInit {

    followupPlan: FollowupPlan;
    isSaving: boolean;

    procedurebookings: ProcedureBooking[];

    patients: Patient[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private followupPlanService: FollowupPlanService,
        private procedureBookingService: ProcedureBookingService,
        private patientService: PatientService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.procedureBookingService.query()
            .subscribe((res: ResponseWrapper) => { this.procedurebookings = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.patientService.query()
            .subscribe((res: ResponseWrapper) => { this.patients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.followupPlan.id !== undefined) {
            this.subscribeToSaveResponse(
                this.followupPlanService.update(this.followupPlan));
        } else {
            this.subscribeToSaveResponse(
                this.followupPlanService.create(this.followupPlan));
        }
    }

    private subscribeToSaveResponse(result: Observable<FollowupPlan>) {
        result.subscribe((res: FollowupPlan) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: FollowupPlan) {
        this.eventManager.broadcast({ name: 'followupPlanListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProcedureBookingById(index: number, item: ProcedureBooking) {
        return item.id;
    }

    trackPatientById(index: number, item: Patient) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-followup-plan-popup',
    template: ''
})
export class FollowupPlanPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private followupPlanPopupService: FollowupPlanPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.followupPlanPopupService
                    .open(FollowupPlanDialogComponent as Component, params['id']);
            } else {
                this.followupPlanPopupService
                    .open(FollowupPlanDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
