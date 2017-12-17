import { Route } from '@angular/router';

import { DataEntryComponent } from './';

export const DATAENTRY_ROUTE: Route = {
    path: 'data-entry',
    component: DataEntryComponent,
    data: {
        authorities: [],
        pageTitle: 'Data Entry!'
    }
};
