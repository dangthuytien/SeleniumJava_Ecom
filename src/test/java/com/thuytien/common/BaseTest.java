package com.thuytien.common;

import com.thuytien.Listener.TestListener;
import com.thuytien.drivers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)

public class BaseTest {

    @BeforeMethod
    public void createDriver() {
        WebDriver driver = new ChromeDriver();
        DriverManager.setDriver(driver);
        driver.manage().window().maximize();
    }

    @AfterMethod()
    public void closeDriver() {
        //WebUI.stopSoftAssertAll();
        DriverManager.quit();
    }
}
