import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Router } from '@angular/router';
import * as _ from 'underscore';

import { Account, LoginModalService, Principal, ResponseWrapper } from '../shared';

import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { FollowupAction, ActionType, FollowupActionService } from '../entities/followup-action';
import { FollowupPlan, FollowupPlanService } from '../entities/followup-plan';
import { Patient, PatientService } from '../entities/patient';
import { ProcedureBooking, ProcedureBookingService } from '../entities/procedure-booking';
import { Questionnaire, QuestionnaireService } from '../entities/questionnaire';
import {IOption} from 'ng-select';
import { MoxfqComponent } from '../forms/moxfq.component';
import { PainvasComponent } from '../forms/painvas.component';
import {IMyDpOptions} from 'mydatepicker';

@Component({
    selector: 'data-entry-home',
    templateUrl: './data-entry.component.html',
    styleUrls: [
        'data-entry.css'
    ]

})
export class DataEntryComponent implements OnInit, OnDestroy {

    formHeight: string = '400px';
    eventSubscriber: Subscription;
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
    patients: Patient[];
    questionnaires: Questionnaire[];
    datePickerOptions: IMyDpOptions = {
        dateFormat: 'dd/mm/yyyy',
        minYear: 1850
    };

    constructor(
        private jhiAlertService: JhiAlertService,
        private parseLinks: JhiParseLinks,
        private followupActionService: FollowupActionService,
        private followupPlanService: FollowupPlanService,
        private patientService: PatientService,
        private procedureBookingService: ProcedureBookingService,
        private questionnaireService: QuestionnaireService,
        private eventManager: JhiEventManager
    ) {
        this.followupAction = new FollowupAction();
    }

    ngOnInit() {
        this.isSaving = false;
        this.followupPlanService.query()
            .subscribe((res: ResponseWrapper) => { this.followupplans = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.patientService.allAsSelectOptions()
            .subscribe((res: ResponseWrapper) => { this.patients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.questionnaireService.allAsSelectOptions()
            .subscribe((res: ResponseWrapper) => { this.questionnaires = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.registerChangeInProcedureBookings();
        this.registerChangeInFollowupActions();
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
        this.formHeight = '500px';
    }

    private loadQuestionnaires(booking: ProcedureBooking) {
        this.questionnaireService.questinnairesForProcedureLocalCode(booking.primaryProcedure).subscribe(
            (res: ResponseWrapper) => this.questionnaires = res.json,
            (res: ResponseWrapper) => this.onError(res.json)
        );
        // update form height
        this.formHeight = '750px';
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
        // update form height
        this.formHeight = '450px';
    }

    private registerChangeInProcedureBookings() {
        this.eventSubscriber = this.eventManager.subscribe('procedureBookingListModification', (response) => this.loadBookings());
    }

    private registerChangeInFollowupActions() {
        this.eventSubscriber = this.eventManager.subscribe('followupActionListModification', (response) => this.resetValues());
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
