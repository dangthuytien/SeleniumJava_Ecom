package com.thuytien.phongthuythienan.Testcase;

import com.thuytien.common.BaseTest;
import com.thuytien.keywords.WebUI;
import com.thuytien.phongthuythienan.Pages.BasePage;
import com.thuytien.phongthuythienan.Pages.CategoryPage;
import com.thuytien.phongthuythienan.Pages.LoginPage;
import com.thuytien.phongthuythienan.Pages.WishlistPage;
import com.thuytien.utils.LogUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class WishlistTestCase extends BaseTest {
    BasePage basePage = new BasePage();
    WishlistPage wishlistPage = basePage.getWishlistPage();
    CategoryPage categoryPage = basePage.getCategoryPage();
    LoginPage loginPage = basePage.getLoginPage();

    @BeforeMethod
    public void openPage() {
        WebUI.openURL("https://phongthuythienan.monamedia.net/danh-muc-san-pham/");
    }

    //Thêm một sản phẩm vào wishlist khi chưa đăng nhập
    @Test
    public void TC01_addToWishlistWithoutLogin() {
        // Truy cập trang danh sách sản phẩm (category page)
        categoryPage.goToCategoryPage();
        WebUI.sleep(2);

        //  Lấy số lượng wishlist ban đầu từ icon header
        int initialWishlistCount = basePage.getWishlistCountFromHeader();
        LogUtils.info("🔹 Wishlist ban đầu: " + initialWishlistCount);

        // Thêm sản phẩm đầu tiên vào wishlist và kiểm tra icon đổi trạng thái
        String productName = categoryPage.addFirstProductToWishlist();
        WebUI.sleep(2);
        LogUtils.info("🛒 Sản phẩm đã thêm vào wishlist: " + productName);

        // Lấy lại số lượng wishlist sau khi thêm
        int updatedWishlistCount = basePage.getWishlistCountFromHeader();
        LogUtils.info("🔹 Wishlist sau khi thêm: " + updatedWishlistCount);

        // Kiểm tra số lượng wishlist đã tăng lên 1
        Assert.assertEquals(updatedWishlistCount, initialWishlistCount + 1,
                "❌ Số lượng sản phẩm trong wishlist không tăng sau khi thêm.");

        // Truy cập trang wishlist
        basePage.clickWishlistMenu(); //
        WebUI.sleep(2);

        // Kiểm tra sản phẩm vẫn hiển thị trong wishlist
        List<String> wishlistProductNames = wishlistPage.getAllProductNamesInWishlist();
        Assert.assertTrue(wishlistProductNames.contains(productName),
                "❌ Sản phẩm không tồn tại trong wishlist");
        LogUtils.info("✅ Sản phẩm vừa thêm có trong danh sách yêu thích");
    }

    //Thêm nhiều sản phẩm vào wishlist khi chưa đăng nhập
    @Test
    public void TC02_addMultipleProductsToWishlistWithoutLogin() {
        // 1. Truy cập trang danh sách sản phẩm
        categoryPage.goToCategoryPage();

        // 2. Lấy số lượng wishlist ban đầu từ header
        int initialWishlistCount = basePage.getWishlistCountFromHeader();
        LogUtils.info("🔹 Wishlist ban đầu: " + initialWishlistCount);

        // 3. Thêm N sản phẩm đầu tiên vào wishlist
        int numberOfProductsToAdd = 3;
        List<String> addedProductNames = categoryPage.addMultipleProductsToWishlist(numberOfProductsToAdd);

        // 4. Kiểm tra icon trái tim đã tô màu đúng số lượng sản phẩm đã thêm
        int filledHearts = categoryPage.countFilledHeartIcons();
        Assert.assertEquals(filledHearts, numberOfProductsToAdd,
                "❌ Số icon trái tim đã tô màu không đúng sau khi thêm nhiều sản phẩm.");

        // 5. Reload lại trang
        WebUI.refreshPage();
        WebUI.sleep(2);

        // 6. Kiểm tra số lượng wishlist đã tăng đúng số lượng sản phẩm đã thêm
        int updatedWishlistCount = basePage.getWishlistCountFromHeader();
        LogUtils.info("🔹 Wishlist sau khi thêm: " + updatedWishlistCount);
        Assert.assertEquals(updatedWishlistCount, initialWishlistCount + numberOfProductsToAdd,
                "❌ Wishlist không tăng đúng số lượng sản phẩm đã thêm.");

        // 7. Vào trang wishlist và kiểm tra các sản phẩm đã có mặt
        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> wishlistItems = wishlistPage.getAllProductNamesInWishlist();

        for (String name : addedProductNames) {
            LogUtils.info("🔹 Tên sản phẩm: " + name);
            Assert.assertTrue(wishlistItems.contains(name),
                    "❌ Sản phẩm '" + name + "' không xuất hiện trong wishlist sau khi thêm.");
        }
    }

//Kiểm tra sản phẩm vừa được thêm vào wishlist sẽ hiển thị đầu trang wishlist
    @Test
    public void TC03_verifyLastAddedProductIsFirstInWishlist() {
        int oldWishlistCount = basePage.getWishlistCountFromHeader();

        String productName = categoryPage.addProductToWishlistByIndex(1); // index = 1 là sản phẩm thứ 2
        LogUtils.info("Tên sản phẩm được thêm: " + productName);

        Assert.assertTrue(categoryPage.isHeartIconFilled(1), "❌ Icon trái tim không được tô màu.");
        int newWishlistCount = basePage.getWishlistCountFromHeader();
        Assert.assertEquals(newWishlistCount, oldWishlistCount + 1, "❌ Wishlist không tăng sau khi thêm.");

        WebUI.refreshPage();
        basePage.clickWishlistMenu();

        String firstWishlistProduct = wishlistPage.getFirstProductNameInWishlist();
        Assert.assertEquals(firstWishlistProduct, productName, "❌ Sản phẩm vừa thêm không hiển thị ở đầu danh sách yêu thích.");
    }

    //Kiểm tra thêm sản phẩm vào wishlist rồi đăng nhập
    @Test
    public void TC04_addToWishlistThenLogin() {
        categoryPage.goToCategoryPage(); // Vào danh sách sản phẩm

        String productName = categoryPage.addProductToWishlistByIndex(0);
        Assert.assertTrue(categoryPage.isHeartIconFilled(0), "❌ Trái tim không được tô màu.");

        int wishlistCount = basePage.getWishlistCountFromHeader();
        Assert.assertTrue(wishlistCount > 0, "❌ Wishlist không tăng sau khi thêm sản phẩm.");

        basePage.clickLogin();
        loginPage.login("monatest@yopmail.com", "Monatest123@");
        WebUI.sleep(2);

        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> wishlistItems = wishlistPage.getAllProductNamesInWishlist();
        WebUI.sleep(2);
        System.out.println("Sản phẩm trong wishlist: "+wishlistItems);

        Assert.assertTrue(wishlistItems.contains(productName), "❌ Sản phẩm vừa thêm không còn trong danh sách yêu thích sau khi đăng nhập.");
    }

    //Kiểm tra thêm nhều sản phẩm vào wishlist rồi đăng nhập
    @Test
    public void TC05_addMultiToWishlistWithoutLogin() {
        // 1. Truy cập trang danh sách sản phẩm
        categoryPage.goToCategoryPage();

        // Thêm N sản phẩm đầu tiên vào wishlist
        int numberOfProductsToAdd = 4;
        List<String> addedProductNames = categoryPage.addMultipleProductsToWishlist(numberOfProductsToAdd);
        WebUI.sleep(2);

        basePage.clickWishlistMenu();
        List<String> wishlistBeforeLogin = wishlistPage.getAllProductNamesInWishlist();

        //Đăng nhập
        WebUI.sleep(2);
        basePage.clickLogin();
        loginPage.login("monatest@yopmail.com", "Monatest123@");
        WebUI.sleep(2);


        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        WebUI.refreshPage();
        WebUI.sleep(2);
        List<String> wishlistAfterLogin = wishlistPage.getAllProductNamesInWishlist();
        System.out.println("wishlistAfterLogin: " + wishlistAfterLogin);

        for (String name : addedProductNames) {
            LogUtils.info("✅ Kiểm tra sản phẩm có tồn tại trong wishlist: " + name);
            Assert.assertTrue(wishlistAfterLogin.contains(name), "❌ Thiếu sản phẩm trong wishlist sau khi login: " + name);
        }
    }

    //Merge danh sách wishlist khi đăng nhập
    @Test
    public void TC06_mergeWishlistAfterLogin() {
        // Bước 1: Login trước để lấy danh sách wishlist cũ
        WebUI.sleep(2);
        basePage.clickLogin();
        loginPage.login("monatest@yopmail.com", "Monatest123@");
        WebUI.sleep(10);

        //Truy cập wishlist
        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> oldWishlist = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("🎯 Danh sách wishlist cũ: " + oldWishlist);

        // Bước 2: Đăng xuất
        basePage.logout();
        WebUI.sleep(5);

        // Bước 3: Thêm 2 sản phẩm mới vào wishlist khi chưa đăng nhập
        categoryPage.goToCategoryPage();
        WebUI.sleep(2);
        int numberOfProductsToAdd = 2;
        List<String> newWishlist = categoryPage.addMultipleProductsToWishlist(numberOfProductsToAdd);
        WebUI.sleep(10);
        LogUtils.info("🎯 Danh sách sản phẩm mới vừa thêm: " + newWishlist);

        // Bước 4: Login lại
        WebUI.sleep(2);
        basePage.clickLogin();
        loginPage.login("monatest@yopmail.com", "Monatest123@");
        WebUI.sleep(5);

        // Bước 5: Truy cập wishlist và kiểm tra merge
        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> mergedWishlist = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("✅ Danh sách wishlist sau khi login: " + mergedWishlist);

        // Kiểm tra chứa toàn bộ sản phẩm cũ
        for (String oldItem : oldWishlist) {
            Assert.assertTrue(mergedWishlist.contains(oldItem), "❌ Thiếu sản phẩm cũ sau khi merge: " + oldItem);
        }

        // Kiểm tra chứa toàn bộ sản phẩm mới
        for (String newItem : newWishlist) {
            Assert.assertTrue(mergedWishlist.contains(newItem), "❌ Thiếu sản phẩm mới sau khi merge: " + newItem);
        }
    }

    @Test
    public void TC07_removeProductFromWishlist() {
        WebUI.waitForElementVisible(basePage.avatarIcon);
        // Bước 1: Đăng nhập
        WebUI.sleep(2);
        basePage.clickLogin();
        loginPage.login("monatest@yopmail.com", "Monatest123@");
        WebUI.sleep(10);

        // Bước 2: Truy cập wishlist
        basePage.clickWishlistMenu();
        WebUI.sleep(2);

        // Bước 3: Kiểm tra có ít nhất 1 sản phẩm
        List<String> wishlistBefore = wishlistPage.getAllProductNamesInWishlist();
        Assert.assertTrue(wishlistBefore.size() > 0, "❌ Wishlist không có sản phẩm để xóa!");

        // Bước 4: Ghi lại tên sản phẩm đầu tiên
        String productToRemove = wishlistBefore.get(0);
        LogUtils.info("🎯 Sản phẩm sẽ xóa: " + productToRemove);

        // Bước 5: Xóa sản phẩm đầu tiên
        wishlistPage.removeProductByName(productToRemove);
        WebUI.sleep(2);


        // Bước 6: Kiểm tra lại danh sách
        List<String> wishlistAfter = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("📋 Danh sách sau khi xóa: " + wishlistAfter);

        // Kết quả mong đợi: sản phẩm đã xóa không còn
        Assert.assertFalse(wishlistAfter.contains(productToRemove), "❌ Sản phẩm vẫn còn sau khi xóa: " + productToRemove);

        // Kiểm tra số lượng giảm 1
        Assert.assertEquals(wishlistBefore.size() - 1, wishlistAfter.size(), "❌ Số lượng sản phẩm không giảm sau khi xóa");
    }

    @Test
    public void TC08_removeFromWishlistByHeartIconAnyPosition() {
        int index = 2; // vị trí bất kỳ trong danh sách

        // Thêm sản phẩm tại vị trí index vào wishlist
        WebUI.waitForElementVisible(basePage.avatarIcon);
        String productName = categoryPage.addProductToWishlistAt(index);
        Assert.assertTrue(categoryPage.isProductHeartIconColoredAt(index), "❌ Icon trái tim chưa được tô màu sau khi thêm");

        // Truy cập wishlist và xác minh sản phẩm có trong danh sách
        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> wishlistItems = wishlistPage.getAllProductNamesInWishlist();
        Assert.assertTrue(wishlistItems.contains(productName), "❌ Sản phẩm không có trong wishlist sau khi thêm");

        // Quay lại danh mục và xóa sản phẩm tại index
        categoryPage.goToCategoryPage();
        categoryPage.removeProductFromWishlistAt(index);
        Assert.assertFalse(categoryPage.isProductHeartIconColoredAt(index), "❌ Icon trái tim vẫn tô màu sau khi xóa");

        // Truy cập wishlist và xác minh sản phẩm đã bị xóa
        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> wishlistAfterRemove = wishlistPage.getAllProductNamesInWishlist();
        Assert.assertFalse(wishlistAfterRemove.contains(productName), "❌ Sản phẩm vẫn còn trong wishlist sau khi xóa");
    }

    //Thêm nhiều sản phẩm vào yêu thích, sau đó xóa một sản phẩm
    @Test
    public void TC09_addMultipleAndRemoveOneFromWishlist() {
        WebUI.waitForElementVisible(basePage.avatarIcon);

        // Thêm 3 sản phẩm vào wishlist
        String product1 = categoryPage.addProductToWishlistAt(0);
        String product2 = categoryPage.addProductToWishlistAt(1);
        String product3 = categoryPage.addProductToWishlistAt(2);
        WebUI.sleep(2);

        // Vào wishlist kiểm tra
        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> wishlistBeforeRemove = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("Danh sách sản phẩm trước khi xóa: " + wishlistBeforeRemove);
        Assert.assertTrue(wishlistBeforeRemove.containsAll(List.of(product1, product2, product3)), "❌ Một trong các sản phẩm chưa có trong wishlist");

        // Xóa sản phẩm thứ hai (product2)
        wishlistPage.removeProductByName(product2);
        WebUI.sleep(2);

        // Kiểm tra danh sách còn lại
        List<String> wishlistAfterRemove = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("Danh sách sản phẩm sau khi xóa: " + wishlistAfterRemove);
        Assert.assertEquals(wishlistAfterRemove.size(), 2, "❌ Số lượng sản phẩm còn lại không đúng");
        Assert.assertFalse(wishlistAfterRemove.contains(product2), "❌ Sản phẩm đã xóa vẫn hiển thị trong wishlist");
        Assert.assertTrue(wishlistAfterRemove.contains(product1) && wishlistAfterRemove.contains(product3), "❌ Một trong 2 sản phẩm còn lại bị thiếu");
    }

    //Thêm nhiều sản phẩm vào yêu thích và xóa một sản phẩm từ trang danh mục
    @Test
    public void TC10_addMultipleThenRemoveOneFromCategoryPage() {
        WebUI.waitForElementVisible(basePage.avatarIcon);

        // 1. Thêm 3 sản phẩm vào wishlist từ danh mục
        String product1 = categoryPage.addProductToWishlistAt(0);
        String product2 = categoryPage.addProductToWishlistAt(1);
        String product3 = categoryPage.addProductToWishlistAt(2);

        // 4. Vào wishlist kiểm tra
        basePage.clickWishlistMenu();
        WebUI.sleep(2);
        List<String> wishlistBeforeRemove = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("Danh sách sản phẩm trước khi xóa: " + wishlistBeforeRemove);
        Assert.assertTrue(wishlistBeforeRemove.containsAll(List.of(product1, product2, product3)), "❌ Một trong các sản phẩm chưa có trong wishlist");

        // 5. Truy cập lại danh mục sản phẩm
        categoryPage.goToCategoryPage();

        // 6. Xóa sản phẩm thứ hai (đã thêm) bằng cách click lại vào trái tim đỏ
        categoryPage.removeProductFromWishlistByName(product2);
        WebUI.sleep(2);

        // 7. Truy cập wishlist
        basePage.clickWishlistMenu();
        WebUI.sleep(2);

        // 8. Kiểm tra danh sách wishlist sau khi xóa
        List<String> wishlistAfterRemove = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("Danh sách sản phẩm sau khi xóa: " + wishlistAfterRemove);
        Assert.assertEquals(wishlistAfterRemove.size(), 2, "❌ Số lượng sản phẩm còn lại không đúng");
        Assert.assertFalse(wishlistAfterRemove.contains(product2), "❌ Sản phẩm đã xóa vẫn còn trong wishlist");
        Assert.assertTrue(wishlistAfterRemove.contains(product1));
        Assert.assertTrue(wishlistAfterRemove.contains(product3));
    }

    @Test
    public void TC11_clearAllWishlistItems() {
        WebUI.waitForElementVisible(basePage.avatarIcon);

        // 2. Truy cập danh mục và thêm sản phẩm
        categoryPage.addProductToWishlistAt(0);
        categoryPage.addProductToWishlistAt(1);

        // 3. Vào wishlist
        basePage.clickWishlistMenu();
        WebUI.sleep(2);

        // 4. Đảm bảo có ít nhất 2 sản phẩm trước khi xóa
        List<String> productsBeforeClear = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("Danh sách sản phẩm trước khi xóa: " + productsBeforeClear);
        Assert.assertTrue(productsBeforeClear.size() >= 2, "❌ Không đủ sản phẩm để kiểm thử chức năng Clear All");

        // 5. Nhấn nút Xóa tất cả
        wishlistPage.clickClearAllButton();

        // 6. Xác nhận nếu có popup xác nhận
        wishlistPage.confirmClearAllPopupIfPresent();

        // 7. Kiểm tra lại danh sách wishlist
        List<String> productsAfterClear = wishlistPage.getAllProductNamesInWishlist();
        LogUtils.info("Danh sách sản phẩm sau khi xóa: " + productsAfterClear);
        Assert.assertEquals(productsAfterClear.size(), 0, "❌ Danh sách wishlist không rỗng sau khi xóa tất cả");

        // 8. Kiểm tra icon trái tim trên header = 0
        int wishlistCount = basePage.getWishlistCountFromHeader();
        Assert.assertEquals(wishlistCount, 0, "❌ Số lượng sản phẩm ở icon trái tim không về 0 sau khi xóa tất cả");
    }




}
