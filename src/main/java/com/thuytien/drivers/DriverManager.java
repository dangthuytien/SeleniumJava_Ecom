package com.thuytien.drivers;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
        super(); // Constructor private để ngăn khởi tạo object
    }


    // Lấy WebDriver hiện tại
    public static WebDriver getDriver() {
        return driver.get();
    }

    //Gán WebDriver mới vào ThreadLocal
    public static void setDriver(WebDriver driver) {
        DriverManager.driver.set(driver);
    }

    //Đóng trình duyệt nếu driver tồn tại
    public static void quit() {
        if (DriverManager.getDriver() != null){
            DriverManager.getDriver().quit();
            DriverManager.driver.remove();
        }
    }
}

