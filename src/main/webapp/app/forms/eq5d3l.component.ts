import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { FormsModule, FormGroup, FormControl } from '@angular/forms';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FollowupAction } from '../entities/followup-action/followup-action.model';
import { ResponseItem } from '../entities/followup-action/response-item.model';
import { FollowupActionService } from '../entities/followup-action/followup-action.service';
import { ResponseWrapper } from '../shared';
import { FormsService } from './forms.service';

@Component({
    selector: 'eq5d3l-component',
    templateUrl: './eq5d3l.component.html'
})
export class Eq5d3lComponent implements OnInit {

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

    submitData(data: any) {
        this.followupAction = this.formsService.prepareFormData(this.followupAction, data);
        this.save();
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
