import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Procedurelink } from './procedurelink.model';
import { ProcedurelinkPopupService } from './procedurelink-popup.service';
import { ProcedurelinkService } from './procedurelink.service';

@Component({
    selector: 'jhi-procedurelink-delete-dialog',
    templateUrl: './procedurelink-delete-dialog.component.html'
})
export class ProcedurelinkDeleteDialogComponent {

    procedurelink: Procedurelink;

    constructor(
        private procedurelinkService: ProcedurelinkService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.procedurelinkService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'procedurelinkListModification',
                content: 'Deleted an procedurelink'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-procedurelink-delete-popup',
    template: ''
})
export class ProcedurelinkDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private procedurelinkPopupService: ProcedurelinkPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.procedurelinkPopupService
                .open(ProcedurelinkDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
