package com.thuytien.phongthuythienan.Pages;

import com.thuytien.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class WishlistPage {
    private By heartIcon = By.xpath("//div[contains(@class,'btn-wishlist')]/img");
    private By productTNames = By.xpath("//h3/a/span");
    private By firstWishlistProductName = By.xpath("(//h3/a/span)[1]");
    private By removeAllButton= By.xpath("//span[normalize-space()='Xóa tất cả']/parent::button");
    private By cancelButton = By.xpath("");
    private By acceptButton = By.xpath("(//span[contains(text(),'Xác nhận')])[1]/parent::button");

    private By productNames = By.xpath("//h3/a/span");


    public List<String> getAllProductNamesInWishlist() {
        List<WebElement> names = WebUI.getWebElements(productTNames);
        return names.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String getFirstProductNameInWishlist() {
        return WebUI.getElementText(firstWishlistProductName); // `firstWishlistProductName` là `By` của tên đầu tiên trong wishlist
    }

    public void removeProductByName(String productName) {
        List<WebElement> products = WebUI.getWebElements(productNames);
        List<WebElement> heartIcons = WebUI.getWebElements(heartIcon);

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getText().trim().equalsIgnoreCase(productName.trim())) {
                heartIcons.get(i).click();
                WebUI.sleep(1); // hoặc đợi element biến mất
                break;
            }
        }
    }

    public void clickClearAllButton() {
        WebUI.clickElement(removeAllButton);
        WebUI.sleep(1);
    }

    public void confirmClearAllPopupIfPresent() {
        if (WebUI.isElementVisible(acceptButton)) {
            WebUI.clickElement(acceptButton);
            WebUI.sleep(1);
        }
    }

}
