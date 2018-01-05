import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProcedureTimepoint } from './procedure-timepoint.model';
import { ProcedureTimepointPopupService } from './procedure-timepoint-popup.service';
import { ProcedureTimepointService } from './procedure-timepoint.service';

@Component({
    selector: 'jhi-procedure-timepoint-delete-dialog',
    templateUrl: './procedure-timepoint-delete-dialog.component.html'
})
export class ProcedureTimepointDeleteDialogComponent {

    procedureTimepoint: ProcedureTimepoint;

    constructor(
        private procedureTimepointService: ProcedureTimepointService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.procedureTimepointService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'procedureTimepointListModification',
                content: 'Deleted an procedureTimepoint'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-procedure-timepoint-delete-popup',
    template: ''
})
export class ProcedureTimepointDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private procedureTimepointPopupService: ProcedureTimepointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.procedureTimepointPopupService
                .open(ProcedureTimepointDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
