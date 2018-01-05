import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Timepoint } from './timepoint.model';
import { TimepointPopupService } from './timepoint-popup.service';
import { TimepointService } from './timepoint.service';

@Component({
    selector: 'jhi-timepoint-dialog',
    templateUrl: './timepoint-dialog.component.html'
})
export class TimepointDialogComponent implements OnInit {

    timepoint: Timepoint;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private timepointService: TimepointService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.timepoint.id !== undefined) {
            this.subscribeToSaveResponse(
                this.timepointService.update(this.timepoint));
        } else {
            this.subscribeToSaveResponse(
                this.timepointService.create(this.timepoint));
        }
    }

    private subscribeToSaveResponse(result: Observable<Timepoint>) {
        result.subscribe((res: Timepoint) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Timepoint) {
        this.eventManager.broadcast({ name: 'timepointListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-timepoint-popup',
    template: ''
})
export class TimepointPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timepointPopupService: TimepointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.timepointPopupService
                    .open(TimepointDialogComponent as Component, params['id']);
            } else {
                this.timepointPopupService
                    .open(TimepointDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
