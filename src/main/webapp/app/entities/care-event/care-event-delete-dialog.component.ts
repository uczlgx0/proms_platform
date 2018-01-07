import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CareEvent } from './care-event.model';
import { CareEventPopupService } from './care-event-popup.service';
import { CareEventService } from './care-event.service';

@Component({
    selector: 'jhi-care-event-delete-dialog',
    templateUrl: './care-event-delete-dialog.component.html'
})
export class CareEventDeleteDialogComponent {

    careEvent: CareEvent;

    constructor(
        private careEventService: CareEventService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.careEventService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'careEventListModification',
                content: 'Deleted an careEvent'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-care-event-delete-popup',
    template: ''
})
export class CareEventDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private careEventPopupService: CareEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.careEventPopupService
                .open(CareEventDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
