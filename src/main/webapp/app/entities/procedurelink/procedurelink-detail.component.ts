import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Procedurelink } from './procedurelink.model';
import { ProcedurelinkService } from './procedurelink.service';

@Component({
    selector: 'jhi-procedurelink-detail',
    templateUrl: './procedurelink-detail.component.html'
})
export class ProcedurelinkDetailComponent implements OnInit, OnDestroy {

    procedurelink: Procedurelink;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private procedurelinkService: ProcedurelinkService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProcedurelinks();
    }

    load(id) {
        this.procedurelinkService.find(id).subscribe((procedurelink) => {
            this.procedurelink = procedurelink;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProcedurelinks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'procedurelinkListModification',
            (response) => this.load(this.procedurelink.id)
        );
    }
}
