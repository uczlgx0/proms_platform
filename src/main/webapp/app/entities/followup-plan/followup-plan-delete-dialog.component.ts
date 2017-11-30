import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { FollowupPlan } from './followup-plan.model';
import { FollowupPlanPopupService } from './followup-plan-popup.service';
import { FollowupPlanService } from './followup-plan.service';

@Component({
    selector: 'jhi-followup-plan-delete-dialog',
    templateUrl: './followup-plan-delete-dialog.component.html'
})
export class FollowupPlanDeleteDialogComponent {

    followupPlan: FollowupPlan;

    constructor(
        private followupPlanService: FollowupPlanService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.followupPlanService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'followupPlanListModification',
                content: 'Deleted an followupPlan'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-followup-plan-delete-popup',
    template: ''
})
export class FollowupPlanDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private followupPlanPopupService: FollowupPlanPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.followupPlanPopupService
                .open(FollowupPlanDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
