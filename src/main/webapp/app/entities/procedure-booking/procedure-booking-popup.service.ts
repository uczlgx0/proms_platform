import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ProcedureBooking } from './procedure-booking.model';
import { ProcedureBookingService } from './procedure-booking.service';

@Injectable()
export class ProcedureBookingPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private procedureBookingService: ProcedureBookingService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.procedureBookingService.find(id).subscribe((procedureBooking) => {
                    procedureBooking.scheduledDate = this.datePipe
                        .transform(procedureBooking.scheduledDate, 'yyyy-MM-ddTHH:mm:ss');
                    procedureBooking.performedDate = this.datePipe
                        .transform(procedureBooking.performedDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.procedureBookingModalRef(component, procedureBooking);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.procedureBookingModalRef(component, new ProcedureBooking());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    procedureBookingModalRef(component: Component, procedureBooking: ProcedureBooking): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.procedureBooking = procedureBooking;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
