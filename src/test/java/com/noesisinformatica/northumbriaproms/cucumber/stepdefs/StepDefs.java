package com.noesisinformatica.northumbriaproms.cucumber.stepdefs;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = NorthumbriapromsApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
