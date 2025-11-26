package com.thuytien.phongthuythienan.Pages;

import com.thuytien.keywords.WebUI;
import com.thuytien.utils.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.text.Normalizer;
import java.util.List;

public class SearchPage {

    BasePage basePage = new BasePage();

    private By searchIcon = By.xpath("(//div[@class='container-second h-full']/descendant::div[@class='flex justify-end '])/div[1]/div");
    private By searchInput = By.xpath("//input[@placeholder='Tìm kiếm...']");
    public By popupResultItem = By.xpath("(//p[contains(normalize-space(),'TÌM KIẾM SẢN PHẨM')]/parent::div)/descendant::ul");
    public By popupResultItems = By.xpath("(//p[contains(normalize-space(),'TÌM KIẾM SẢN PHẨM')]/parent::div)/descendant::ul/li");
    private By popupResultTitleItems = By.xpath("(//p[contains(normalize-space(),'TÌM KIẾM SẢN PHẨM')]/parent::div)/descendant::ul/li/div/a");
    private By noResultText = By.xpath("(//p[contains(normalize-space(),'TÌM KIẾM SẢN PHẨM')]/parent::div)/descendant::ul/div/p");
    private By viewAllButton = By.xpath("//span[contains(text(),'Xem toàn bộ sản phẩm')]/parent::button");
    public By searchForm = By.xpath("//p[contains(normalize-space(),'TÌM KIẾM SẢN PHẨM')]/parent::div");
    private By resultItems = By.xpath("//div[@class='sec-pro']/descendant::h3");

    public void openSearchPopup() {
        WebUI.clickElement(searchIcon);
    }

    public void searchInPopup(String keyword) {
        WebUI.setText(searchInput, keyword);
        WebUI.waitForPageLoaded();
    }

    public boolean isPopupResultDisplayed() {
        return WebUI.checkElementExist(popupResultItem);
    }

    public boolean isNoPopupResultDisplayed() {
        return WebUI.checkElementExist(noResultText);
    }

    public void clickViewAll() {
        WebUI.clickElement(viewAllButton);
    }

    public void pressEnterInSearch() {
        WebUI.pressEnter(searchInput);
    }
    public List<String> getPopupProductTitles() {
        return WebUI.getElementsText(popupResultTitleItems);
    }

    public List<String> getSearchResultProductTitles() {
        return WebUI.getElementsText(resultItems);
    }

    private boolean isSimilar(String keyword, String title) {
        // Chuyển chuỗi về không dấu và thường
        String normalizedKeyword = Normalizer.normalize(keyword, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();

        String normalizedTitle = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();

        // Tách từ và kiểm tra mỗi từ trong keyword có xuất hiện tương tự trong title không
        for (String key : normalizedKeyword.split(" ")) {
            if (normalizedTitle.contains(key)) {
                return true;
            }
        }

        return false;
    }

    public void verifyResultPopup(String keyword){
        searchInPopup(keyword);

        // Chờ popup hiển thị
        WebUI.waitForElementVisible(popupResultItem);
        WebUI.sleep(2);

        // Kiểm tra popup có kết quả
        Assert.assertTrue(WebUI.getWebElements(popupResultItems).size() > 0,
                "❌ Popup không hiển thị kết quả tìm kiếm");

        // Kiểm tra kết quả popup có chứa từ khóa
        for (WebElement item : WebUI.getWebElements(popupResultTitleItems)) {
            String productTitle = item.getText().toLowerCase();
            LogUtils.info("🔸 Title: " + productTitle + " | Từ khóa: " + keyword.toLowerCase());
            boolean match = isSimilar(keyword, productTitle);
            Assert.assertTrue(match,
                    "❌ Kết quả **'" + productTitle + "'** không chứa từ khóa: '" + keyword.toLowerCase() + "'");
        }
    }

    public void verifyResultPopupAndResultPage(String keyword){
        searchInPopup(keyword);

        // Lưu kết quả từ popup
        WebUI.waitForElementVisible(popupResultItem);
        WebUI.sleep(2);
        List<String> popupTitles = getPopupProductTitles();

        // Nhấn Enter để đến trang kết quả
        pressEnterInSearch();
        WebUI.sleep(2);
        WebUI.waitForPageLoaded();

        // Lấy kết quả từ trang kết quả
        List<String> resultTitles = getSearchResultProductTitles();

        // So sánh từng tên có trong popup cũng phải xuất hiện ở trang kết quả
        for (String popupTitle : popupTitles) {
            LogUtils.info("🔸 Title: " + popupTitle + " | Từ khóa: " + keyword.toLowerCase());
            Assert.assertTrue(
                    resultTitles.stream().anyMatch(title -> title.equalsIgnoreCase(popupTitle)),
                    "❌ Sản phẩm trong popup không tìm thấy trong trang kết quả: " + popupTitle
            );
        }
    }
}
