import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { FollowupPlan } from './followup-plan.model';
import { FollowupPlanService } from './followup-plan.service';

@Component({
    selector: 'jhi-followup-plan-detail',
    templateUrl: './followup-plan-detail.component.html'
})
export class FollowupPlanDetailComponent implements OnInit, OnDestroy {

    followupPlan: FollowupPlan;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private followupPlanService: FollowupPlanService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFollowupPlans();
    }

    load(id) {
        this.followupPlanService.find(id).subscribe((followupPlan) => {
            this.followupPlan = followupPlan;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFollowupPlans() {
        this.eventSubscriber = this.eventManager.subscribe(
            'followupPlanListModification',
            (response) => this.load(this.followupPlan.id)
        );
    }
}
