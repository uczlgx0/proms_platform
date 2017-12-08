import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FollowupAction } from '../entities/followup-action/followup-action.model';
//import { FollowupActionService } from '../entities/followup-action/followup-action.service';
import { ResponseWrapper } from '../shared';

@Component({
    selector: 'moxfq-component',
    templateUrl: './moxfq.component.html'
})
export class MoxfqComponent implements OnInit {

    @Input() followupAction: FollowupAction;
    isSaving: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        //private followupActionService: FollowupActionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
    }

    save() {
        this.isSaving = true;
        //if (this.followupAction.id !== undefined) {
        //    this.subscribeToSaveResponse(
        //        this.followupActionService.update(this.followupAction));
        //} else {
        //    this.subscribeToSaveResponse(
        //        this.followupActionService.create(this.followupAction));
        //}
    }

    private subscribeToSaveResponse(result: Observable<FollowupAction>) {
        result.subscribe((res: FollowupAction) =>
            this.onSaveSuccess(res), (res: Response) => this.onError(res));
    }

    private onSaveSuccess(result: FollowupAction) {
        this.eventManager.broadcast({ name: 'followupActionListModification', content: 'OK'});
        this.isSaving = false;
        //this.activeModal.dismiss(result);
    }

    //private onSaveError() {
    //    this.isSaving = false;
    //}

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
