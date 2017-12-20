import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Questionnaire } from './questionnaire.model';
import { QuestionnairePopupService } from './questionnaire-popup.service';
import { QuestionnaireService } from './questionnaire.service';

@Component({
    selector: 'jhi-questionnaire-delete-dialog',
    templateUrl: './questionnaire-delete-dialog.component.html'
})
export class QuestionnaireDeleteDialogComponent {

    questionnaire: Questionnaire;

    constructor(
        private questionnaireService: QuestionnaireService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.questionnaireService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'questionnaireListModification',
                content: 'Deleted an questionnaire'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-questionnaire-delete-popup',
    template: ''
})
export class QuestionnaireDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private questionnairePopupService: QuestionnairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.questionnairePopupService
                .open(QuestionnaireDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
