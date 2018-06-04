import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ProcedureTimepoint } from './procedure-timepoint.model';
import { ProcedureTimepointService } from './procedure-timepoint.service';

@Component({
    selector: 'jhi-procedure-timepoint-detail',
    templateUrl: './procedure-timepoint-detail.component.html'
})
export class ProcedureTimepointDetailComponent implements OnInit, OnDestroy {

    procedureTimepoint: ProcedureTimepoint;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private procedureTimepointService: ProcedureTimepointService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProcedureTimepoints();
    }

    load(id) {
        this.procedureTimepointService.find(id).subscribe((procedureTimepoint) => {
            this.procedureTimepoint = procedureTimepoint;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProcedureTimepoints() {
        this.eventSubscriber = this.eventManager.subscribe(
            'procedureTimepointListModification',
            (response) => this.load(this.procedureTimepoint.id)
        );
    }
}
