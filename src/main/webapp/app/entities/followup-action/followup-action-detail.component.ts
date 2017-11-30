import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FollowupAction } from './followup-action.model';
import { FollowupActionService } from './followup-action.service';

@Component({
    selector: 'jhi-followup-action-detail',
    templateUrl: './followup-action-detail.component.html'
})
export class FollowupActionDetailComponent implements OnInit, OnDestroy {

    followupAction: FollowupAction;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private followupActionService: FollowupActionService,
        private jhiAlertService: JhiAlertService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFollowupActions();
    }

    load(id) {
        this.followupActionService.find(id).subscribe((followupAction) => {
            this.followupAction = followupAction;
        });
    }

    previousState() {
        window.history.back();
    }

    initiate() {
        this.jhiAlertService.info('Launching followup action...', null, null);
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFollowupActions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'followupActionListModification',
            (response) => this.load(this.followupAction.id)
        );
    }
}
