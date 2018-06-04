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
import { SpyObject } from './spyobject';
import { Principal } from '../../../../main/webapp/app/shared/auth/principal.service';
import Spy = jasmine.Spy;

export class MockPrincipal extends SpyObject {

    identitySpy: Spy;
    fakeResponse: any;

    constructor() {
        super(Principal);

        this.fakeResponse = {};
        this.identitySpy = this.spy('identity').andReturn(Promise.resolve(this.fakeResponse));
    }

    setResponse(json: any): void {
        this.fakeResponse = json;
    }
}
