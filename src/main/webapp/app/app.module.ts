import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { NorthumbriapromsSharedModule, UserRouteAccessService } from './shared';
import { NorthumbriapromsAppRoutingModule} from './app-routing.module';
import { NorthumbriapromsHomeModule } from './home/home.module';
import { NorthumbriapromsAdminModule } from './admin/admin.module';
import { NorthumbriapromsAccountModule } from './account/account.module';
import { NorthumbriapromsEntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        NorthumbriapromsAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        NorthumbriapromsSharedModule,
        NorthumbriapromsHomeModule,
        NorthumbriapromsAdminModule,
        NorthumbriapromsAccountModule,
        NorthumbriapromsEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class NorthumbriapromsAppModule {}
