import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { FollowupAction } from './followup-action.model';
import { FollowupActionPopupService } from './followup-action-popup.service';
import { FollowupActionService } from './followup-action.service';

@Component({
    selector: 'jhi-followup-action-delete-dialog',
    templateUrl: './followup-action-delete-dialog.component.html'
})
export class FollowupActionDeleteDialogComponent {

    followupAction: FollowupAction;

    constructor(
        private followupActionService: FollowupActionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.followupActionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'followupActionListModification',
                content: 'Deleted an followupAction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-followup-action-delete-popup',
    template: ''
})
export class FollowupActionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private followupActionPopupService: FollowupActionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.followupActionPopupService
                .open(FollowupActionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
