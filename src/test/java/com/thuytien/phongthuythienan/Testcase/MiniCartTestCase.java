package com.thuytien.phongthuythienan.Testcase;

import com.thuytien.common.BaseTest;
import com.thuytien.keywords.WebUI;
import com.thuytien.phongthuythienan.Pages.*;
import com.thuytien.utils.LogUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class MiniCartTestCase extends BaseTest {
    BasePage basePage = new BasePage();
    CategoryPage categoryPage = basePage.getCategoryPage();
    CartPage cartPage = basePage.getCartPage();
    LoginPage loginPage = basePage.getLoginPage();

    @BeforeMethod
    public void openPage() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/danh-muc-san-pham/");
    }

    //Thêm sản phẩm vào giỏ hàng
    @Test
    public void TC01_addProductToCartFromPopup() {
        WebUI.waitForElementVisible(basePage.avatarIcon);
        categoryPage.openPopupProductQuickViewByIndex(0);
        String expectedName = categoryPage.getProductNameFromPopup();
        categoryPage.clickAddToCartInPopup();

        Assert.assertTrue(cartPage.isMiniCartDisplayed(), "Mini cart không hiển thị sau khi thêm sản phẩm.");
        List<String> miniCartNames = cartPage.getMiniCartProductNames();
        LogUtils.info("Danh sách sản phẩm trong giỏ hàng: " + miniCartNames);
        Assert.assertTrue(miniCartNames.contains(expectedName), "Tên sản phẩm trong mini cart không đúng.");
    }

    //Thêm nhiều sản phẩm vào giỏ hàng
    @Test
    public void TC02_addMultipleProductsToCart() {

        // Thêm 3 sản phẩm
        for (int i = 0; i < 3; i++) {
            categoryPage.openPopupProductQuickViewByIndex(i);
            categoryPage.clickAddToCartInPopup();
            cartPage.closeMiniCart(); // Đóng mini cart sau mỗi lần thêm
        }

        Assert.assertTrue(cartPage.getMiniCartProductCount() >= 3, "Mini cart không chứa đủ sản phẩm.");
    }


}
