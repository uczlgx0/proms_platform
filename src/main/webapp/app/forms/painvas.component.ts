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

@Component({
    selector: 'painvas-component',
    templateUrl: './painvas.component.html'
})
export class PainvasComponent implements OnInit {

    @Input() followupAction: FollowupAction;
    isSaving: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        private followupActionService: FollowupActionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        if(!this.followupAction.responseItems) {
            this.followupAction.responseItems = [];
        }
        // now set first item if missing
        if(this.followupAction.responseItems.length == 0) {
            let item:ResponseItem = new ResponseItem();
            item.followupActionId = this.followupAction.id;
            this.followupAction.responseItems.push(item);
        }
        this.followupAction.responseItems[0].question = 'How bad is your foot/ankel pain today?';
        this.followupAction.responseItems[0].localId = 0;
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

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
