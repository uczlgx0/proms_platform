import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProcedureComponent } from './procedure.component';
import { ProcedureDetailComponent } from './procedure-detail.component';
import { ProcedurePopupComponent } from './procedure-dialog.component';
import { ProcedureDeletePopupComponent } from './procedure-delete-dialog.component';

@Injectable()
export class ProcedureResolvePagingParams implements Resolve<any> {

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

export const procedureRoute: Routes = [
    {
        path: 'procedure',
        component: ProcedureComponent,
        resolve: {
            'pagingParams': ProcedureResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedures'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'procedure/:id',
        component: ProcedureDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedures'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const procedurePopupRoute: Routes = [
    {
        path: 'procedure-new',
        component: ProcedurePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedures'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedure/:id/edit',
        component: ProcedurePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedures'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedure/:id/delete',
        component: ProcedureDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedures'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
