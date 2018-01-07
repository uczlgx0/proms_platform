import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CareEventComponent } from './care-event.component';
import { CareEventDetailComponent } from './care-event-detail.component';
import { CareEventPopupComponent } from './care-event-dialog.component';
import { CareEventDeletePopupComponent } from './care-event-delete-dialog.component';

@Injectable()
export class CareEventResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const careEventRoute: Routes = [
    {
        path: 'care-event',
        component: CareEventComponent,
        resolve: {
            'pagingParams': CareEventResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CareEvents'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'care-event/:id',
        component: CareEventDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CareEvents'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const careEventPopupRoute: Routes = [
    {
        path: 'care-event-new',
        component: CareEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CareEvents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'care-event/:id/edit',
        component: CareEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CareEvents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'care-event/:id/delete',
        component: CareEventDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CareEvents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
