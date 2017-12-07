import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { HealthcareProvider } from './healthcare-provider.model';
import { HealthcareProviderPopupService } from './healthcare-provider-popup.service';
import { HealthcareProviderService } from './healthcare-provider.service';

@Component({
    selector: 'jhi-healthcare-provider-delete-dialog',
    templateUrl: './healthcare-provider-delete-dialog.component.html'
})
export class HealthcareProviderDeleteDialogComponent {

    healthcareProvider: HealthcareProvider;

    constructor(
        private healthcareProviderService: HealthcareProviderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.healthcareProviderService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'healthcareProviderListModification',
                content: 'Deleted an healthcareProvider'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-healthcare-provider-delete-popup',
    template: ''
})
export class HealthcareProviderDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private healthcareProviderPopupService: HealthcareProviderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.healthcareProviderPopupService
                .open(HealthcareProviderDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
