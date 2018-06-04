import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProcedureTimepoint } from './procedure-timepoint.model';
import { ProcedureTimepointService } from './procedure-timepoint.service';

@Injectable()
export class ProcedureTimepointPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private procedureTimepointService: ProcedureTimepointService

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
                this.procedureTimepointService.find(id).subscribe((procedureTimepoint) => {
                    this.ngbModalRef = this.procedureTimepointModalRef(component, procedureTimepoint);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.procedureTimepointModalRef(component, new ProcedureTimepoint());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    procedureTimepointModalRef(component: Component, procedureTimepoint: ProcedureTimepoint): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.procedureTimepoint = procedureTimepoint;
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
