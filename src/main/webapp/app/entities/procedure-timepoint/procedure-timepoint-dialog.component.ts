import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProcedureTimepoint } from './procedure-timepoint.model';
import { ProcedureTimepointPopupService } from './procedure-timepoint-popup.service';
import { ProcedureTimepointService } from './procedure-timepoint.service';
import { Procedure, ProcedureService } from '../procedure';
import { Timepoint, TimepointService } from '../timepoint';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-procedure-timepoint-dialog',
    templateUrl: './procedure-timepoint-dialog.component.html'
})
export class ProcedureTimepointDialogComponent implements OnInit {

    procedureTimepoint: ProcedureTimepoint;
    isSaving: boolean;

    procedures: Procedure[];

    timepoints: Timepoint[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private procedureTimepointService: ProcedureTimepointService,
        private procedureService: ProcedureService,
        private timepointService: TimepointService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.procedureService.query()
            .subscribe((res: ResponseWrapper) => { this.procedures = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.timepointService.query()
            .subscribe((res: ResponseWrapper) => { this.timepoints = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.procedureTimepoint.id !== undefined) {
            this.subscribeToSaveResponse(
                this.procedureTimepointService.update(this.procedureTimepoint));
        } else {
            this.subscribeToSaveResponse(
                this.procedureTimepointService.create(this.procedureTimepoint));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProcedureTimepoint>) {
        result.subscribe((res: ProcedureTimepoint) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProcedureTimepoint) {
        this.eventManager.broadcast({ name: 'procedureTimepointListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProcedureById(index: number, item: Procedure) {
        return item.id;
    }

    trackTimepointById(index: number, item: Timepoint) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-procedure-timepoint-popup',
    template: ''
})
export class ProcedureTimepointPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private procedureTimepointPopupService: ProcedureTimepointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.procedureTimepointPopupService
                    .open(ProcedureTimepointDialogComponent as Component, params['id']);
            } else {
                this.procedureTimepointPopupService
                    .open(ProcedureTimepointDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
