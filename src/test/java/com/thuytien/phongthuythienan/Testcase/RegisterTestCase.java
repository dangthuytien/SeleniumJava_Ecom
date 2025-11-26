package com.thuytien.phongthuythienan.Testcase;

import com.thuytien.common.BaseTest;
import com.thuytien.drivers.DriverManager;
import com.thuytien.helpers.ExcelHelpers;
import com.thuytien.keywords.WebUI;
import com.thuytien.phongthuythienan.Pages.BasePage;
import com.thuytien.phongthuythienan.Pages.RegisterPage;
import org.testng.annotations.Test;

public class RegisterTestCase extends BaseTest {
    BasePage basePage = new BasePage();
    RegisterPage page = basePage.getRegisterPage();
    ExcelHelpers excel = new ExcelHelpers();



    @Test(priority = 1)
    public void TC01_registerSuccessWithValidData() {
        excel.setExcelFile("./src/test/resources/testdata/registerdata30_6.xlsx", "Sheet1");
        WebUI.openURL("https://phongthuythienan.monamedia.net/register/");
        page.enterName(excel.getCellData("name", 6));
        page.enterPhone(excel.getCellData("phone", 6));
        page.enterEmail(excel.getCellData("email", 6));
        page.enterPassword(excel.getCellData("password", 6));
        page.enterConfirmPassword(excel.getCellData("confirmPassword", 6));
        page.clickRegisterButton();
        WebUI.sleep(20);
        page.verifyRegisterSuccess(excel.getCellData("email", 6));
    }

    @Test(priority = 2)
    public void TC02_registerWithEmptyFields() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/register/");
        page.clickRegisterButton();
        WebUI.sleep(2);
        page.verifyRegisterFailWithNullAllFields();
    }

    @Test(priority = 3)
    public void TC03_testRegisterWithPasswordMismatch() {
        excel.setExcelFile("./src/test/resources/testdata/registerdata30_6.xlsx", "Sheet1");
        WebUI.openURL("https://phongthuythienan.monamedia.net/register/");
        page.enterName(excel.getCellData("name", 3));
        page.enterPhone(excel.getCellData("phone", 3));
        page.enterEmail(excel.getCellData("email", 3));
        page.enterPassword(excel.getCellData("password", 3));
        page.enterConfirmPassword(excel.getCellData("confirmPassword", 3));
        page.clickRegisterButton();
        WebUI.sleep(2);

        page.verifyConfirmPasswordNotMatched();
    }

    @Test(priority = 4)
    public void TC04_testRegisterWithShortPassword() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/register/");
        page.enterName(excel.getCellData("name", 4));
        page.enterPhone(excel.getCellData("phone", 4));
        page.enterEmail(excel.getCellData("email", 4));
        page.enterPassword(excel.getCellData("password", 4));
        page.enterConfirmPassword(excel.getCellData("confirmPassword", 4));
        page.clickRegisterButton();
        WebUI.sleep(2);

        page.verifyRegisterFailWithShortPassword();
    }

    @Test(priority = 5)
    public void TC05_testRegisterWithDuplicateEmail() {

        // Lần 1: đăng ký thành công
        WebUI.openURL("https://phongthuythienan.monamedia.net/register/");
        excel.setExcelFile("./src/test/resources/testdata/registerdata30_6.xlsx", "Sheet1");
        RegisterPage page = basePage.getRegisterPage();
        page.enterName(excel.getCellData("name", 7));
        page.enterPhone(excel.getCellData("phone", 7));
        page.enterEmail(excel.getCellData("email", 7));
        page.enterPassword(excel.getCellData("password", 7));
        page.enterConfirmPassword(excel.getCellData("confirmPassword", 7));
        page.clickRegisterButton();
        WebUI.sleep(20);

        page.verifyRegisterSuccess(excel.getCellData("email", 7));
        // Quay lại trang đăng ký
        WebUI.openURL("https://phongthuythienan.monamedia.net/register/");

        // Lần 2: dùng lại email
        page.enterName(excel.getCellData("name", 7));
        page.enterPhone(excel.getCellData("phone", 7));
        page.enterEmail(excel.getCellData("email", 7));
        page.enterPassword(excel.getCellData("password", 7));
        page.enterConfirmPassword(excel.getCellData("confirmPassword", 7));
        page.clickRegisterButton();
        WebUI.sleep(5);
        page.verifyRegisterFailWithDuplicateEmail();
    }

    @Test(priority = 6)
    public void TC06_testClickLoginLinkRedirectsToLoginPage() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/register/");
        page.clickToLoginLink();
        WebUI.sleep(2);
        page.verifyNavigateToLoginPage();
    }


}
