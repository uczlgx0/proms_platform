import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Timepoint } from './timepoint.model';
import { TimepointPopupService } from './timepoint-popup.service';
import { TimepointService } from './timepoint.service';

@Component({
    selector: 'jhi-timepoint-delete-dialog',
    templateUrl: './timepoint-delete-dialog.component.html'
})
export class TimepointDeleteDialogComponent {

    timepoint: Timepoint;

    constructor(
        private timepointService: TimepointService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.timepointService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'timepointListModification',
                content: 'Deleted an timepoint'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-timepoint-delete-popup',
    template: ''
})
export class TimepointDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timepointPopupService: TimepointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.timepointPopupService
                .open(TimepointDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
