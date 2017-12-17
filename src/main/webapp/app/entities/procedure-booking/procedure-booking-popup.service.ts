import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ProcedureBooking } from './procedure-booking.model';
import { Patient } from '../patient/patient.model';
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

    open(component: Component, id: number | any, createNew: boolean | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            console.log("id  = " , id );
            console.log("createNew  = " , createNew );

            if(createNew) {
                if (id) {
                    let procedureBooking = new ProcedureBooking();
                    procedureBooking.patient = new Patient();
                    procedureBooking.patient.id = id;
                    setTimeout(() => {
                        this.ngbModalRef = this.procedureBookingModalRef(component, procedureBooking, true);
                        resolve(this.ngbModalRef);
                    }, 0);

                    //this.procedureBookingService.find(id).subscribe((procedureBooking) => {
                    //    procedureBooking.scheduledDate = this.datePipe
                    //        .transform(procedureBooking.scheduledDate, 'yyyy-MM-ddTHH:mm:ss');
                    //    procedureBooking.performedDate = this.datePipe
                    //        .transform(procedureBooking.performedDate, 'yyyy-MM-ddTHH:mm:ss');
                    //    this.ngbModalRef = this.procedureBookingModalRef(component, procedureBooking);
                    //    resolve(this.ngbModalRef);
                    //});
                } else {
                    // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                    setTimeout(() => {
                        this.ngbModalRef = this.procedureBookingModalRef(component, new ProcedureBooking(), false);
                        resolve(this.ngbModalRef);
                    }, 0);
                }
            } else {
                this.procedureBookingService.find(id).subscribe((procedureBooking) => {
                    procedureBooking.scheduledDate = this.datePipe
                        .transform(procedureBooking.scheduledDate, 'yyyy-MM-ddTHH:mm:ss');
                    procedureBooking.performedDate = this.datePipe
                        .transform(procedureBooking.performedDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.procedureBookingModalRef(component, procedureBooking, true);
                    resolve(this.ngbModalRef);
                });
            }
        });
    }

    procedureBookingModalRef(component: Component, procedureBooking: ProcedureBooking, hidePatients?: boolean | any): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.procedureBooking = procedureBooking;
        if(hidePatients) {
            modalRef.componentInstance.hidePatients = hidePatients;
        }
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
