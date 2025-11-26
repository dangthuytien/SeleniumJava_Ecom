package com.thuytien.phongthuythienan.Pages;

import com.thuytien.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage {

    private By miniCartPopup = By.xpath("(//div[contains(text(),'GIỎ HÀNG')]/parent::div)/parent::div");
    private By miniCartProductName = By.xpath("//li[@class='flex gap-3']/child::div[2]/a");
    private By miniCartProductItem = By.xpath("//li[@class='flex gap-3']");
    private By quantityProductInMiniCart = By.xpath("(//button[contains(@aria-label,'minus')])[1]/following-sibling::div"); // ô hiện số lượng
    private By increaseButtonInMiniCart  = By.xpath("(//button[contains(@aria-label,'minus')])[2]"); // nút +
    private By decreaseButtonInMiniCart  = By.xpath("(//button[contains(@aria-label,'minus')])[1]");
    private By closeMiniCartButton = By.xpath("(//div[contains(text(),'GIỎ HÀNG')]/parent::div)/parent::div//*[name()='svg']");

    public boolean isMiniCartDisplayed() {
        return WebUI.isElementVisible(miniCartPopup);
    }

    public List<String> getMiniCartProductNames() {
        return WebUI.getElementsText(miniCartProductName);
    }

    public int getMiniCartProductCount() {
        return WebUI.getWebElements(miniCartProductItem).size();
    }

    public int getMiniCartProductQuantity(String productName) {
        List<WebElement> products = WebUI.getWebElements(miniCartProductName);
        List<WebElement> quantities = WebUI.getWebElements(quantityProductInMiniCart);

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getText().equalsIgnoreCase(productName)) {
                return Integer.parseInt(quantities.get(i).getText());
            }
        }
        return 0;
    }

    public void closeMiniCart() {
        WebUI.clickElement(closeMiniCartButton);
        WebUI.sleep(1);
    }

//    public void goToCartPage() {
//        WebUI.clickElement(viewCartButtonLocator);
//    }
//
//    public List<String> getCartPageProductNames() {
//        return WebUI.getTextListFromElements(cartPageProductNameLocator);
//    }

}
