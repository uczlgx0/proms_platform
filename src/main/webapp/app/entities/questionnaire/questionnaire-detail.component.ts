import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Questionnaire } from './questionnaire.model';
import { QuestionnaireService } from './questionnaire.service';

@Component({
    selector: 'jhi-questionnaire-detail',
    templateUrl: './questionnaire-detail.component.html'
})
export class QuestionnaireDetailComponent implements OnInit, OnDestroy {

    questionnaire: Questionnaire;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private questionnaireService: QuestionnaireService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInQuestionnaires();
    }

    load(id) {
        this.questionnaireService.find(id).subscribe((questionnaire) => {
            this.questionnaire = questionnaire;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInQuestionnaires() {
        this.eventSubscriber = this.eventManager.subscribe(
            'questionnaireListModification',
            (response) => this.load(this.questionnaire.id)
        );
    }
}
