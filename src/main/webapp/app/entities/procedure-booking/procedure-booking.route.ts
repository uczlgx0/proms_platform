import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProcedureBookingComponent } from './procedure-booking.component';
import { ProcedureBookingDetailComponent } from './procedure-booking-detail.component';
import { ProcedureBookingPopupComponent } from './procedure-booking-dialog.component';
import { ProcedureBookingDeletePopupComponent } from './procedure-booking-delete-dialog.component';

@Injectable()
export class ProcedureBookingResolvePagingParams implements Resolve<any> {

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

export const procedureBookingRoute: Routes = [
    {
        path: 'procedure-booking',
        component: ProcedureBookingComponent,
        resolve: {
            'pagingParams': ProcedureBookingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureBookings'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'procedure-booking/:id',
        component: ProcedureBookingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureBookings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const procedureBookingPopupRoute: Routes = [
    {
        path: 'procedure-booking-new',
        component: ProcedureBookingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureBookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedure-booking/:id/edit',
        component: ProcedureBookingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureBookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'procedure-booking/:id/delete',
        component: ProcedureBookingDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProcedureBookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
