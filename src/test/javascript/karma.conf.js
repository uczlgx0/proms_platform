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
const webpackConfig = require('../../../webpack/webpack.test.js');

const WATCH = process.argv.includes('--watch');

module.exports = (config) => {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: './',

        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine', 'intl-shim'],

        // list of files / patterns to load in the browser
        files: [
            'spec/entry.ts'
        ],


        // list of files to exclude
        exclude: ['e2e/**'],

        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        preprocessors: {
            'spec/entry.ts': ['webpack', 'sourcemap']
        },

        webpack: webpackConfig(WATCH),

        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        reporters: ['dots', 'junit', 'progress', 'karma-remap-istanbul', 'notify'],

        junitReporter: {
            outputFile: '../../../../target/test-results/karma/TESTS-results.xml'
        },

        notifyReporter: {
            reportEachFailure: true, // Default: false, will notify on every failed sepc
            reportSuccess: true // Default: true, will notify when a suite was successful
        },


        remapIstanbulReporter: {
            reports: { // eslint-disable-line
                'lcovonly': 'target/test-results/coverage/report-lcov/lcov.info',
                'html': 'target/test-results/coverage',
                'text-summary': null
            }
        },

        // web server port
        port: 9876,

        // enable / disable colors in the output (reporters and logs)
        colors: true,

        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: WATCH,

        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['PhantomJS'],

        // Ensure all browsers can run tests written in .ts files
        mime: {
            'text/x-typescript': ['ts','tsx']
        },

        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: !WATCH
    });
};
