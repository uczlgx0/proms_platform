package com.noesisinformatica.northumbriaproms;

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

import com.noesisinformatica.northumbriaproms.config.ApplicationProperties;
import com.noesisinformatica.northumbriaproms.config.Constants;
import com.noesisinformatica.northumbriaproms.config.DefaultProfileUtil;
import com.noesisinformatica.northumbriaproms.domain.*;
import com.noesisinformatica.northumbriaproms.domain.enumeration.GenderType;
import com.noesisinformatica.northumbriaproms.domain.enumeration.TimeUnit;
import com.noesisinformatica.northumbriaproms.repository.*;
import com.noesisinformatica.northumbriaproms.repository.search.UserSearchRepository;
import com.noesisinformatica.northumbriaproms.security.AuthoritiesConstants;
import com.noesisinformatica.northumbriaproms.service.*;
import com.noesisinformatica.northumbriaproms.service.util.RandomUtil;
import com.opencsv.CSVReader;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ComponentScan
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class NorthumbriapromsApp {

    private static final Logger log = LoggerFactory.getLogger(NorthumbriapromsApp.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Environment env;

    public NorthumbriapromsApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes northumbriaproms.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="http://www.jhipster.tech/profiles/">http://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(NorthumbriapromsApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        ConfigurableApplicationContext ctx = app.run(args);
        Environment env = ctx.getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}\n\t" +
                "External: \t{}://{}:{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            env.getProperty("server.port"),
            protocol,
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getActiveProfiles());

        if (Arrays.asList(env.getActiveProfiles()).contains("dev") || Arrays.asList(env.getActiveProfiles()).contains("prod")) {

            // add data if none exists
            verifyAndImportProcedures(ctx);
            verifyAndImportQuestionnaires(ctx);
            verifyAndImportProcedureLinks(ctx);
            verifyAndImportHealthcareProviders(ctx);
            verifyAndImportPatients(ctx);
            verifyAndImportConsultants(ctx);
            verifyAndImportTimepoints(ctx);
            verifyAndImportFollowupTimepoints(ctx);

            // clear security context
            log.info("Logging out admin user after importing entities");
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * Utility bootstrap method that imports procedures if none is found.
     * @param ctx the application context
     */
    private static void verifyAndImportProcedures(ConfigurableApplicationContext ctx) {

        ProcedureService procedureService = ctx.getBean(ProcedureService.class);
        long count = ctx.getBean(ProcedureRepository.class).count();
        log.info("No of existing procedures {}", count);

        if (count == 0) {

            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/procedures.csv")){
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int counter = 0;
                String line = bufReader.readLine();
                while(line != null){
                    if(counter > 0){
                        Procedure procedure = new Procedure();
                        String[] parts = line.split(",");
                        // create procedure form parts
                        procedure.setLocalCode(Integer.valueOf(parts[0]));
                        procedure.setName(parts[1]);
                        // save procedure
                        procedureService.save(procedure);
                    }
                    counter++;
                    line = bufReader.readLine();
                }
            } catch (IOException e){
                log.error("Unable to read procedures.csv from class path. Nested exception is : ", e);
            }
        }
    }

    /**
     * Utility bootstrap method that imports questionnaires if none found.
     * @param ctx the application context
     */
    private static void verifyAndImportQuestionnaires(ConfigurableApplicationContext ctx) {

        QuestionnaireService questionnaireService = ctx.getBean(QuestionnaireService.class);
        long count = ctx.getBean(QuestionnaireRepository.class).count();
        log.info("No of existing questionnaires {} " , count);

        if (count == 0) {

            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/questionnaires.csv")){
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int counter = 0;
                String line = bufReader.readLine();
                while(line != null){
                    if(counter > 0){
                        Questionnaire questionnaire = new Questionnaire();
                        String[] parts = line.split(",");
                        // create questionnaire form parts
                        questionnaire.setName(parts[0]);
                        // save questionnaire
                        questionnaireService.save(questionnaire);
                    }
                    counter++;
                    line = bufReader.readLine();
                }
            } catch (IOException e){
                log.error("Unable to read questionnaires.csv from class path. Nested exception is : ", e);
            }
        }
    }


    /**
     * Utility bootstrap method that imports procedure links if none is found.
     * @param ctx the application context
     */
    private static void verifyAndImportProcedureLinks(ConfigurableApplicationContext ctx) {

        ProcedurelinkService procedureLinkService = ctx.getBean(ProcedurelinkService.class);
        ProcedureRepository procedureRepository = ctx.getBean(ProcedureRepository.class);
        QuestionnaireRepository questionnaireRepository = ctx.getBean(QuestionnaireRepository.class);
        long count = ctx.getBean(ProcedurelinkRepository.class).count();
        log.info("No of existing procedure links {}", count);
        Map<String, Questionnaire> questionnaireMap = new HashMap<>();

        if (count == 0) {

            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/procedure_links.csv")){
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int counter = 0;
                String line = bufReader.readLine();
                List<String> questionnaireNames = new ArrayList<>();
                while(line != null){
                    // use first line to get names of all questionnaires
                    if(counter == 0) {
                        String[] parts = line.split(",");
                        // use questionnaire name to find match in repo
                        for(int i=0; i < parts.length; i++) {
                            // first add part to list for look up later on
                            questionnaireNames.add(parts[i]);
                            Optional<Questionnaire> questionnaire = questionnaireRepository.findOneByName(parts[i]);
                            if(questionnaire.isPresent()) {
                                questionnaireMap.put(parts[i], questionnaire.get());
                            }
                        }
                        log.info("questionnaireMap = {}", questionnaireMap);
                    } else {
                        String[] parts = line.split(",");
                        // create procedure link form parts
                        Optional<Procedure> procedure =  procedureRepository.findOneByLocalCode(Integer.valueOf(parts[0]));
                        if(procedure.isPresent()) {
                            // process rest of parts and if they contain a 'x' then create procedure link
                            for(int i=1; i < parts.length; i++) {
                                String entry = parts[i];
                                if("X".equalsIgnoreCase(entry)) {
                                    // use index to get corresponding questionnaire name in list
                                    String questionnaireName = questionnaireNames.get(i);
                                    log.info("questionnaireName = {}", questionnaireName);
                                    log.info("questionnaireMap.get(questionnaireName) = {}", questionnaireMap.get(questionnaireName));
                                    if (questionnaireMap.containsKey(questionnaireName)) {
                                        Procedurelink procedurelink = new Procedurelink();
                                        procedurelink.procedure(procedure.get()).questionnaire(questionnaireMap.get(questionnaireName));
                                        // save procedure link
                                        procedureLinkService.save(procedurelink);
                                    }
                                }
                            }
                        }
                    }
                    counter++;
                    line = bufReader.readLine();
                }
            } catch (IOException e){
                log.error("Unable to read procedure_links.csv from class path. Nested exception is : ", e);
            }
        }
    }

    /**
     * Utility bootstrap method that imports healthcare providers if none found.
     * @param ctx the application context
     */
    private static void verifyAndImportHealthcareProviders(ConfigurableApplicationContext ctx) {

        HealthcareProviderService healthcareProviderService = ctx.getBean(HealthcareProviderService.class);
        long count = ctx.getBean(HealthcareProviderRepository.class).count();
        log.info("No of existing healthcare providers {} " , count);

        if (count == 0) {

            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/healthcare_providers.csv")){
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int counter = 0;
                String line = bufReader.readLine();
                while(line != null){
                    if(counter > 0){
                        HealthcareProvider healthcareProvider = new HealthcareProvider();
                        String[] parts = line.split(",");
                        // create healthcareProvider form parts
                        healthcareProvider.setName(parts[0]);
                        // save healthcareProvider
                        healthcareProviderService.save(healthcareProvider);
                    }
                    counter++;
                    line = bufReader.readLine();
                }
            } catch (IOException e){
                log.error("Unable to read healthcare_providers.csv from class path. Nested exception is : ", e);
            }
        }
    }

    /**
     * Utility bootstrap method that imports followup time points if none found.
     * @param ctx the application context
     */
    private static void verifyAndImportFollowupTimepoints(ConfigurableApplicationContext ctx) {

        ProcedureRepository procedureRepository = ctx.getBean(ProcedureRepository.class);
        TimepointRepository timepointRepository = ctx.getBean(TimepointRepository.class);
        ProcedureTimepointService procedureTimepointService = ctx.getBean(ProcedureTimepointService.class);
        long count = ctx.getBean(ProcedureTimepointRepository.class).count();
        log.info("No of existing procedure time points : {} " , count);

        if (count == 0) {

            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/followup_timepoints.csv")){
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                // get and store time points in a list for processing later
                List<Timepoint> timepoints = new ArrayList<>();
                String[] names = {"PRE-OP", "6th Month", "1st Year", "Annual"};  // note names must exactly match time point names
                Arrays.stream(names).forEach(name -> {
                    Optional<Timepoint> timepoint = timepointRepository.findOneByName(name);
                    if(timepoint.isPresent()) {
                        timepoints.add(timepoint.get());
                    }
                });

                int counter = 0;
                String line = bufReader.readLine();
                while(line != null){
                    if(counter > 0){
                        String[] parts = line.split(",");
                        // create procedure time points form parts
                        Optional<Procedure> procedure =  procedureRepository.findOneByLocalCode(Integer.valueOf(parts[0]));
                        if(procedure.isPresent()) {
                            // process rest of parts and if they contain a 'x' then create procedure time point
                            for(int i=1; i < parts.length; i++) {
                                String entry = parts[i];
                                if("X".equalsIgnoreCase(entry)) {
                                    /*
                                     use 'i' to figure out what timepoint it matches - columns are listed as '6M', '1Y'
                                      and 'ANNUAL'. We ignore Annual for now and process 6M as '6th Moth' and 1Y as
                                      1st Year
                                     */
                                    ProcedureTimepoint procedureTimepoint = new ProcedureTimepoint();
                                    procedureTimepoint.setProcedure(procedure.get());
                                    Timepoint timepoint = timepoints.get(i - 1);
                                    // save entity
                                    if(timepoint != null) {
                                        procedureTimepoint.setTimepoint(timepoint);
                                        procedureTimepointService.save(procedureTimepoint);
                                    }
                                }
                            }
                        }
                    }
                    counter++;
                    line = bufReader.readLine();
                }
            } catch (IOException e){
                log.error("Unable to read followup_timepoints.csv from class path. Nested exception is : ", e);
            }
        }
    }

    /**
     * Utility bootstrap method that imports time points if none found.
     * @param ctx the application context
     */
    private static void verifyAndImportTimepoints(ConfigurableApplicationContext ctx) {

        TimepointService timepointService = ctx.getBean(TimepointService.class);
        long count = ctx.getBean(TimepointRepository.class).count();
        log.info("No of existing time points : {} " , count);

        if (count == 0) {

            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/timepoints.csv")){
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int counter = 0;
                String line = bufReader.readLine();
                while(line != null){
                    if(counter > 0){
                        Timepoint timepoint = new Timepoint();
                        String[] parts = line.split(",");
                        // create time point form parts
                        timepoint.setValue(Integer.valueOf(parts[0].trim()));
                        timepoint.setUnit(TimeUnit.valueOf(parts[1].trim()));
                        timepoint.setName(parts[2].trim());
                        // save time point
                        timepointService.save(timepoint);
                    }
                    counter++;
                    line = bufReader.readLine();
                }
            } catch (IOException e){
                log.error("Unable to read timepoints.csv from class path. Nested exception is : ", e);
            }
        }
    }

    /**
     * Utility bootstrap method that imports patients if none found.
     * @param ctx the application context
     */
    private static void verifyAndImportPatients(ConfigurableApplicationContext ctx) {

        PatientService patientService = ctx.getBean(PatientService.class);
        long count = ctx.getBean(PatientRepository.class).count();
        log.info("No of existing patients {} " , count);

        if (count == 0) {

            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/patients.csv")){
                CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                reader.forEach(parts -> {
                    log.debug("parts = {}", parts);
                    if(! parts[0].startsWith("#")){
                        Patient patient = new Patient();
                        // create patient form parts
                        patient.setGivenName(parts[2]);
                        patient.setFamilyName(parts[3]);

                        Address address = new Address(parts[4]);
                        address.addLine(parts[5]);
                        address.setCity(parts[6]);
                        patient.addAddress(address);

                        address.setPostalCode(parts[7]);
                        LocalDate date = LocalDate.parse(parts[9], formatter);
                        patient.setBirthDate(date);
                        patient.setGender(GenderType.valueOf(parts[10]));
                        patient.setNhsNumber(Long.valueOf(parts[11]));
                        // save questionnaire
                        patientService.save(patient);
                    }
                });
            } catch (IOException e){
                log.error("Unable to read questionnaires.csv from class path. Nested exception is : ", e);
            }
        }
    }

    /**
     * Utility bootstrap method that imports consultants if none found.
     * @param ctx the application context
     */
    private static void verifyAndImportConsultants(ConfigurableApplicationContext ctx) {

        UserRepository userRepository = ctx.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);
        UserSearchRepository userSearchRepository = ctx.getBean(UserSearchRepository.class);
        long count = userRepository.findAllByAuthoritiesName(AuthoritiesConstants.CONSULTANT, null).getTotalElements();
        log.info("No of existing consultants {} ", count);

        if (count == 0) {

            AuthorityRepository authorityRepository = ctx.getBean(AuthorityRepository.class);
            // find consultant role
            Authority consultantRole = authorityRepository.findOne(AuthoritiesConstants.CONSULTANT);
            try (InputStream inputStream = NorthumbriapromsApp.class.getClassLoader().getResourceAsStream("config/consultants.csv")){
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int counter = 0;
                String line = bufReader.readLine();
                while(line != null){
                    if(counter > 0){
                        String[] parts = line.split(",");
                        // create consultant from part
                        User user = new User();
                        user.setTitle(parts[0].trim());
                        user.setFirstName(parts[1].trim());
                        user.setLastName(parts[2].trim());
                        user.setLogin(parts[3].trim());
                        user.setEmail(parts[4].trim() + "@promsapp.com");
                        user.setPassword(parts[4].trim());
                        user.addAuthority(consultantRole);
                        if (user.getLangKey() == null) {
                            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
                        } else {
                            user.setLangKey(user.getLangKey());
                        }

                        // set random password if no password set
                        if (user.getPassword() == null) {
                            user.setPassword(RandomUtil.generatePassword());
                        }

                        String encryptedPassword = passwordEncoder.encode(user.getPassword());
                        user.setPassword(encryptedPassword);
                        user.setResetKey(RandomUtil.generateResetKey());
                        user.setResetDate(Instant.now());
                        user.setActivated(true);
                        log.debug("Saving user = {}", user);
                        userRepository.save(user);
                        userSearchRepository.save(user);
                    }
                    counter++;
                    line = bufReader.readLine();
                }
            } catch (IOException e){
                log.error("Unable to read consultants.csv from class path. Nested exception is : ", e);
            }
        }
    }

}
