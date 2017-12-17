import { Route } from '@angular/router';

import { UserRouteAccessService } from '../shared';

import { StartComponent } from './';

export const START_ROUTE: Route = {
    path: 'start',
    component: StartComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Get started!'
    },
    canActivate: [UserRouteAccessService]
};
