import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable, Subscription } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { FollowupAction, ActionType } from './followup-action.model';
import { FollowupActionPopupService } from './followup-action-popup.service';
import { FollowupActionService } from './followup-action.service';
import { FollowupPlan, FollowupPlanService } from '../followup-plan';
import { Patient, PatientService } from '../patient';
import { ProcedureBooking, ProcedureBookingService } from '../procedure-booking';
import { Questionnaire, QuestionnaireService } from '../questionnaire';
import { ResponseWrapper } from '../../shared';
import {IOption} from 'ng-select';
import {IMyDpOptions} from 'mydatepicker';

@Component({
    selector: 'jhi-followup-action-dialog',
    templateUrl: './followup-action-dialog.component.html'
})
export class FollowupActionDialogComponent implements OnInit, OnDestroy {

    followupAction: FollowupAction;
    isSaving: boolean;
    patientId: string;
    questionnaireId: string;
    followupplans: FollowupPlan[];
    procedureBookings: any;
    selectedProcedureBooking: ProcedureBooking;
    selectedFollowupPlan: FollowupPlan;
    bookingLinks: any;
    bookingTotalItems: any;
    bookingQueryCount: any;
    eventSubscriber: Subscription;
    datePickerOptions: IMyDpOptions = {
        dateFormat: 'dd/mm/yyyy',
        minYear: 1850
    };
    patients: Patient[];
    questionnaires: Questionnaire[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private parseLinks: JhiParseLinks,
        private followupActionService: FollowupActionService,
        private followupPlanService: FollowupPlanService,
        private patientService: PatientService,
        private procedureBookingService: ProcedureBookingService,
        private questionnaireService: QuestionnaireService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.followupPlanService.query()
            .subscribe((res: ResponseWrapper) => { this.followupplans = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    filterInputChanged(event: any) {
        this.patientService.allAsSelectOptions({
            page: 0,
            query: event,
            size: 20})
            .subscribe((res: ResponseWrapper) => { this.patients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
        // rest existing values
        this.resetValues();
        // now set patient id
        if(!this.followupAction.patient) {
            this.followupAction.patient = new Patient();
        }
        this.followupAction.patient.id = parseInt(option.value);

        // now get procedure bookings
        this.loadBookings();
    }

    onQuestionnaireSelected(option: IOption) {
        if(!this.followupAction.questionnaire) {
            this.followupAction.questionnaire = new Questionnaire();
        }
        this.followupAction.questionnaire.id = parseInt(option.value);
        this.followupAction.name = option.label;
        this.followupAction.type = ActionType['QUESTIONNAIRE'];
    }

    onBookingSelected(booking: ProcedureBooking) {
        // first reset questionnaire related values
        this.followupAction.questionnaire = null;
        this.followupAction.name = null;
        this.followupAction.type = null;
        this.questionnaireId = null;

        // now process booking and reset questionnaires
        this.selectedProcedureBooking = booking;
        this.selectedFollowupPlan = this.selectedProcedureBooking.followupPlan;
        console.log("this.selectedFollowupPlan  = " , this.selectedFollowupPlan );
        this.followupAction.followupPlan = this.selectedFollowupPlan;
        // get questionnaires for procedure in booking
        this.loadQuestionnaires(booking);
    }

    private loadBookings() {
        // now get procedure bookings for patient
        this.procedureBookingService.findByPatientId(this.followupAction.patient.id,  {
            page: 0,
            size: 50,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onBookingsSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json));
        //this.formHeight = '500px';
    }

    private loadQuestionnaires(booking: ProcedureBooking) {
        this.questionnaireService.questinnairesForProcedureLocalCode(booking.primaryProcedure).subscribe(
            (res: ResponseWrapper) => this.questionnaires = res.json,
            (res: ResponseWrapper) => this.onError(res.json)
        );
        // update form height
        //this.formHeight = '750px';
    }

    private onBookingsSuccess(data, headers) {
        this.bookingLinks = this.parseLinks.parse(headers.get('link'));
        this.bookingTotalItems = headers.get('X-Total-Count');
        this.bookingQueryCount = this.bookingTotalItems;
        this.procedureBookings = data;
    }

    private sort() {
        return ['id,asc'];
    }

    private resetValues() {
        this.selectedProcedureBooking = null;
        this.selectedFollowupPlan = null;
        this.followupAction = new FollowupAction();
    }

    private registerChangeInProcedureBookings() {
        this.eventSubscriber = this.eventManager.subscribe('procedureBookingListModification', (response) => this.loadBookings());
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
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
