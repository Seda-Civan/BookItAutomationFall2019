package com.bookit.pages;

import org.apache.commons.math3.analysis.function.Exp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SelfPage extends BasePage{

    /**
     *
     * @param value can be: role, name, team,  batch, campus
     * @returnit's value
     */
    public String getUserInfo(String value){
        String xpath = "//p[text()='"+value+"']/preceding-sibling::p";
        WebElement valueElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return valueElement.getText().trim();
    }


}
