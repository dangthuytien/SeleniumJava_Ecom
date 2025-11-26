package com.thuytien.phongthuythienan.Testcase;

import com.thuytien.common.BaseTest;
import com.thuytien.keywords.WebUI;
import com.thuytien.phongthuythienan.Pages.BasePage;
import com.thuytien.phongthuythienan.Pages.CategoryPage;
import com.thuytien.utils.LogUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CategoryTestCase extends BaseTest {

    BasePage basePage = new BasePage();
    CategoryPage categoryPage = basePage.getCategoryPage();

    @BeforeMethod
    public void openCategoryPage() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/danh-muc-san-pham/");
    }

    //filter by price
    @Test(priority = 3)
    public void TC03_filterByPriceSliderXToY() {
        categoryPage.clickFilter();
        WebUI.sleep(2);


        // Kéo thanh giá từ 500.0000 đến 3 triệu
        categoryPage.setPriceSlider(50, 0);
        WebUI.sleep(2); // Đợi filter áp dụng

        // Kiểm tra sản phẩm hiển thị đúng theo khoảng giá
        List<Integer> priceTexts = categoryPage.getAllProductPricesAcrossPages();

        Assert.assertFalse(priceTexts.isEmpty(), "❌ Không có sản phẩm nào được hiển thị sau khi lọc!");

        for (Integer price : priceTexts) {
            LogUtils.info("🔸 Giá sản phẩm: " + price);
            Assert.assertTrue(price >= 500000 && price <= 3000000,
                    "❌ Giá sản phẩm không nằm trong khoảng 500.000 – 3.000.000: " + price);
        }
    }
    //sort price ascending
    @Test(priority = 11)
    public void TC11_verifySortPriceAscending() {
        categoryPage.sortByText("Giá");
        WebUI.sleep(2);
        // Giá tăng dần
        Assert.assertTrue(categoryPage.isPriceSortedAscending(), "❌ Giá không được sắp xếp từ thấp đến cao");
    }

    //sort price descending
    @Test(priority = 12)
    public void TC12_verifySortPriceDescending() {
        categoryPage.sortByText("Giá");
        WebUI.clickElement(categoryPage.sortButton);
        WebUI.sleep(2);
        // Giá giảm dần
        Assert.assertTrue(categoryPage.isPriceSortedDescending(), "❌ Giá không được sắp xếp từ thấp đến cao");
    }

    @Test(priority = 13)
    public void TC13_verifySortNameAscending(){
        categoryPage.sortByText("Tên sản phẩm");
        WebUI.sleep(2);
        Assert.assertTrue(categoryPage.isNameSortedAscending(), "❌ Tên sản phẩm không được sắp xếp từ A đến Z trên tất cả các trang");
    }


    @Test(priority = 14)
    public void TC14_verifySortNameDescending(){
        categoryPage.sortByText("Tên sản phẩm");
        WebUI.clickElement(categoryPage.sortButton);
        WebUI.sleep(2);
        Assert.assertTrue(categoryPage.isNameSortedDescending(), "❌ Tên sản phẩm không được sắp xếp từ Z đến A trên tất cả các trang");
    }

    @Test(priority = 16)
    public void TC16_verifyFilterPriceAndSortByNameAscending() {
        categoryPage.clickFilter();
        WebUI.sleep(2);

        // Bước 1: Lọc theo giá từ 0 - 1 triệu
        categoryPage.setPriceSlider(0, -197);
        WebUI.sleep(2); // Chờ filter áp dụng

        // Bước 2: Sắp xếp theo tên tăng dần
        categoryPage.sortByText("Tên sản phẩm"); //
        WebUI.sleep(2); // Chờ sắp xếp

        // Bước 3: Kiểm tra tất cả sản phẩm qua các trang
        List<Integer> prices = categoryPage.getAllProductPricesAcrossPages();
        for (int price : prices) {
            Assert.assertTrue(price >= 0 && price <= 1000000, "❌ Sản phẩm có giá không nằm trong khoảng lọc: " + price);
        }

        // Bước 4: Kiểm tra tên sản phẩm đã sắp xếp đúng
        Assert.assertTrue(categoryPage.isNameSortedAscending(), "❌ Tên sản phẩm không được sắp xếp tăng dần");
    }


}
