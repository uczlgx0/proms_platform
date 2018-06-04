import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { HealthcareProvider } from './healthcare-provider.model';
import { HealthcareProviderPopupService } from './healthcare-provider-popup.service';
import { HealthcareProviderService } from './healthcare-provider.service';

@Component({
    selector: 'jhi-healthcare-provider-dialog',
    templateUrl: './healthcare-provider-dialog.component.html'
})
export class HealthcareProviderDialogComponent implements OnInit {

    healthcareProvider: HealthcareProvider;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private healthcareProviderService: HealthcareProviderService,
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
        if (this.healthcareProvider.id !== undefined) {
            this.subscribeToSaveResponse(
                this.healthcareProviderService.update(this.healthcareProvider));
        } else {
            this.subscribeToSaveResponse(
                this.healthcareProviderService.create(this.healthcareProvider));
        }
    }

    private subscribeToSaveResponse(result: Observable<HealthcareProvider>) {
        result.subscribe((res: HealthcareProvider) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: HealthcareProvider) {
        this.eventManager.broadcast({ name: 'healthcareProviderListModification', content: 'OK'});
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
    selector: 'jhi-healthcare-provider-popup',
    template: ''
})
export class HealthcareProviderPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private healthcareProviderPopupService: HealthcareProviderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.healthcareProviderPopupService
                    .open(HealthcareProviderDialogComponent as Component, params['id']);
            } else {
                this.healthcareProviderPopupService
                    .open(HealthcareProviderDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
