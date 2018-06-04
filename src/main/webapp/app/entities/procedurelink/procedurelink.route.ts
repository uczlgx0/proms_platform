import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProcedurelinkComponent } from './procedurelink.component';
import { ProcedurelinkDetailComponent } from './procedurelink-detail.component';
import { ProcedurelinkPopupComponent } from './procedurelink-dialog.component';
import { ProcedurelinkDeletePopupComponent } from './procedurelink-delete-dialog.component';

@Injectable()
export class ProcedurelinkResolvePagingParams implements Resolve<any> {

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

export const procedurelinkRoute: Routes = [
    {
        path: 'procedurelink',
        component: ProcedurelinkComponent,
        resolve: {
            'pagingParams': ProcedurelinkResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedurelinks'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'procedurelink/:id',
        component: ProcedurelinkDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedurelinks'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const procedurelinkPopupRoute: Routes = [
    {
        path: 'procedurelink-new',
        component: ProcedurelinkPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedurelinks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedurelink/:id/edit',
        component: ProcedurelinkPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedurelinks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedurelink/:id/delete',
        component: ProcedurelinkDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Procedurelinks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
