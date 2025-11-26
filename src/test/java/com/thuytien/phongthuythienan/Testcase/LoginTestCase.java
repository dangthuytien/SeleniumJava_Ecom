package com.thuytien.phongthuythienan.Testcase;

import com.thuytien.common.BaseTest;
import com.thuytien.helpers.ExcelHelpers;
import com.thuytien.keywords.WebUI;
import com.thuytien.phongthuythienan.Pages.BasePage;
import com.thuytien.phongthuythienan.Pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginTestCase extends BaseTest {
    BasePage basePage = new BasePage();
    ExcelHelpers excel = new ExcelHelpers();
    LoginPage loginPage = basePage.getLoginPage();


    @BeforeMethod
    public void openLoginPage() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/dang-nhap/");
        excel.setExcelFile("src/test/resources/testdata/logindata30_6.xlsx", "Sheet1");
    }

    @Test(priority = 1)
    public void TC01_loginSuccessWithValidAccount() {
//        WebUI.openURL("https://phongthuythienan.monamedia.net/login/");
//        excel.setExcelFile("src/test/resources/testdata/logindata30_6.xlsx", "Sheet1");
        loginPage.login(excel.getCellData("email",1), excel.getCellData("password",1));
        WebUI.sleep(2);
        loginPage.verifyLoginSuccess(excel.getCellData("email",1));
    }

    @Test(priority = 2)
    public void TC02_loginFailWithEmptyFields() {
        loginPage.clickLoginButton();
        WebUI.sleep(2);
        loginPage.verifyLoginFailWithEmptyFields();
    }

    @Test(priority = 3)
    public void TC03_loginFailWithOnlyPassword() {
        loginPage.enterPassword(excel.getCellData("password",3));
        loginPage.clickLoginButton();
        WebUI.sleep(2);
        loginPage.verifyLoginFailWithEmptyEmail();
    }

    @Test(priority = 4)
    public void TC04_loginFailWithOnlyEmail() {
        loginPage.enterEmail(excel.getCellData("email",4));
        loginPage.clickLoginButton();
        WebUI.sleep(2);
        loginPage.verifyLoginFailWithEmptyPassword();
    }

    @Test(priority = 5)
    public void TC05_loginFailWithWrongAccount() {
        loginPage.login(excel.getCellData("email",5), excel.getCellData("password",5));
        WebUI.sleep(2);
        loginPage.verifyLoginFailWithWrongAccount();
    }

    @Test(priority = 6)
    public void TC06_loginFailWithInvalidEmailFormat() {
        loginPage.enterEmail(excel.getCellData("email",6));
        loginPage.enterPassword(excel.getCellData("password",6));
        loginPage.clickLoginButton();
        WebUI.sleep(2);
        loginPage.verifyLoginFailWithInvalidEmailFormat();
    }

    @Test(priority = 7)
    public void TC07_clickRegisterLinkRedirectsToRegisterPage() {
        loginPage.clickRegisterLink();
        WebUI.sleep(2);
        loginPage.verifyNavigateToRegisterPage();
    }

    @Test(priority = 8)
    public void TC08_toggleShowHidePassword() {


        // Ban đầu phải là ẩn (type = password)
        loginPage.verifyPasswordIsHidden();

        // Click icon để hiện mật khẩu
        WebUI.clickElement(loginPage.eyeIcon);
        WebUI.sleep(1);
        loginPage.verifyPasswordIsVisible();

        // Click lại để ẩn mật khẩu
        WebUI.clickElement(loginPage.eyeIcon);
        WebUI.sleep(1);
        loginPage.verifyPasswordIsHidden();
    }



}
