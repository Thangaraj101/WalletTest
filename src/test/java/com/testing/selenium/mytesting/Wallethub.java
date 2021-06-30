package com.testing.selenium.mytesting;

import com.testing.pages.PageActions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Wallethub extends PageActions{
    @Test
    public void Test1() throws InterruptedException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Username\n");
        String uName = br.readLine();
        System.out.println("Enter Password\n");
        String pWord = br.readLine();
        PageActions page = new PageActions();
        page.fLogInAndPost(uName.trim(), pWord.trim());
        Assert.assertTrue(true,"Successfully post submitted");

    }

    @Test
    public void Test2() throws InterruptedException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Username\n");
        String uName = br.readLine();
        System.out.println("Enter Password\n");
        String pWord = br.readLine();
        PageActions page = new PageActions();

        //Login to the page
        page.wLogIn(uName.trim(), pWord.trim());

        //Writing review
        page.wWriteReview();
        PageActions.driver.quit();
    }

    @AfterSuite
    public void closeBrowser(){
        PageActions.driver.close();
        PageActions.driver.quit();
    }


}
