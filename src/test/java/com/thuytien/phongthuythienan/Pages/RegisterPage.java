package com.thuytien.phongthuythienan.Pages;

import com.thuytien.drivers.DriverManager;
import com.thuytien.keywords.WebUI;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class RegisterPage {

    BasePage basePage = new BasePage();

    //==========Locator===========/

    private By nameInput = By.xpath("(//label[contains(text(),'Họ tên')]/ancestor::div[@class='relative w-full '])/descendant::input");
    private By phoneInput = By.xpath("(//label[contains(text(),'Số điện thoại')]/ancestor::div[@class='relative w-full '])/descendant::input");
    private By emailInput = By.xpath("(//label[contains(text(),'Email')]/ancestor::div[@class='relative w-full '])/descendant::input");
    private By passwordInput = By.xpath("(//label[contains(text(),'Mật khẩu')]/ancestor::div[@class='relative w-full !m-0'])/descendant::input");
    private By confirmPasswordInput = By.xpath("(//label[contains(text(),'Nhập lại mật khẩu')]/ancestor::div[@class='relative w-full !m-0'])/descendant::input");
    private By registerButton = By.xpath("//button[@type='submit']");
    private By loginLink = By.xpath("//a[contains(text(),'Đăng nhập')]");

    //============Error message===========/
    private By nameValidationMessage = By.xpath("//p[contains(text(),'Tên là bắt buộc')]");
    private By phoneValidationMessage = By.xpath("//p[contains(text(),'Số điện thoại là bắt buộc')]");
    private By emailValidationMessage = By.xpath("//p[contains(text(),'Email là bắt buộc')]");
    private By passwordValidationMessage = By.xpath("//p[contains(text(),'Mật khẩu là bắt buộc')]");
    private By passwordShortValidationMessage = By.xpath("//p[contains(text(),'Mật khẩu phải có ít nhất 8 ký tự')]");
    private By confirmPasswordValidationMessage = By.xpath("//p[contains(text(),'Vui lòng xác nhận mật khẩu')]");
    private By confirmPasswordUnmatchValidationMessage= By.xpath("//p[contains(text(),'Mật khẩu không khớp')]");
    private By sameEmailValidationMessage = By.xpath("(//label[contains(text(),'Nhập lại mật khẩu')]/ancestor::div[@class='relative w-full !m-0'])/following-sibling::div");

    // ===== Action methods =====

    public void enterName (String name) {
        WebUI.setText(nameInput, name);
    }

    public void enterPhone(String phone) {
        WebUI.setText(phoneInput, phone);
    }

    public void enterEmail (String email) {
        WebUI.setText(emailInput, email);
    }

    public void enterPassword (String password) {
        WebUI.setText(passwordInput, password);
    }
    public void enterConfirmPassword (String confirmPassword) {
        WebUI.setText(confirmPasswordInput, confirmPassword);
    }

    public void clickRegisterButton () {
        WebUI.clickElement(registerButton);
    }

    public void clickToLoginLink() {
        WebUI.clickElement(loginLink);
    }


    //===================

    //Đăng ký thất bại khi để trống tất cả các trường
    public void verifyRegisterFailWithNullAllFields() {
        SoftAssert softAssert = new SoftAssert();
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("register"), "FAIL. Không còn ở trang Register");
        Assert.assertTrue(DriverManager.getDriver().findElement(nameValidationMessage).isDisplayed(), "Tên không hiển thị thông báo trường bắt buộc");
        Assert.assertTrue(DriverManager.getDriver().findElement(phoneValidationMessage).isDisplayed(), "Số điện thoại không hiển thị thông báo trường bắt buộc");
        Assert.assertTrue(DriverManager.getDriver().findElement(emailValidationMessage).isDisplayed(), "Email không hiển thị thông báo trường bắt buộc");
        Assert.assertTrue(DriverManager.getDriver().findElement(passwordValidationMessage).isDisplayed(), "Mật khẩu không hiển thị thông báo trường bắt buộc");
        Assert.assertTrue(DriverManager.getDriver().findElement(confirmPasswordValidationMessage).isDisplayed(), "Nhập lại mật khẩu không hiển thị thông báo trường bắt buộc");

        softAssert.assertEquals(WebUI.getElementText(nameValidationMessage), "Tên là bắt buộc", "Thông báo lỗi trường Tên không trùng khớp");
        softAssert.assertEquals(WebUI.getElementText(phoneValidationMessage), "Số điện thoại là bắt buộc","Thông báo lỗi trường Số điện thoại không trùng khớp");
        softAssert.assertEquals(WebUI.getElementText(emailValidationMessage), "Email là bắt buộc", "Thông báo lỗi trường Email không trùng khớp");
        softAssert.assertEquals(WebUI.getElementText(passwordValidationMessage),"Mật khẩu là bắt buộc", "Thông báo lỗi trường Mật khẩu không trùng khớp");
        softAssert.assertEquals(WebUI.getElementText(confirmPasswordValidationMessage), "Vui lòng xác nhận mật khẩu", "Thông báo lỗi trường Nhập lại mật khẩu không trùng khớp");
        softAssert.assertAll();
    }


    // Email đã tồn tại
    public void verifyRegisterFailWithDuplicateEmail() {
        Assert.assertTrue(DriverManager.getDriver().findElement(sameEmailValidationMessage).isDisplayed(), "Không hiển thị lỗi email đã tồn tại");
        Assert.assertEquals(WebUI.getElementText(sameEmailValidationMessage), "Email đã tồn tại", "Thông báo email đã tồn tại không chính xác");
    }

    // Mật khẩu quá ngắn
    public void verifyRegisterFailWithShortPassword() {
        Assert.assertTrue(DriverManager.getDriver().findElement(passwordShortValidationMessage).isDisplayed(), "Không hiển thị lỗi mật khẩu ngắn");
        Assert.assertEquals(WebUI.getElementText(passwordShortValidationMessage), "Mật khẩu phải có ít nhất 8 ký tự", "Thông báo mật khẩu ngắn không chính xác");
    }

    // Chuyển hướng sang trang Đăng nhập
    public void verifyNavigateToLoginPage() {
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "Không chuyển hướng sang trang đăng nhập");
    }

    // Nhập lại mật khẩu không trùng khớp với mật khẩu
    public void verifyConfirmPasswordNotMatched() {
        SoftAssert softAssert = new SoftAssert();

        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("register"), "FAIL. Không còn ở trang Register");
        Assert.assertTrue(DriverManager.getDriver().findElement(confirmPasswordUnmatchValidationMessage).isDisplayed(), "Không hiển thị lỗi khi xác nhận mật khẩu không trùng");

        softAssert.assertEquals(WebUI.getElementText(confirmPasswordUnmatchValidationMessage),
                "Mật khẩu không khớp",
                "Nội dung lỗi không trùng khớp mật khẩu không đúng");

        softAssert.assertAll();
    }

    public void verifyRegisterSuccess(String expectedEmail) {

        // 1. Kiểm tra chuyển hướng sang trang chủ
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.equals("https://phongthuythienan.monamedia.net/"),
                "FAIL. Không chuyển về trang chủ sau khi đăng ký thành công");

        // 2. Hover vào avatar để hiển thị menu
        WebUI.hoverElement(basePage.avatarIcon); // Hover để hiện thông tin

        // 3. Lấy email hiển thị trong menu
        WebUI.waitForElementVisible(basePage.emailAccountHeaderDropdown); // Đợi element hiển thị

        String actualEmail = WebUI.getElementText(basePage.emailAccountHeaderDropdown);
        Assert.assertEquals(actualEmail.trim(), expectedEmail.trim(),
                "Email hiển thị không đúng với email đã đăng ký");

    }











}
