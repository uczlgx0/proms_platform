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
import { IMyDate, IMyDateModel, IMyDpOptions } from 'mydatepicker';

@Component({
    selector: 'painvas-component',
    templateUrl: './painvas.component.html'
})
export class PainvasComponent extends FormParentComponent implements OnInit {

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
        // if formData.q1 is not set, we set it to 0
        if(!this.formData['q1']) {
            this.formData['q1'] = 0;
        }
    }

    onChange(event: any, questionKey: string) {
        this.formData[questionKey] = event;
        this.calculateScore();
    }

    private calculateScore() {
        this.followupAction.outcomeScore = this.formData['q1'];
    }
}
