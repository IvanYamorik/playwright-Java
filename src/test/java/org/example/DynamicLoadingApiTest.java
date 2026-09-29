package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicLoadingApiTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    /**
     // Тест проверки динамического контента:
     1. Инициализация браузера с включенной трассировкой
     2. Переход на тестовую страницу
     3. Мониторинг сетевых ответов с валидацией статусов
     4. Взаимодействие с элементами интерфейса
     5. Сохранение трассировочных данных при успешном выполнении
     */

    @BeforeEach
    void createContextAndPage() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }


    @Test
    void testDynamicLoading() {
        // Настраиваем трассировку со скриншотами
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true));

        page = context.newPage();
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");

        page.onResponse(response -> {
            if (response.url().contains("/dynamic_loading")) {
                assertEquals(200, response.status(),
                        "Неверный статус ответа для URL: " + response.url());
            }
        });

        // Работа с динамическими элементами
        page.click("button");
        Locator finishText = page.locator("#finish");
        finishText.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        assertEquals("Hello World!", finishText.textContent().trim(),
                "Текст элемента не соответствует ожидаемому");

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace/trace-success.zip")));

    }

    // Освобождение ресурсов после теста

    @AfterEach
    void tearDown() {
        if (page != null) page.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
