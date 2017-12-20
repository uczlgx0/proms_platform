import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Procedure } from './procedure.model';
import { ProcedurePopupService } from './procedure-popup.service';
import { ProcedureService } from './procedure.service';

@Component({
    selector: 'jhi-procedure-delete-dialog',
    templateUrl: './procedure-delete-dialog.component.html'
})
export class ProcedureDeleteDialogComponent {

    procedure: Procedure;

    constructor(
        private procedureService: ProcedureService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.procedureService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'procedureListModification',
                content: 'Deleted an procedure'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-procedure-delete-popup',
    template: ''
})
export class ProcedureDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private procedurePopupService: ProcedurePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.procedurePopupService
                .open(ProcedureDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
