import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProcedureTimepointComponent } from './procedure-timepoint.component';
import { ProcedureTimepointDetailComponent } from './procedure-timepoint-detail.component';
import { ProcedureTimepointPopupComponent } from './procedure-timepoint-dialog.component';
import { ProcedureTimepointDeletePopupComponent } from './procedure-timepoint-delete-dialog.component';

@Injectable()
export class ProcedureTimepointResolvePagingParams implements Resolve<any> {

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

export const procedureTimepointRoute: Routes = [
    {
        path: 'procedure-timepoint',
        component: ProcedureTimepointComponent,
        resolve: {
            'pagingParams': ProcedureTimepointResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureTimepoints'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'procedure-timepoint/:id',
        component: ProcedureTimepointDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureTimepoints'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const procedureTimepointPopupRoute: Routes = [
    {
        path: 'procedure-timepoint-new',
        component: ProcedureTimepointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureTimepoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedure-timepoint/:id/edit',
        component: ProcedureTimepointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureTimepoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedure-timepoint/:id/delete',
        component: ProcedureTimepointDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureTimepoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
