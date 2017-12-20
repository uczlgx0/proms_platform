import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { FollowupAction } from './followup-action.model';
import { FollowupActionService } from './followup-action.service';

@Injectable()
export class FollowupActionPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private followupActionService: FollowupActionService

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
                this.followupActionService.find(id).subscribe((followupAction) => {
                    followupAction.scheduledDate = this.datePipe
                        .transform(followupAction.scheduledDate, 'yyyy-MM-ddTHH:mm:ss');
                    followupAction.completedDate = this.datePipe
                        .transform(followupAction.completedDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.followupActionModalRef(component, followupAction);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.followupActionModalRef(component, new FollowupAction());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    followupActionModalRef(component: Component, followupAction: FollowupAction): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.followupAction = followupAction;
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
