import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { QuestionnaireComponent } from './questionnaire.component';
import { QuestionnaireDetailComponent } from './questionnaire-detail.component';
import { QuestionnairePopupComponent } from './questionnaire-dialog.component';
import { QuestionnaireDeletePopupComponent } from './questionnaire-delete-dialog.component';

@Injectable()
export class QuestionnaireResolvePagingParams implements Resolve<any> {

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

export const questionnaireRoute: Routes = [
    {
        path: 'questionnaire',
        component: QuestionnaireComponent,
        resolve: {
            'pagingParams': QuestionnaireResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'questionnaire/:id',
        component: QuestionnaireDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const questionnairePopupRoute: Routes = [
    {
        path: 'questionnaire-new',
        component: QuestionnairePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'questionnaire/:id/edit',
        component: QuestionnairePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'questionnaire/:id/delete',
        component: QuestionnaireDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questionnaires'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
