package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.KeyboardModifier;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelExecutionTest {

    private Playwright playwright;
    private Browser browser;
    BrowserContext context;
    Page page;


    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }
    @AfterEach
    void teardown() {
        browser.close();
        playwright.close();
    }

    @Test
    void testGoogleTitle() {
        // контекст создается и закрывается в каждом тесте. создание Playwright и браузера и их закрытие теперь
        // перед каждым тестом BeforeEach, а не 1 браузер на все тесты BeforeAll
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://www.google.com/");
        assertTrue(page.title().contains("Google"));
        context.close();
    }

    @Test
    void testPlaywrightDocs() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://www.playwright.dev/java");
        assertTrue(page.title().contains("Playwright"));
        context.close();
    }

    @Test
    void testBanki24() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://banki24.by/");
        assertTrue(page.title().contains("banki24.by"));
        context.close();
    }

}
