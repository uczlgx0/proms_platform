import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TimepointComponent } from './timepoint.component';
import { TimepointDetailComponent } from './timepoint-detail.component';
import { TimepointPopupComponent } from './timepoint-dialog.component';
import { TimepointDeletePopupComponent } from './timepoint-delete-dialog.component';

@Injectable()
export class TimepointResolvePagingParams implements Resolve<any> {

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

export const timepointRoute: Routes = [
    {
        path: 'timepoint',
        component: TimepointComponent,
        resolve: {
            'pagingParams': TimepointResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Timepoints'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'timepoint/:id',
        component: TimepointDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Timepoints'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timepointPopupRoute: Routes = [
    {
        path: 'timepoint-new',
        component: TimepointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Timepoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'timepoint/:id/edit',
        component: TimepointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Timepoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'timepoint/:id/delete',
        component: TimepointDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Timepoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
