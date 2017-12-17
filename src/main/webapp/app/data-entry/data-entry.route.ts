import { Route } from '@angular/router';
import { UserRouteAccessService } from '../shared';

import { DataEntryComponent } from './';

export const DATAENTRY_ROUTE: Route = {
    path: 'data-entry',
    component: DataEntryComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Data Entry!'
    },
    canActivate: [UserRouteAccessService]
};
