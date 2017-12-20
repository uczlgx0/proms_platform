import { Injectable } from '@angular/core';

import { FollowupAction } from '../entities/followup-action/followup-action.model';
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
}
