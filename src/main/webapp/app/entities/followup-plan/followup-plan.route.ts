import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FollowupPlanComponent } from './followup-plan.component';
import { FollowupPlanDetailComponent } from './followup-plan-detail.component';
import { FollowupPlanPopupComponent } from './followup-plan-dialog.component';
import { FollowupPlanDeletePopupComponent } from './followup-plan-delete-dialog.component';

@Injectable()
export class FollowupPlanResolvePagingParams implements Resolve<any> {

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

export const followupPlanRoute: Routes = [
    {
        path: 'followup-plan',
        component: FollowupPlanComponent,
        resolve: {
            'pagingParams': FollowupPlanResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupPlans'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'followup-plan/:id',
        component: FollowupPlanDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupPlans'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const followupPlanPopupRoute: Routes = [
    {
        path: 'followup-plan-new',
        component: FollowupPlanPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupPlans'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'followup-plan/:id/edit',
        component: FollowupPlanPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupPlans'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'followup-plan/:id/delete',
        component: FollowupPlanDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FollowupPlans'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
