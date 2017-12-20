import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Questionnaire } from './questionnaire.model';
import { QuestionnairePopupService } from './questionnaire-popup.service';
import { QuestionnaireService } from './questionnaire.service';

@Component({
    selector: 'jhi-questionnaire-dialog',
    templateUrl: './questionnaire-dialog.component.html'
})
export class QuestionnaireDialogComponent implements OnInit {

    questionnaire: Questionnaire;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private questionnaireService: QuestionnaireService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.questionnaire.id !== undefined) {
            this.subscribeToSaveResponse(
                this.questionnaireService.update(this.questionnaire));
        } else {
            this.subscribeToSaveResponse(
                this.questionnaireService.create(this.questionnaire));
        }
    }

    private subscribeToSaveResponse(result: Observable<Questionnaire>) {
        result.subscribe((res: Questionnaire) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Questionnaire) {
        this.eventManager.broadcast({ name: 'questionnaireListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-questionnaire-popup',
    template: ''
})
export class QuestionnairePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private questionnairePopupService: QuestionnairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.questionnairePopupService
                    .open(QuestionnaireDialogComponent as Component, params['id']);
            } else {
                this.questionnairePopupService
                    .open(QuestionnaireDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
