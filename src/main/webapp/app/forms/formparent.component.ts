import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FollowupAction } from '../entities/followup-action/followup-action.model';
import { ResponseItem } from '../entities/followup-action/response-item.model';
import { FollowupActionService } from '../entities/followup-action/followup-action.service';
import { ResponseWrapper } from '../shared';
import { FormsService } from './forms.service';
import { IMyDate, IMyDateModel, IMyDpOptions } from 'mydatepicker';

export class FormParentComponent implements OnInit {

    @Input() followupAction: FollowupAction;
    @Input() isEditing: boolean;
    isSaving: boolean;
    formData: any;
    datePickerOptions: IMyDpOptions = {
        dateFormat: 'dd/mm/yyyy',
        minYear: 1850
    };
    selectedDate: IMyDate;

    constructor(
        public jhiAlertService: JhiAlertService,
        public followupActionService: FollowupActionService,
        public eventManager: JhiEventManager,
        public formsService: FormsService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.formData = {};
        if(this.followupAction.completedDate) {
            let completedDate: Date = new Date(this.followupAction.completedDate);
            this.selectedDate = {year: completedDate.getFullYear(),
                month: completedDate.getMonth() + 1,
                day: completedDate.getDate()};
        }
        if (this.followupAction.responseItems) {
            this.formsService.convertToFormData(this.followupAction.responseItems, this.formData);
        }
    }

    onDateChanged(event: IMyDateModel) {
        console.log("event  = " , event );
        this.selectedDate = event.date;
        this.followupAction.completedDate = event;
    }

    submitData(data: any) {
        this.followupAction = this.formsService.prepareFormData(this.followupAction, data);
        console.log("this.followupAction  = " , this.followupAction );
        this.save();
        this.isEditing = false;
    }

    save() {
        this.isSaving = true;
        this.subscribeToSaveResponse(this.followupActionService.update(this.followupAction));
    }

    private subscribeToSaveResponse(result: Observable<FollowupAction>) {
        result.subscribe((res: FollowupAction) =>
            this.onSaveSuccess(res), (res: Response) => this.onError(res));
    }

    private onSaveSuccess(result: FollowupAction) {
        console.log("result  = " , result );
        this.eventManager.broadcast({ name: 'followupActionListModification', content: result});
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
