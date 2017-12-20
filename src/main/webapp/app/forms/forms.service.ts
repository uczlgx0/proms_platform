import { Injectable } from '@angular/core';

import { FollowupAction } from '../entities/followup-action/followup-action.model';
import { ResponseItem } from '../entities/followup-action/response-item.model';

@Injectable()
export class FormsService {

    constructor() { }

    convertToResponseItem(key: string, value: string, followupAction: FollowupAction): ResponseItem {
        let responseItem = new ResponseItem();
        responseItem.followupActionId = followupAction.id;
        // process key which looks like 'qN' where N is the key we want
        let k = key.substr(1);
        responseItem.localId = parseInt(k);
        responseItem.value = parseInt(value);
        return responseItem;
    }

    convertToFormData(items: ResponseItem[], formData: any) {
        items.forEach(item => {
            formData['q'+item.localId] = item.value;
        })
    }
}
