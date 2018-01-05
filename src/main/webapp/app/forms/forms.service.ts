import { Injectable } from '@angular/core';

import { FollowupAction, ActionStatus } from '../entities/followup-action';
import { ResponseItem } from '../entities/followup-action/response-item.model';

@Injectable()
export class FormsService {

    constructor() { }

    convertToResponseItem(key: string, value: string, followupAction: FollowupAction): ResponseItem {
        let responseItem = new ResponseItem();
        responseItem.followupActionId = followupAction.id;
        responseItem.localId = key;
        responseItem.value = parseInt(value);
        return responseItem;
    }

    convertToFormData(items: ResponseItem[], formData: any) {
        items.forEach(item => {
            formData[item.localId] = item.value;
        })
    }

    prepareFormData(followupAction: FollowupAction, formData: any): FollowupAction {
        console.log("form data  = " , formData );
        // loop through data keys and collect as response items
        let items: Array<ResponseItem> = [];
        Object.keys(formData).forEach((key) => {
            if(key != 'comment' && key != 'outcomeScore' && formData[key]) {
                items.push(this.convertToResponseItem(key, formData[key], followupAction));
            }
        });
        console.log("items  = " , items );
        followupAction.responseItems = items;
        followupAction.status = ActionStatus[ActionStatus.COMPLETED];
        console.log("followupAction  = " , followupAction );

        return followupAction;
    }
}
