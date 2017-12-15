import { Route } from '@angular/router';

import { StartComponent } from './';

export const START_ROUTE: Route = {
    path: 'start',
    component: StartComponent,
    data: {
        authorities: [],
        pageTitle: 'Get started!'
    }
};
