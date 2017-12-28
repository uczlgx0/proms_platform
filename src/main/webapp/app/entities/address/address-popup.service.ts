import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Address } from './address.model';
import { Patient } from '../patient/patient.model';
import { AddressService } from './address.service';

@Injectable()
export class AddressPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private addressService: AddressService

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
                    let address = new Address();
                    address.patient = new Patient();
                    address.patient.id = id;
                    // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                    setTimeout(() => {
                        this.ngbModalRef = this.addressModalRef(component, address, true);
                        resolve(this.ngbModalRef);
                    }, 0);
                } else {
                    // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                    setTimeout(() => {
                        this.ngbModalRef = this.addressModalRef(component, new Address(), false);
                        resolve(this.ngbModalRef);
                    }, 0);
                }
            } else {
                this.addressService.find(id).subscribe((address) => {
                    this.ngbModalRef = this.addressModalRef(component, address, true);
                    resolve(this.ngbModalRef);
                });
            }
        });
    }

    addressModalRef(component: Component, address: Address, hidePatients?: boolean | any): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.address = address;
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
