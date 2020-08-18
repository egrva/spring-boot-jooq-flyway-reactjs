package ru.aegorova.friendslist;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:application.properties")

public class FriendsSeleniumTest {

    @Autowired
    private MockMvc mockMvc;

    private static final int TEST_SERVER_PORT = 28080;
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendsSeleniumTest.class);


    // declare postgresql test container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:10.13-alpine") {{
        withDatabaseName("postgres");
        withUsername("aegorova");
        withPassword("aegorova");
        waitingFor(waitStrategy);
    }};


    // override before and after test methods
    @ClassRule
    public static ExternalResource resource = new ExternalResource() {
        @Override
        public void before() throws InterruptedException {
            System.out.println("Before test...");
            Testcontainers.exposeHostPorts(TEST_SERVER_PORT);
            postgreSQLContainer.start();
            Thread.sleep(10000);
            // init mocks
            MockitoAnnotations.initMocks(this);
        }

        @Override
        public void after() {
            System.out.println("After test...");
        }
    };

    // change default data source configuration
    @TestConfiguration
    static class TestConfig {
        @Bean("dataSource")
        DataSource dataSource() {
            return DataSourceBuilder.create()
                    .driverClassName(postgreSQLContainer.getDriverClassName())
                    .url(postgreSQLContainer.getJdbcUrl())
                    .username(postgreSQLContainer.getUsername())
                    .password(postgreSQLContainer.getPassword())
                    .build();
        }
    }

    // create google chrome driver container
    @Rule
    public BrowserWebDriverContainer container = new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions());

    @Test
    // test for input form
    public void seleniumTest() {
        final RemoteWebDriver driver = container.getWebDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        final String name = "test";
        driver.get("http://host.testcontainers.internal:" + TEST_SERVER_PORT + "/");

        // find list on the page
        final List<WebElement> friendList = parseFriendsList(driver);
        // count list's items
        final int friendListSizeBeforeUpdate = friendList.size();

        // find form
        final WebElement form = driver.findElement(By.id("name"));
        // write testing name into form
        form.sendKeys(name);

        // find button
        final WebElement button = driver.findElement(By.id("button"));
        // click on the button
        button.click();
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // reload our list
        friendList.clear();
        friendList.addAll(parseFriendsList(driver));
        // count new list's size
        final int friendListSizeAfterUpdate = friendList.size();
        // compare list's size before and after update. One friend was added, so updated list has to be one more friend
        assert (friendListSizeAfterUpdate == friendListSizeBeforeUpdate + 1);
        // compare last name in list on the page with our testing name
        assert (friendList.get(friendListSizeAfterUpdate - 1).getText().equals(name));
        driver.quit();
        LOGGER.info("Selenium test ends");
    }

    // get list from page with css selectors
    private List<WebElement> parseFriendsList(WebDriver driver) {
        return driver.findElements(By.cssSelector("ul li"));
    }

    // simple test to check connection
    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
        LOGGER.info("Greeting test ends");

    }

    // test creating request with rare name to check the response
    @Test
    @Transactional
    public void postAndGetFriend() throws Exception {
        final String nameInner = UUID.randomUUID().toString();
        mockMvc.perform(post("/api/friends")
                .param("name", nameInner))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nameInner));

        mockMvc.perform(get("/api/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem(nameInner)));
        LOGGER.info("MockMvc test ends");
    }
}