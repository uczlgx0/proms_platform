import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { CareEvent } from './care-event.model';
import { CareEventService } from './care-event.service';

@Component({
    selector: 'jhi-care-event-detail',
    templateUrl: './care-event-detail.component.html'
})
export class CareEventDetailComponent implements OnInit, OnDestroy {

    careEvent: CareEvent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private careEventService: CareEventService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCareEvents();
    }

    load(id) {
        this.careEventService.find(id).subscribe((careEvent) => {
            this.careEvent = careEvent;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCareEvents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'careEventListModification',
            (response) => this.load(this.careEvent.id)
        );
    }
}
