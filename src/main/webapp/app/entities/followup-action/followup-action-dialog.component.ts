import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FollowupAction, ActionType } from './followup-action.model';
import { FollowupActionPopupService } from './followup-action-popup.service';
import { FollowupActionService } from './followup-action.service';
import { FollowupPlan, FollowupPlanService } from '../followup-plan';
import { Patient, PatientService } from '../patient';
import { Questionnaire, QuestionnaireService } from '../questionnaire';
import { ResponseWrapper } from '../../shared';
import {IOption} from 'ng-select';

@Component({
    selector: 'jhi-followup-action-dialog',
    templateUrl: './followup-action-dialog.component.html'
})
export class FollowupActionDialogComponent implements OnInit {

    followupAction: FollowupAction;
    isSaving: boolean;
    patientId: string;
    questionnaireId: string;
    followupplans: FollowupPlan[];

    patients: Patient[];

    questionnaires: Questionnaire[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private followupActionService: FollowupActionService,
        private followupPlanService: FollowupPlanService,
        private patientService: PatientService,
        private questionnaireService: QuestionnaireService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.followupPlanService.query()
            .subscribe((res: ResponseWrapper) => { this.followupplans = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.patientService.allAsSelectOptions()
            .subscribe((res: ResponseWrapper) => { this.patients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.questionnaireService.allAsSelectOptions()
            .subscribe((res: ResponseWrapper) => { this.questionnaires = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.followupAction.id !== undefined) {
            this.subscribeToSaveResponse(
                this.followupActionService.update(this.followupAction));
        } else {
            this.subscribeToSaveResponse(
                this.followupActionService.create(this.followupAction));
        }
    }

    private subscribeToSaveResponse(result: Observable<FollowupAction>) {
        result.subscribe((res: FollowupAction) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: FollowupAction) {
        this.eventManager.broadcast({ name: 'followupActionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackFollowupPlanById(index: number, item: FollowupPlan) {
        return item.id;
    }

    trackPatientById(index: number, item: Patient) {
        return item.id;
    }

    trackQuestionnaireById(index: number, item: Questionnaire) {
        return item.id;
    }

    onPatientSelected(option: IOption) {
        if(!this.followupAction.patient) {
            this.followupAction.patient = new Patient();
        }
        this.followupAction.patient.id = parseInt(option.value);
    }

    onQuestionnaireSelected(option: IOption) {
        if(!this.followupAction.questionnaire) {
            this.followupAction.questionnaire = new Questionnaire();
        }
        this.followupAction.questionnaire.id = parseInt(option.value);
        this.followupAction.name = option.label;
        this.followupAction.type = ActionType['QUESTIONNAIRE'];
    }
}

@Component({
    selector: 'jhi-followup-action-popup',
    template: ''
})
export class FollowupActionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private followupActionPopupService: FollowupActionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.followupActionPopupService
                    .open(FollowupActionDialogComponent as Component, params['id']);
            } else {
                this.followupActionPopupService
                    .open(FollowupActionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
