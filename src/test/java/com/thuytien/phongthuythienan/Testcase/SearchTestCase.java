package com.thuytien.phongthuythienan.Testcase;

import com.thuytien.common.BaseTest;
import com.thuytien.keywords.WebUI;
import com.thuytien.phongthuythienan.Pages.BasePage;
import com.thuytien.phongthuythienan.Pages.SearchPage;
import com.thuytien.utils.LogUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchTestCase extends BaseTest {
    BasePage basePage = new BasePage();
    SearchPage searchPage = basePage.getSearchPage();

    @BeforeMethod
    public void openLoginPage() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/");
    }


    @Test (priority = 1)
    public void TC01_openSearchPopup() {
        searchPage.openSearchPopup();
        Assert.assertTrue(WebUI.checkElementExist(searchPage.searchForm), "❌ Popup không hiển thị");
    }

    @Test (priority = 2)
    public void TC02_validKeywordShowsPreview() {
        searchPage.openSearchPopup();
        searchPage.searchInPopup("vòng tay");
        Assert.assertTrue(searchPage.isPopupResultDisplayed(), "❌ Không hiển thị gợi ý sản phẩm");
    }

    @Test(priority = 3)
    public void TC03_invalidKeywordShowsNoResult() {
        searchPage.openSearchPopup();
        searchPage.searchInPopup("abcdefxyz");
        Assert.assertTrue(searchPage.isNoPopupResultDisplayed(), "❌ Không hiển thị thông báo không có sản phẩm");
    }

    @Test(priority = 4)
    public void TC04_clickViewAllResults() {
        String keyword ="vòng tay";
        searchPage.openSearchPopup();
        searchPage.searchInPopup(keyword);
        searchPage.clickViewAll();
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        LogUtils.info("url encode: "+ encodedKeyword);
        WebUI.sleep(2);
        String currentUrl = WebUI.getCurrentUrl();
        LogUtils.info(WebUI.getCurrentUrl());
        Assert.assertTrue(currentUrl.contains("?search=" + encodedKeyword), "❌ Không chuyển sang trang kết quả tìm kiếm");
    }

    @Test(priority = 5)
    public void TC05_pressEnterGoesToSearchPage() {
        String keyword ="vòng tay";
        searchPage.openSearchPopup();
        searchPage.searchInPopup(keyword);
        searchPage.pressEnterInSearch();
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        LogUtils.info("url encode: "+ encodedKeyword);
        WebUI.sleep(2);
        String currentUrl = WebUI.getCurrentUrl();
        LogUtils.info(WebUI.getCurrentUrl());
        Assert.assertTrue(currentUrl.contains("?search=" + encodedKeyword), "❌ Nhấn Enter không chuyển sang trang kết quả");
    }

    @Test (priority = 6)
    public void TC06_searchPopupWithValidKeyword() {
        searchPage.openSearchPopup();
        searchPage.verifyResultPopup("Tỳ hưu");
    }

    @Test(priority = 7)
    public void TC07_comparePopupAndSearchResultPage() {
        searchPage.openSearchPopup();
        searchPage.verifyResultPopupAndResultPage("Tỳ hưu");
    }


}
