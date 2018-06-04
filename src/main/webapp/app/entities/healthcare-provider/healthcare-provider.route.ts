import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { HealthcareProviderComponent } from './healthcare-provider.component';
import { HealthcareProviderDetailComponent } from './healthcare-provider-detail.component';
import { HealthcareProviderPopupComponent } from './healthcare-provider-dialog.component';
import { HealthcareProviderDeletePopupComponent } from './healthcare-provider-delete-dialog.component';

@Injectable()
export class HealthcareProviderResolvePagingParams implements Resolve<any> {

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

export const healthcareProviderRoute: Routes = [
    {
        path: 'healthcare-provider',
        component: HealthcareProviderComponent,
        resolve: {
            'pagingParams': HealthcareProviderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HealthcareProviders'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'healthcare-provider/:id',
        component: HealthcareProviderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HealthcareProviders'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const healthcareProviderPopupRoute: Routes = [
    {
        path: 'healthcare-provider-new',
        component: HealthcareProviderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HealthcareProviders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'healthcare-provider/:id/edit',
        component: HealthcareProviderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HealthcareProviders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'healthcare-provider/:id/delete',
        component: HealthcareProviderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HealthcareProviders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
