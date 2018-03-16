/*-
 * #%L
 * Proms Platform
 * %%
 * Copyright (C) 2017 - 2018 Termlex
 * %%
 * This software is Copyright and Intellectual Property of Termlex Inc Limited.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation as version 3 of the
 * License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 * #L%
 */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import { JhiParseLinks } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { PaginationConfig } from '../../../../../../main/webapp/app/blocks/config/uib-pagination.config'
import { AuditsComponent } from '../../../../../../main/webapp/app/admin/audits/audits.component';
import { AuditsService } from '../../../../../../main/webapp/app/admin/audits/audits.service';
import { ITEMS_PER_PAGE } from '../../../../../../main/webapp/app/shared';

function getDate(isToday= true) {
    let date: Date = new Date();
    if (isToday) {
        // Today + 1 day - needed if the current day must be included
        date.setDate(date.getDate() + 1);
    } else {
      // get last month
      if (date.getMonth() === 0) {
        date = new Date(date.getFullYear() - 1, 11, date.getDate());
      } else {
        date = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
      }
    }
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
}

describe('Component Tests', () => {

    describe('AuditsComponent', () => {

        let comp: AuditsComponent;
        let fixture: ComponentFixture<AuditsComponent>;
        let service: AuditsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [AuditsComponent],
                providers: [
                    AuditsService,
                    NgbPaginationConfig,
                    JhiParseLinks,
                    PaginationConfig,
                    DatePipe
                ]
            }).overrideTemplate(AuditsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuditsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuditsService);
        });

        describe('today function ', () => {
            it('should set toDate to current date', () => {
               comp.today();
               expect(comp.toDate).toBe(getDate());
            });
        });

        describe('previousMonth function ', () => {
            it('should set fromDate to current date', () => {
               comp.previousMonth();
               expect(comp.fromDate).toBe(getDate(false));
            });
        });

        describe('By default, on init', () => {
            it('should set all default values correctly', () => {
               fixture.detectChanges();
               expect(comp.toDate).toBe(getDate());
               expect(comp.fromDate).toBe(getDate(false));
               expect(comp.itemsPerPage).toBe(ITEMS_PER_PAGE);
               expect(comp.page).toBe(1);
               expect(comp.reverse).toBeFalsy();
               expect(comp.orderProp).toBe('timestamp');
            });
        });
    });
});
