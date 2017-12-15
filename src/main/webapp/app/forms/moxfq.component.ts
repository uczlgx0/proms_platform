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

@Component({
    selector: 'moxfq-component',
    templateUrl: './moxfq.component.html'
})
export class MoxfqComponent implements OnInit {

    @Input() followupAction: FollowupAction;
    isSaving: boolean;
    formData: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private followupActionService: FollowupActionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.formData = {};
        if (this.followupAction.responseItems) {
            this.convertToFormData(this.followupAction.responseItems);
        }
    }

    clear() {
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
        this.eventManager.broadcast({ name: 'followupActionListModification', content: 'OK'});
        this.isSaving = false;
    }

    submitData(data: any) {
        console.log("form data  = " , data );
        // loop through data keys and collect as response items
        let items: Array<ResponseItem> = [];
        Object.keys(data).forEach((key) => {
            if(key != 'comment' && data[key]) {
                items.push(this.convertToResponseItem(key, data[key]));
            }
        });
        console.log("items  = " , items );
        this.followupAction.responseItems = items;
        this.save();
    }

    private convertToResponseItem(key: string, value: string) {
        let responseItem = new ResponseItem();
        responseItem.followupActionId = this.followupAction.id;
        // process key which looks like 'qN' where N is the key we want
        let k = key.substr(1);
        responseItem.localId = parseInt(k);
        responseItem.value = parseInt(value);
        return responseItem;
    }

    private convertToFormData(items: ResponseItem[]) {
        items.forEach(item => {
            this.formData['q'+item.localId] = item.value;
        })
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
