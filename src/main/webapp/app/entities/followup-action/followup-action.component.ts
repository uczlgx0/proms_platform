import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import * as _ from 'underscore';

import { FollowupAction } from './followup-action.model';
import { Query } from './query.model';
import { Category } from './category.model';
import { FollowupActionService } from './followup-action.service';
import { ProcedureService } from '../procedure/procedure.service';
import { HealthcareProviderService } from '../healthcare-provider/healthcare-provider.service';
import { UserService } from '../../shared/user/user.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {IOption} from 'ng-select';

@Component({
    selector: 'jhi-followup-action',
    templateUrl: './followup-action.component.html',
    styleUrls: [
        'followup-action.css'
    ]
})
export class FollowupActionComponent implements OnInit, OnDestroy {

    currentAccount: any;
    followupActions: FollowupAction[];
    categories: Category[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    @Input() selectedPatientId;
    proceduresLookup: any;
    query: Query;
    model: any = {};
    includeStarted: boolean = true;
    includePending: boolean = false;
    includeUninitialised: boolean = false;
    consultants: Array<IOption>;
    locations: Array<IOption>;
    procedures: Array<IOption>;
    genders: Array<IOption>;
    sides: Array<IOption>;
    phases: Array<IOption>;
    statuses: Array<IOption>;
    selectedConsultant: any;
    selectedLocation: any;
    selectedProcedure: any;
    selectedGender: any;
    selectedSide: any;
    selectedStatus: any;
    selectedPhase: any;
    ageRange = [10, 100];

    constructor(
        private followupActionService: FollowupActionService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private healthcareProviderService: HealthcareProviderService,
        private router: Router,
        private procedureService: ProcedureService,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    ngOnInit() {
        this.query = new Query();
        this.query.statuses = [];
        this.query.statuses.push('STARTED');
        this.selectedStatus = 'STARTED';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

        // load consultants
        this.userService.consultants()
            .subscribe((res: ResponseWrapper) => {this.consultants = res.json; }, (res: ResponseWrapper) => this.onError(res.json()));
        // load locations
        this.healthcareProviderService.allAsSelectOptions()
            .subscribe((res: ResponseWrapper) => {this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json()));
        // load procedures
        this.procedureService.allAsSelectOptions()
            .subscribe((res: ResponseWrapper) => {
                this.procedures = res.json;
                this.proceduresLookup = _.indexBy(res.json, 'value');
            },
            (res: ResponseWrapper) => this.onError(res.json()));
        // set genders
        this.genders =[{value: 'MALE', label: 'Male'}, {value: 'FEMALE', label: 'Female'}];
        this.sides =[{value: 'LEFT', label: 'Left'}, {value: 'RIGHT', label: 'Right'}];
        this.phases =[{value: 'PRE_OPERATIVE', label: 'Pre Op'}, {value: 'POST_OPERATIVE', label: 'Post Op'}];
        this.statuses =[{value: 'STARTED', label: 'Started'}, {value: 'PENDING', label: 'Pending'}, {value: 'UNKNOWN', label: 'Unknown'}, {value: 'UNINITIALISED', label: 'Uninitialised'}];

        this.registerChangeInFollowupActions();
        this.registerChangeInBookings();
    }

    loadAll() {
        // update min and max ages for query
        this.query.minAge = this.ageRange[0];
        this.query.maxAge = this.ageRange[1];
        // always set status to completed
        //this.query.statuses = [];
        //this.query.statuses.push('COMPLETED');

        if (this.currentSearch) {
            //this.currentSearch = '';
            this.query.token = this.currentSearch;
        } else {
            this.query.token = '';
        }

        if(this.selectedPatientId) {
            this.query.patientIds = [];
            this.query.patientIds.push(this.selectedPatientId);
        } else {
            this.query.patientIds = [];
        }

        console.log("this.query  = " , this.query );
        this.followupActionService.search({
            page: this.page - 1,
            query: this.query,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        if (!this.selectedPatientId) {
            this.router.navigate(['/followup-action'], {
                queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
            });
        }
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        //this.router.navigate(['/followup-action', {
        //    page: this.page,
        //    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //}]);
        this.loadAll();
    }

    search(query) {
        //if (!query) {
        //    return this.clear();
        //}
        if(query) {
            this.currentSearch = query;
        }
        this.page = 0;
        //this.currentSearch = query;
        //this.router.navigate(['/followup-outcomes', {
        //    search: this.currentSearch,
        //    page: this.page,
        //    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //}]);
        this.loadAll();
    }

    onConsultantSelected(option: IOption) {
        this.query.consultants = [];
        this.query.consultants.push(option.value);
        this.search(null);
    }

    onLocationSelected(option: IOption) {
        this.query.locations = [];
        this.query.locations.push(option.value);
        this.search(null);
    }

    onProcedureSelected(option: IOption) {
        this.query.procedures = [];
        this.query.procedures.push(option.value);
        this.search(null);
    }

    onStatusSelected(option: IOption) {
        this.query.statuses = [];
        this.query.statuses.push(option.value);
        this.search(null);
    }

    onPhaseSelected(option: IOption) {
        this.query.phases = [];
        this.query.phases.push(option.value);
        this.search(null);
    }

    onAgeChange(event: any) {
        this.search(null);
    }

    clearFilters() {
        this.query = new Query();
        this.selectedProcedure = null;
        this.selectedConsultant = null;
        this.selectedLocation = null;
        this.selectedStatus = null;
        this.selectedPhase = null;
        this.ageRange = [10, 100];
        this.search(null);
    }

    clearConsultants() {
        this.query.consultants = [];
        this.selectedConsultant = null;
        this.search(null);
    }

    clearLocations() {
        this.query.locations = [];
        this.selectedLocation = null;
        this.search(null);
    }

    clearProcedures() {
        this.query.procedures = [];
        this.selectedProcedure = null;
        this.search(null);
    }

    clearStatus() {
        this.query.statuses = [];
        this.selectedStatus = null;
        this.search(null);
    }

    clearPhases() {
        this.query.phases = [];
        this.selectedPhase = null;
        this.search(null);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: FollowupAction) {
        return item.id;
    }

    registerChangeInFollowupActions() {
        this.eventSubscriber = this.eventManager.subscribe('followupActionListModification', (response) => this.loadAll());
    }

    registerChangeInBookings() {
        this.eventSubscriber = this.eventManager.subscribe('procedureBookingListModification', (response) => {
            setTimeout(() => { this.loadAll();}, 3000);
        });
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        console.log("data  = " , data );
        if(data.results !== undefined) {
            this.links = this.parseLinks.parse(headers.get('link'));
            this.totalItems = headers.get('X-Total-Count');
            this.queryCount = this.totalItems;
            // this.page = pagingParams.page;
            this.followupActions = data.results;

            // process categories
            let v = [];
            _.mapObject(data.categories, function (value, key) {
                let category = new Category();
                category.key = key;
                category.items = value;
                v.push(category);
            });
            this.categories = v;
            console.log("this.categories  = " , this.categories );
        }
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
