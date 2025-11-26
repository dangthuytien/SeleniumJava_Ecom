package com.thuytien.phongthuythienan.Pages;

import com.thuytien.drivers.DriverManager;
import com.thuytien.keywords.WebUI;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class LoginPage {

    BasePage basePage = new BasePage();

    private By emailInput = By.xpath("(//label[contains(text(),'Email')]/ancestor::div[@class='relative w-full '])/descendant::input");
    private By passwordInput = By.xpath("(//label[contains(text(),'Mật khẩu')]/ancestor::div[@class='relative w-full !m-0'])/descendant::input");
    private By loginCheckbox = By.xpath("//input[@name='remember']");
    private By forgotPasswordLinkText = By.xpath("//a[contains(text(),'Quên mật khẩu?')]");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By registerLinkText = By.xpath("//a[contains(text(),'Đăng ký ngay')]");
    public By eyeIcon = By.xpath("//button[@type='button']//*[name()='svg']");

    //======Error Message=========

    private By emailErrorMessage = By.xpath("(//label[contains(text(),'Email')]/ancestor::div[@class='relative w-full '])/descendant::p");
    private By passwordErrorMessage = By.xpath("(//label[contains(text(),'Mật khẩu')]/ancestor::div[@class='relative w-full !m-0'])/descendant::p");
    private By invalidAccountMessage = By.xpath("(//label[contains(text(),'Mật khẩu')]/ancestor::div[@class='relative w-full !m-0'])/following-sibling::div");

    //========Action method==========

    public void enterEmail(String email) {
        WebUI.setText(emailInput, email);
    }

    public void enterPassword(String password) {
        WebUI.setText(passwordInput, password);
    }

    public void clickLoginButton() {
        WebUI.clickElement(loginButton);
    }

    public void clickRegisterLink() {
        WebUI.clickElement(registerLinkText);
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    // ========== Verifications ========== //
    public void verifyLoginSuccess(String expectedEmail) {
        SoftAssert softAssert = new SoftAssert();

        // 1. Kiểm tra URL chuyển về trang chủ
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        softAssert.assertEquals(currentUrl, "https://phongthuythienan.monamedia.net/",
                "❌ Không chuyển về trang chủ sau khi đăng nhập");

        // 2. Hover avatar và kiểm tra email
        WebUI.waitForElementVisible(basePage.avatarIcon);
        WebUI.hoverElement(basePage.avatarIcon);
        WebUI.waitForElementVisible(basePage.emailAccountHeaderDropdown);
        String actualEmail = WebUI.getElementText(basePage.emailAccountHeaderDropdown).trim();
        softAssert.assertEquals(actualEmail, expectedEmail.trim(),
                "❌ Email hiển thị sau khi đăng nhập không đúng");

        softAssert.assertAll();
    }
    public void verifyLoginFailWithEmptyFields() {
        Assert.assertTrue(DriverManager.getDriver().findElement(emailErrorMessage).isDisplayed(), "Không hiển thị thông báo lỗi trống email");
        Assert.assertTrue(DriverManager.getDriver().findElement(passwordErrorMessage).isDisplayed(), "Không hiển thị thông báo lỗi trống password");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(WebUI.getElementText(emailErrorMessage), "Email là bắt buộc", "Thông báo lỗi trường email không trùng khớp");
        softAssert.assertEquals(WebUI.getElementText(passwordErrorMessage), "Mật khẩu là bắt buộc", "Thông báo lỗi trường Mật khẩu không trùng khớp");
        softAssert.assertAll();
    }

    public void verifyLoginFailWithEmptyPassword() {
        Assert.assertTrue(DriverManager.getDriver().findElement(passwordErrorMessage).isDisplayed(), "Không hiển thị thông báo lỗi trống password");
        Assert.assertEquals(WebUI.getElementText(passwordErrorMessage), "Mật khẩu là bắt buộc", "Thông báo lỗi trường Mật khẩu không trùng khớp");
    }

    public void verifyLoginFailWithEmptyEmail() {
        Assert.assertTrue(DriverManager.getDriver().findElement(emailErrorMessage).isDisplayed(), "Không hiển thị thông báo lỗi trống email");
        Assert.assertEquals(WebUI.getElementText(emailErrorMessage), "Email là bắt buộc", "Thông báo lỗi trường email không trùng khớp");
    }

    public void verifyLoginFailWithWrongAccount() {
        Assert.assertTrue(DriverManager.getDriver().findElement(invalidAccountMessage).isDisplayed(), "Không hiển thị thông báo lỗi tài khoản không hợp lệ");
        Assert.assertEquals(WebUI.getElementText(invalidAccountMessage),"Tài khoản không hợp lệ", "Thông báo lỗi tài khoản không hợp lệ không khớp");

    }

    public void verifyLoginFailWithInvalidEmailFormat() {
        Assert.assertTrue(DriverManager.getDriver().findElement(emailErrorMessage).isDisplayed(), "Không hiển thị thông báo lỗi email không đúng định dạng");
        Assert.assertEquals(WebUI.getElementText(emailErrorMessage),"Email không hợp lệ", "Thông báo lỗi email không hợp lệ không khớp");

    }

    public void verifyNavigateToRegisterPage() {
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("register"), "Không điều hướng sang trang đăng ký");
    }


    public void verifyPasswordIsVisible() {
        String type = WebUI.getWebElement(passwordInput).getAttribute("type");
        Assert.assertEquals(type, "text", "❌ Mật khẩu không hiển thị khi click icon!");
    }

    public void verifyPasswordIsHidden() {
        String type = WebUI.getWebElement(passwordInput).getAttribute("type");
        Assert.assertEquals(type, "password", "❌ Mật khẩu không ẩn sau khi click lại icon!");
    }




}
