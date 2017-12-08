import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Patient } from './patient.model';
import { PatientService } from './patient.service';

@Injectable()
export class PatientPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private patientService: PatientService

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
                this.patientService.find(id).subscribe((patient) => {
                    if (patient.birthDate) {
                        patient.birthDate = {
                            year: patient.birthDate.getFullYear(),
                            month: patient.birthDate.getMonth() + 1,
                            day: patient.birthDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.patientModalRef(component, patient);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.patientModalRef(component, new Patient());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    patientModalRef(component: Component, patient: Patient): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.patient = patient;
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
