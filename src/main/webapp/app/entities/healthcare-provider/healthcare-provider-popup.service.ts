import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HealthcareProvider } from './healthcare-provider.model';
import { HealthcareProviderService } from './healthcare-provider.service';

@Injectable()
export class HealthcareProviderPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private healthcareProviderService: HealthcareProviderService

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
                this.healthcareProviderService.find(id).subscribe((healthcareProvider) => {
                    this.ngbModalRef = this.healthcareProviderModalRef(component, healthcareProvider);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.healthcareProviderModalRef(component, new HealthcareProvider());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    healthcareProviderModalRef(component: Component, healthcareProvider: HealthcareProvider): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.healthcareProvider = healthcareProvider;
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
