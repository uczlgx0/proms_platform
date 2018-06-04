import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FormParentComponent } from './formparent.component';
import { FollowupAction } from '../entities/followup-action/followup-action.model';
import { ResponseItem } from '../entities/followup-action/response-item.model';
import { FollowupActionService } from '../entities/followup-action/followup-action.service';
import { ResponseWrapper } from '../shared';
import { FormsService } from './forms.service';

@Component({
    selector: 'aos-component',
    templateUrl: './aos.component.html'
})
export class AosComponent extends FormParentComponent implements OnInit {

    @Input() followupAction: FollowupAction;
    isSaving: boolean;
    formData: any;

    constructor(
        public jhiAlertService: JhiAlertService,
        public followupActionService: FollowupActionService,
        public eventManager: JhiEventManager,
        public formsService: FormsService
    ) {
        super(jhiAlertService, followupActionService, eventManager, formsService);
    }

    ngOnInit() {
        super.ngOnInit();
        //this.isSaving = false;
        //this.formData = {};
        //if (this.followupAction.responseItems) {
        //    this.formsService.convertToFormData(this.followupAction.responseItems, this.formData);
        //}
        // we need to set initial values of all fields that might be empty
        for(var i=1; i<10; i++) {
            let pKey = 'p' + i;
            let dKey = 'd' + i;
            // now verify if properties exist in form data or set value to 0
            if(!this.formData[pKey]) {
                this.formData[pKey] = 0;
            }
            if(!this.formData[dKey]) {
                this.formData[dKey] = 0;
            }
        }
    }

    onChange(event: any, questionKey: string) {
        this.formData[questionKey] = event;
    }

    //submitData(data: any) {
    //    this.followupAction = this.formsService.prepareFormData(this.followupAction, data);
    //    this.save();
    //}
    //
    //save() {
    //    this.isSaving = true;
    //    this.subscribeToSaveResponse(
    //        this.followupActionService.update(this.followupAction));
    //}
    //
    //private subscribeToSaveResponse(result: Observable<FollowupAction>) {
    //    result.subscribe((res: FollowupAction) =>
    //        this.onSaveSuccess(res), (res: Response) => this.onError(res));
    //}
    //
    //private onSaveSuccess(result: FollowupAction) {
    //    this.eventManager.broadcast({ name: 'followupActionListModification', content: result});
    //    this.isSaving = false;
    //}
    //
    //private onError(error: any) {
    //    this.jhiAlertService.error(error.message, null, null);
    //}
}
