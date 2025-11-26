package com.thuytien.DataProvider;

import com.thuytien.helpers.ExcelHelpers;
import org.testng.annotations.DataProvider;

public class DataProviderFactory {

    @DataProvider (name = "data_provider_register_success")
    public Object[][] dataRegisterFromExcel (){
        ExcelHelpers excelHelper = new ExcelHelpers();
        Object[][] data = excelHelper.getExcelDataProvider("./src/test/resources/testdata/registerData.xlsx", "Sheet1");
        System.out.println("Login Data from Excel: " + data);
        return data;
    }

    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        ExcelHelpers excelHelper = new ExcelHelpers();
        Object[][] data = excelHelper.getExcelDataProvider("./src/test/resources/testdata/searchdata.xlsx", "Sheet1");
        System.out.println("Data search: " + data);
        return data;
    }
}
