import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { HealthcareProvider } from './healthcare-provider.model';
import { HealthcareProviderService } from './healthcare-provider.service';

@Component({
    selector: 'jhi-healthcare-provider-detail',
    templateUrl: './healthcare-provider-detail.component.html'
})
export class HealthcareProviderDetailComponent implements OnInit, OnDestroy {

    healthcareProvider: HealthcareProvider;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private healthcareProviderService: HealthcareProviderService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHealthcareProviders();
    }

    load(id) {
        this.healthcareProviderService.find(id).subscribe((healthcareProvider) => {
            this.healthcareProvider = healthcareProvider;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHealthcareProviders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'healthcareProviderListModification',
            (response) => this.load(this.healthcareProvider.id)
        );
    }
}
