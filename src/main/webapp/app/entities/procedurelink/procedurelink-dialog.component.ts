import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Procedurelink } from './procedurelink.model';
import { ProcedurelinkPopupService } from './procedurelink-popup.service';
import { ProcedurelinkService } from './procedurelink.service';
import { Procedure, ProcedureService } from '../procedure';
import { Questionnaire, QuestionnaireService } from '../questionnaire';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-procedurelink-dialog',
    templateUrl: './procedurelink-dialog.component.html'
})
export class ProcedurelinkDialogComponent implements OnInit {

    procedurelink: Procedurelink;
    isSaving: boolean;

    procedures: Procedure[];

    questionnaires: Questionnaire[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private procedurelinkService: ProcedurelinkService,
        private procedureService: ProcedureService,
        private questionnaireService: QuestionnaireService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.procedureService.query()
            .subscribe((res: ResponseWrapper) => { this.procedures = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.questionnaireService.query()
            .subscribe((res: ResponseWrapper) => { this.questionnaires = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.procedurelink.id !== undefined) {
            this.subscribeToSaveResponse(
                this.procedurelinkService.update(this.procedurelink));
        } else {
            this.subscribeToSaveResponse(
                this.procedurelinkService.create(this.procedurelink));
        }
    }

    private subscribeToSaveResponse(result: Observable<Procedurelink>) {
        result.subscribe((res: Procedurelink) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Procedurelink) {
        this.eventManager.broadcast({ name: 'procedurelinkListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProcedureById(index: number, item: Procedure) {
        return item.id;
    }

    trackQuestionnaireById(index: number, item: Questionnaire) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-procedurelink-popup',
    template: ''
})
export class ProcedurelinkPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private procedurelinkPopupService: ProcedurelinkPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.procedurelinkPopupService
                    .open(ProcedurelinkDialogComponent as Component, params['id']);
            } else {
                this.procedurelinkPopupService
                    .open(ProcedurelinkDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
