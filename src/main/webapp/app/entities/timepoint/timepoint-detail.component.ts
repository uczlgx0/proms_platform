import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Timepoint } from './timepoint.model';
import { TimepointService } from './timepoint.service';

@Component({
    selector: 'jhi-timepoint-detail',
    templateUrl: './timepoint-detail.component.html'
})
export class TimepointDetailComponent implements OnInit, OnDestroy {

    timepoint: Timepoint;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private timepointService: TimepointService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTimepoints();
    }

    load(id) {
        this.timepointService.find(id).subscribe((timepoint) => {
            this.timepoint = timepoint;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTimepoints() {
        this.eventSubscriber = this.eventManager.subscribe(
            'timepointListModification',
            (response) => this.load(this.timepoint.id)
        );
    }
}
