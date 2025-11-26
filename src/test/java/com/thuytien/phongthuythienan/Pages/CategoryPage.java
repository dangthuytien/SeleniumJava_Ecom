package com.thuytien.phongthuythienan.Pages;

import com.thuytien.keywords.WebUI;
import com.thuytien.utils.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CategoryPage {

    private By filterButton = By.xpath("//div[contains(text(),'Bộ lọc')]");
    private By filterApplyButton = By.xpath("(//span[contains(text(),'Áp dụng')])/parent::button");
    private By closeButton = By.xpath("(//span[contains(text(),'Đóng')])/parent::button");
    private By refreshButton = By.xpath("//div[normalize-space()='Làm mới']");
    private By priceMinInput = By.xpath("");
    private By priceMaxInput = By.xpath("");
    private By minPriceSlider  = By.xpath("(//div[@class='mb-3']/div[3]/descendant::input[1])/parent::span");
    private By maxPriceSlider   = By.xpath("(//div[@class='mb-3']/div[3]/descendant::input[2])/parent::span");
    private By minPrice   = By.xpath("//div[@class='mb-3']/div[2]/span[1]");
    private By maxPrice   = By.xpath("//div[@class='mb-3']/div[2]/span[2]");
    private By productPrices   = By.xpath("((//div[contains(@class,'card-wrapper h-full flex flex-col')])/child::div[2])/child::div[2]/span[1]");
    private By popupFilter = By.xpath("(//h3[contains(text(),'Bộ lọc')])/ancestor::div[5]");
    private By itemDropdown = By.xpath("//ul[@role='listbox']/li");
    private By sortDropdown = By.xpath("(//div[@id='mui-component-select-sort-product'])[1]");
    public By sortButton = By.xpath("//div[contains(text(),'Sắp xếp theo:')]/following-sibling::div/button[1]");
    private By nextButton = By.xpath("(//button[@aria-label='Go to next page'])[1]");
    private By productNames = By.xpath("//h3/a/span");
    private By productHeartIcons = By.xpath("//div[contains(@class,'btn-wishlist')]/img");
    private By addToCartButton = By.xpath("(//span[contains(text(),'Thêm vào giỏ')])/parent::button");
    private By productPopup =By.xpath("//div[@role='dialog']");
    private By addToCartButtonInPopup = By.xpath("(//div[@role='dialog']/descendant::span[normalize-space()='Thêm vào giỏ'])/parent::button");
    private By miniCartPopup = By.xpath("(//div[contains(text(),'GIỎ HÀNG')]/parent::div)/parent::div");
    private By productNameInPopup = By.xpath("//a[@class='hover:text-primary']/h1");
    private By quantityProductInPopup = By.xpath("/div[@class='flex']/div/div"); // ô hiện số lượng
    private By increaseButton = By.xpath("//div[@class='flex']/div/button[2]"); // nút +
    private By decreaseButton = By.xpath("//div[@class='flex']/div/button[1]");

    public void goToCategoryPage() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/danh-muc-san-pham/");
    }

    public void clickFilter() {
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
        WebUI.clickElement(filterButton);
        WebUI.waitForElementVisible(popupFilter);
    }
    public void setPriceSlider(int minOffset, int maxOffset){
        WebUI.dragSliderByOffset(minPriceSlider, minOffset);
        WebUI.dragSliderByOffset(maxPriceSlider, maxOffset);
        WebUI.sleep(2);
        WebUI.clickElement(filterApplyButton);
    }

    public List<Integer> getAllProductPrices() {
        List<WebElement> prices = WebUI.getWebElements(productPrices);
        return prices.stream().map(e -> {
            String raw = e.getText().replaceAll("[^\\d]", "");
            if (raw.isEmpty()) return 0;
            return Integer.parseInt(raw);
        }).collect(Collectors.toList());
    }
    public List<Integer> getAllProductPricesAcrossPages() {
        List<Integer> allPrices = new ArrayList<>();

        while (true) {
            // Lấy giá từ trang hiện tại
            List<WebElement> prices = WebUI.getWebElements(productPrices);
            List<Integer> pricesOnPage = prices.stream().map(e -> {
                String raw = e.getText().replaceAll("[^\\d]", "");
                if (raw.isEmpty()) return 0;
                return Integer.parseInt(raw);
            }).collect(Collectors.toList());

            allPrices.addAll(pricesOnPage);

            // Kiểm tra nút "Next" có tồn tại và có thể nhấn
            WebUI.scrollToElement(nextButton);
            if (WebUI.isEnabled(nextButton)) {
                WebUI.sleep(2);
                WebUI.clickElement(nextButton);
                WebUI.sleep(2);
                WebUI.waitForPageLoaded();
            } else {
                break; // Không còn trang tiếp theo
            }
        }

        return allPrices;
    }

    public List<String> getAllProductNamesAcrossPages() {
        List<String> allNames = new ArrayList<>();

        while (true) {
            List<WebElement> nameElements = WebUI.getWebElements(productNames);
            List<String> names = nameElements.stream()
                    .map(e -> e.getText().trim().toLowerCase())
                    .collect(Collectors.toList());
            allNames.addAll(names);

            WebUI.scrollToElement(nextButton);
            if (WebUI.isEnabled(nextButton)) {
                WebUI.sleep(2);
                WebUI.clickElement(nextButton);
                WebUI.sleep(2);
                WebUI.waitForPageLoaded();
            } else {
                break; // Không còn trang tiếp theo
            }
        }

        return allNames;
    }



    public void sortByText(String text) {
        WebUI.waitForElementVisible(sortDropdown);
        WebUI.clickElement(sortDropdown);
        WebUI.selectOptionDropdownDynamicByText(itemDropdown, text); // Ví dụ: "price" cho giá tăng dần
        WebUI.sleep(2);
    }

    public boolean isPriceSortedAscending() {
        List<Integer> prices = getAllProductPricesAcrossPages();
        // Kiểm tra có đủ dữ liệu để kiểm tra hay không
        if (prices == null || prices.size() < 2) {
            LogUtils.error("❌ Không đủ dữ liệu để kiểm tra sắp xếp. Số lượng sản phẩm: " + (prices == null ? 0 : prices.size()));
            Assert.fail("❌ Không đủ dữ liệu để kiểm tra sắp xếp. Số lượng sản phẩm: " + (prices == null ? 0 : prices.size()));
            return false;
        }

        // Log tất cả giá sản phẩm
        for (int i = 0; i < prices.size(); i++) {
            LogUtils.info("🔸 Giá sản phẩm [" + i + "]: " + prices.get(i));
        }

        // Kiểm tra giá có được sắp xếp từ thấp đến cao
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                LogUtils.error("❌ Giá không tăng dần tại vị trí [" + i + "]: " + prices.get(i) + " > " + prices.get(i + 1));
                return false;
            }
        }
        return true;
    }

    public boolean isPriceSortedDescending() {
        List<Integer> prices = getAllProductPricesAcrossPages();
        // Kiểm tra có đủ dữ liệu để kiểm tra hay không
        if (prices == null || prices.size() < 2) {
            LogUtils.error("❌ Không đủ dữ liệu để kiểm tra sắp xếp. Số lượng sản phẩm: " + (prices == null ? 0 : prices.size()));
            Assert.fail("❌ Không đủ dữ liệu để kiểm tra sắp xếp. Số lượng sản phẩm: " + (prices == null ? 0 : prices.size()));
            return false;
        }

        // Log tất cả giá sản phẩm
        for (int i = 0; i < prices.size(); i++) {
            LogUtils.info("🔸 Giá sản phẩm [" + i + "]: " + prices.get(i));
        }

        // Kiểm tra giá có được sắp xếp từ cao đến thấp
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) < prices.get(i + 1)) {
                LogUtils.error("❌ Giá không tăng dần tại vị trí [" + i + "]: " + prices.get(i) + " < " + prices.get(i + 1));
                return false;
            }
        }
        return true;
    }

    public boolean isNameSortedAscending() {
        List<String> names = getAllProductNamesAcrossPages();

        LogUtils.info("📦 Danh sách tên sản phẩm thu thập được:");
        for (String name : names) {
            LogUtils.info("🔹 " + name);
        }

        List<String> sorted = new ArrayList<>(names);
        // Sắp xếp theo chuẩn tiếng Việt (Locale)
        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);
        sorted.sort(collator);

        LogUtils.info("📑 Danh sách tên sản phẩm sau khi sắp xếp tăng dần:");
        for (String name : sorted) {
            LogUtils.info("✅ " + name);
        }

        boolean isSorted = names.equals(sorted);
        LogUtils.info("📊 Kết quả so sánh: " + (isSorted ? "ĐÚNG thứ tự tăng dần ✅" : "SAI thứ tự ⛔"));

        return isSorted;
    }

    public boolean isNameSortedDescending() {
        List<String> names = getAllProductNamesAcrossPages();

        LogUtils.info("📦 Danh sách tên sản phẩm thu thập được:");
        for (String name : names) {
            LogUtils.info("🔹 " + name);
        }

        List<String> sorted = new ArrayList<>(names);

        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);

        sorted.sort(collator.reversed()); // Giảm dần

        LogUtils.info("📑 Danh sách tên sản phẩm sau khi sắp xếp giảm dần:");
        for (String name : sorted) {
            LogUtils.info("✅ " + name);
        }

        boolean isSorted = names.equals(sorted);
        LogUtils.info("📊 Kết quả so sánh: " + (isSorted ? "ĐÚNG thứ tự giảm dần ✅" : "SAI thứ tự ⛔"));

        return isSorted;
    }

    public String addFirstProductToWishlist() {
        List<WebElement> products = WebUI.getWebElements(productHeartIcons);
        List<WebElement> names = WebUI.getWebElements(productNames);

        // Lấy tên sản phẩm đầu tiên
        String name = names.get(0).getText();

        // Lưu src trước khi click
        String srcBefore = products.get(0).getAttribute("src");
        LogUtils.info("📌 src trước khi click: " + srcBefore);

        // Click icon trái tim sản phẩm đầu tiên
        products.get(0).click();
        WebUI.sleep(2);

        // Lưu src sau khi click
        String srcAfter = products.get(0).getAttribute("src");
        LogUtils.info("📌 src sau khi click: " + srcAfter);

        // Kiểm tra icon đã được tô màu (src đã đổi)
        if (srcBefore.equals(srcAfter)) {
            LogUtils.error("❌ Icon trái tim không đổi sau khi click. Có thể chưa được thêm vào wishlist.");
            throw new RuntimeException("❌ Icon trái tim không được tô màu sau khi thêm vào wishlist.");
        } else {
            LogUtils.info("✅ Icon trái tim đã được tô màu (src đã đổi).");
        }

        return name;
    }

    //Wishlist
    public List<String> addMultipleProductsToWishlist(int count) {
        List<WebElement> products = WebUI.getWebElements(productHeartIcons);
        List<WebElement> names = WebUI.getWebElements(productNames);

        List<String> addedNames = new ArrayList<>();
        for (int i = 0; i < count && i < products.size(); i++) {
            products.get(i).click();
            WebUI.sleep(1);
            addedNames.add(names.get(i).getText());
        }
        return addedNames;
    }

    public int countFilledHeartIcons() {
        List<WebElement> hearts = WebUI.getWebElements(productHeartIcons);
        int count = 0;
        for (WebElement heart : hearts) {
            String src = heart.getAttribute("src");
            if (src.contains("filled") || src.contains("red")) {
                count++;
            }
        }
        return count;
    }
    public boolean isHeartIconFilled(int index) {
        List<WebElement> hearts = WebUI.getWebElements(productHeartIcons);
        String iconSrc = hearts.get(index).getAttribute("src");
        return iconSrc.contains("filled") || iconSrc.contains("red"); // tuỳ vào icon
    }

    public String addProductToWishlistByIndex(int index) {
        List<WebElement> hearts = WebUI.getWebElements(productHeartIcons);
        List<WebElement> names = WebUI.getWebElements(productNames);
        String name = names.get(index).getText();
        hearts.get(index).click();
        WebUI.sleep(2);
        return name;
    }

    public String addProductToWishlistAt(int index) {
        List<WebElement> products = WebUI.getWebElements(productHeartIcons);
        List<WebElement> names = WebUI.getWebElements(productNames);
        String name = names.get(index).getText();
        products.get(index).click();
        WebUI.sleep(1);
        return name;
    }

    public void removeProductFromWishlistAt(int index) {
        List<WebElement> products = WebUI.getWebElements(productHeartIcons);
        products.get(index).click();
        WebUI.sleep(1);
    }

    public boolean isProductHeartIconColoredAt(int index) {
        WebElement icon = WebUI.getWebElements(productHeartIcons).get(index);
        String src = icon.getAttribute("src");
        return src.contains("red") || src.contains("fill"); // tùy theo ảnh
    }

    public void removeProductFromWishlistByName(String productName) {
        List<WebElement> products = WebUI.getWebElements(productNames);
        List<WebElement> heartIcons = WebUI.getWebElements(productHeartIcons);

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getText().trim().equalsIgnoreCase(productName)) {
                heartIcons.get(i).click(); // click icon đã được tô màu
                WebUI.sleep(1);
                break;
            }
        }
    }

    //Add to cart
    public void openPopupProductQuickViewByIndex(int index) {
        WebUI.scrollToElement(addToCartButton);
        List<WebElement> addToCartButtons = WebUI.getWebElements(addToCartButton);
        addToCartButtons.get(index).click();
        WebUI.waitForElementVisible(productPopup);
    }

    public void clickAddToCartInPopup() {
        WebUI.clickElement(addToCartButtonInPopup);
        WebUI.waitForElementVisible(miniCartPopup);
    }

    public void setQuantityInPopup(int quantity) {
        WebElement qtyField = WebUI.getWebElement(quantityProductInPopup);
        int currentQuantity = Integer.parseInt(qtyField.getAttribute("value"));

        if (quantity == currentQuantity) return;

        By buttonToClick = quantity > currentQuantity ? increaseButton : decreaseButton;
        int steps = Math.abs(quantity - currentQuantity);

        for (int i = 0; i < steps; i++) {
            WebUI.clickElement(buttonToClick);
            WebUI.sleep(300); // Chờ một chút để UI cập nhật số lượng
        }
    }


    public String getProductNameFromPopup() {
        return WebUI.getElementText(productNameInPopup);
    }

}
