package com.thuytien.phongthuythienan.Pages;

import com.thuytien.keywords.WebUI;
import com.thuytien.utils.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BasePage {


    public RegisterPage registerPage;

    public RegisterPage getRegisterPage() {
        if (registerPage == null) {
            registerPage = new RegisterPage();
        }
        return registerPage;
    }

    public LoginPage loginPage;

    public LoginPage getLoginPage(){
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    public SearchPage searchPage;
    public SearchPage getSearchPage(){
        if (searchPage == null) {
            searchPage = new SearchPage();
        }
        return searchPage;
    }

    public CategoryPage categoryPage;
    public CategoryPage getCategoryPage(){
        if (categoryPage == null) {
            categoryPage = new CategoryPage();
        }
        return categoryPage;
    }

    public WishlistPage wishlistPage;
    public WishlistPage getWishlistPage(){
        if (wishlistPage == null) {
            wishlistPage = new WishlistPage();
        }
        return wishlistPage;
    }

    public CartPage cartPage;
    public CartPage getCartPage(){
        if (cartPage == null) {
            cartPage = new CartPage();
        }
        return cartPage;
    }

    //============Locator========

    public By emailAccountHeaderDropdown = By.xpath("//a[@href='/tai-khoan/'][1]/p[2]");
    public By avatarIcon = By.xpath("(//div[contains(@class,'flex justify-end')])[1]/div[4]");
    public By successMessage = By.xpath("//div[@role='alert']/div[2]");
    public By heartIconHeader = By.xpath("(//div[contains(@class,'flex justify-end')])[1]/div[2]");
    public By totalItemsHeart = By.xpath("(//div[contains(@class,'flex justify-end')])[1]/div[2]/a/span[1]");
    public By logoutButton = By.xpath("//span[normalize-space()='Đăng xuất']/parent::button");

    //=============

    public void clickWishlistMenu(){
        WebUI.waitForPageLoaded();
        WebUI.clickElement(heartIconHeader);
    }

    public int getWishlistCountFromHeader() {
        WebElement countElement = WebUI.getWebElement(totalItemsHeart); // sửa selector nếu cần
        String rawCount = countElement.getText().trim();

        if (rawCount.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(rawCount);
        } catch (NumberFormatException e) {
            LogUtils.error("❌ Không thể chuyển đổi số lượng wishlist từ chuỗi: " + rawCount);
            return 0;
        }
    }
    public void clickLogin(){
        WebUI.waitForPageLoaded();
        WebUI.clickElement(avatarIcon);
    }

    public void logout(){
        WebUI.hoverElement(avatarIcon);
        WebUI.waitForElementVisible(logoutButton);
        WebUI.clickElement(logoutButton);
    }
}
