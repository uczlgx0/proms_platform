import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CareEvent } from './care-event.model';
import { CareEventPopupService } from './care-event-popup.service';
import { CareEventService } from './care-event.service';
import { Timepoint, TimepointService } from '../timepoint';
import { Patient, PatientService } from '../patient';
import { FollowupPlan, FollowupPlanService } from '../followup-plan';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-care-event-dialog',
    templateUrl: './care-event-dialog.component.html'
})
export class CareEventDialogComponent implements OnInit {

    careEvent: CareEvent;
    isSaving: boolean;

    timepoints: Timepoint[];

    patients: Patient[];

    followupplans: FollowupPlan[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private careEventService: CareEventService,
        private timepointService: TimepointService,
        private patientService: PatientService,
        private followupPlanService: FollowupPlanService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.timepointService.query()
            .subscribe((res: ResponseWrapper) => { this.timepoints = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.patientService.query()
            .subscribe((res: ResponseWrapper) => { this.patients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.followupPlanService.query()
            .subscribe((res: ResponseWrapper) => { this.followupplans = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.careEvent.id !== undefined) {
            this.subscribeToSaveResponse(
                this.careEventService.update(this.careEvent));
        } else {
            this.subscribeToSaveResponse(
                this.careEventService.create(this.careEvent));
        }
    }

    private subscribeToSaveResponse(result: Observable<CareEvent>) {
        result.subscribe((res: CareEvent) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CareEvent) {
        this.eventManager.broadcast({ name: 'careEventListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTimepointById(index: number, item: Timepoint) {
        return item.id;
    }

    trackPatientById(index: number, item: Patient) {
        return item.id;
    }

    trackFollowupPlanById(index: number, item: FollowupPlan) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-care-event-popup',
    template: ''
})
export class CareEventPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private careEventPopupService: CareEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.careEventPopupService
                    .open(CareEventDialogComponent as Component, params['id']);
            } else {
                this.careEventPopupService
                    .open(CareEventDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
