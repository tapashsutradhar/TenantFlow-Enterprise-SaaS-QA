package com.tenantflow.qa;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

class WorkspaceLoginTest {
    @Test void adminCanOpenNorthwindWorkspace() {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(System.getProperty("tenantflow.web", "http://localhost:5173"));
            driver.findElement(By.cssSelector("input[type=email]")).sendKeys("admin@northwind.test");
            driver.findElement(By.cssSelector("input[type=password]")).sendKeys("Northwind-Dev-Only!");
            driver.findElement(By.cssSelector("button[type=submit]")).click();
            assertTrue(new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Northwind Labs")));
        } finally { driver.quit(); }
    }
}
