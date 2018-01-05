import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Router } from '@angular/router';
import * as _ from 'underscore';

import { Account, LoginModalService, Principal, ResponseWrapper, ITEMS_PER_PAGE } from '../shared';

import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { FollowupAction, ActionType, FollowupActionService, Query } from '../entities/followup-action';
import { FollowupPlan, FollowupPlanService } from '../entities/followup-plan';
import { Patient, PatientService } from '../entities/patient';
import { ProcedureBooking, ProcedureBookingService } from '../entities/procedure-booking';
import { ProcedureService } from '../entities/procedure/procedure.service';
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
    followupActions: FollowupAction[];
    procedureBookings: any;
    selectedProcedureBooking: ProcedureBooking;
    selectedFollowupPlan: FollowupPlan;
    bookingLinks: any;
    bookingTotalItems: any;
    bookingQueryCount: any;
    actionLinks: any;
    actionTotalItems: any;
    actionQueryCount: any;
    itemsPerPage: any;
    patients: Patient[];
    questionnaires: Questionnaire[];
    datePickerOptions: IMyDpOptions = {
        dateFormat: 'dd/mm/yyyy',
        minYear: 1850
    };
    proceduresLookup: any;
    query: Query;

    constructor(
        private jhiAlertService: JhiAlertService,
        private parseLinks: JhiParseLinks,
        private procedureService: ProcedureService,
        private followupPlanService: FollowupPlanService,
        private followupActionService: FollowupActionService,
        private patientService: PatientService,
        private procedureBookingService: ProcedureBookingService,
        private questionnaireService: QuestionnaireService,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.followupAction = new FollowupAction();
        this.query = new Query();
    }

    ngOnInit() {
        this.isSaving = false;
        // load procedures lookup
        this.procedureService.allAsSelectOptions().subscribe((res: ResponseWrapper) => {
                this.proceduresLookup = _.indexBy(res.json, 'value');
            },
            (res: ResponseWrapper) => this.onError(res.json())
        );
        this.followupPlanService.query()
            .subscribe((res: ResponseWrapper) => { this.followupplans = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    filterInputChanged(event: any) {
        this.patientService.allAsSelectOptions({
            page: 0,
            query: event,
            size: 20})
            .subscribe((res: ResponseWrapper) => { this.patients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    onPatientSelected(option: IOption) {
        // rest existing values
        this.resetValues();
        // now set patient id
        if(!this.followupAction.patient) {
            this.followupAction.patient = new Patient();
        }
        this.patientId = option.value;
        this.followupAction.patient.id = parseInt(this.patientId);

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
        console.log("booking  = " , booking );
        this.selectedProcedureBooking = booking;
        this.followupPlanService.findByProcedureBookingId(this.selectedProcedureBooking.id)
            .subscribe((followupPlan) => {
                this.selectedFollowupPlan = followupPlan;
                //this.selectedFollowupPlan = this.selectedProcedureBooking.followupPlan;
                console.log("this.selectedFollowupPlan  = " , this.selectedFollowupPlan );
                //this.followupAction.followupPlan = this.selectedFollowupPlan;
                // get questionnaires for procedure in booking
                this.loadQuestionnaires(booking);

                this.loadFollowupActions();
            }
        );
    }

    private loadFollowupActions() {
        this.query = new Query();
        this.query.token = '';
        this.query.patientIds = [];
        this.query.patientIds.push(this.patientId);
        this.query.statuses = [];
        this.query.statuses.push('STARTED');
        this.query.procedures = [];
        this.query.procedures.push(this.selectedProcedureBooking.primaryProcedure);
        this.followupActionService.search({
            page: 0,
            query: this.query,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    private loadBookings() {
        // now get procedure bookings for patient
        console.log("this.patientId  = " , this.patientId );
        console.log("this.followupAction  = " , this.followupAction );
        this.procedureBookingService.findByPatientId(this.patientId,  {
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

    private onSuccess(data, headers) {
        this.actionLinks = this.parseLinks.parse(headers.get('link'));
        this.actionTotalItems = headers.get('X-Total-Count');
        this.actionQueryCount = this.actionTotalItems;
        // this.page = pagingParams.page;
        this.followupActions = data.results;
        // assign values for actions
        for(let action of this.followupActions) {
            action.followupPlan = this.selectedFollowupPlan;
            action.patient.id = this.patientId;
            if (!action.type) {
                action.type = ActionType['QUESTIONNAIRE'];
            }
        }
        console.log("this.followupActions  = " , this.followupActions );
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
        //this.onBookingSelected(this.selectedProcedureBooking);
    }

    private registerChangeInProcedureBookings() {
        this.eventSubscriber = this.eventManager.subscribe('procedureBookingListModification', (response) => this.loadBookings());
    }

    private registerChangeInFollowupActions() {
        this.eventSubscriber = this.eventManager.subscribe('followupActionListModification', (response) => this.loadFollowupActions());
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
