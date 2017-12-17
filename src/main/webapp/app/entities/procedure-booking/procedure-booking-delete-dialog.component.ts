import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProcedureBooking } from './procedure-booking.model';
import { ProcedureBookingPopupService } from './procedure-booking-popup.service';
import { ProcedureBookingService } from './procedure-booking.service';

@Component({
    selector: 'jhi-procedure-booking-delete-dialog',
    templateUrl: './procedure-booking-delete-dialog.component.html'
})
export class ProcedureBookingDeleteDialogComponent {

    procedureBooking: ProcedureBooking;

    constructor(
        private procedureBookingService: ProcedureBookingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.procedureBookingService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'procedureBookingListModification',
                content: 'Deleted an procedureBooking'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-procedure-booking-delete-popup',
    template: ''
})
export class ProcedureBookingDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private procedureBookingPopupService: ProcedureBookingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.procedureBookingPopupService
                .open(ProcedureBookingDeleteDialogComponent as Component, params['id'], false);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
