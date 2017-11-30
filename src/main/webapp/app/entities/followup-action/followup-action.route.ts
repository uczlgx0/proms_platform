import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FollowupActionComponent } from './followup-action.component';
import { FollowupActionDetailComponent } from './followup-action-detail.component';
import { FollowupActionPopupComponent } from './followup-action-dialog.component';
import { FollowupActionDeletePopupComponent } from './followup-action-delete-dialog.component';

@Injectable()
export class FollowupActionResolvePagingParams implements Resolve<any> {

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

export const followupActionRoute: Routes = [
    {
        path: 'followup-action',
        component: FollowupActionComponent,
        resolve: {
            'pagingParams': FollowupActionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupActions'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'followup-action/:id',
        component: FollowupActionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupActions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const followupActionPopupRoute: Routes = [
    {
        path: 'followup-action-new',
        component: FollowupActionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupActions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'followup-action/:id/edit',
        component: FollowupActionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupActions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'followup-action/:id/delete',
        component: FollowupActionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupActions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
