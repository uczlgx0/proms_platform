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

@Component({
    selector: 'cofas-component',
    templateUrl: './cofas.component.html'
})
export class CofasComponent implements OnInit {

    @Input() followupAction: FollowupAction;
    isSaving: boolean;
    formData: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private followupActionService: FollowupActionService,
        private eventManager: JhiEventManager,
        private formsService: FormsService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.formData = {};
        if (this.followupAction.responseItems) {
            this.formsService.convertToFormData(this.followupAction.responseItems, this.formData);
        }
        // we need to set initial values of all fields that might be empty
        for(let i of ['1', '2', '5']) {
            let pKey = 'p' + i;
            // now verify if properties exist in form data or set value to 0
            if(!this.formData[pKey]) {
                this.formData[pKey] = 0;
            }
        }

        for(let i of ['1', '3', '6', '7', '9']) {
            let dKey = 'd' + i;
            // now verify if properties exist in form data or set value to 0
            if(!this.formData[dKey]) {
                this.formData[dKey] = 0;
            }
        }
    }

    onChange(event: any, questionKey: string) {
        this.formData[questionKey] = event;
    }

    submitData(data: any) {
        this.followupAction = this.formsService.prepareFormData(this.followupAction, data);
        this.save();
    }

    save() {
        this.isSaving = true;
        this.subscribeToSaveResponse(
            this.followupActionService.update(this.followupAction));
    }

    private subscribeToSaveResponse(result: Observable<FollowupAction>) {
        result.subscribe((res: FollowupAction) =>
            this.onSaveSuccess(res), (res: Response) => this.onError(res));
    }

    private onSaveSuccess(result: FollowupAction) {
        this.eventManager.broadcast({ name: 'followupActionListModification', content: result});
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
