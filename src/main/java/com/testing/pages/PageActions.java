package com.testing.pages;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.util.Random;


public class PageActions {
    public static By userName = By.xpath("//*[@id='email']");
    public static By passWord = By.xpath("//*[@id='pass']");
    public static By login = By.xpath("//*[@name='login']");
    public static By statusSpan = By.xpath("//span[contains(text(),\"What's on your mind\")]");
    public static By statusDiv = By.xpath("//*[contains(@aria-label,\"What's on your mind\")]//span");
    public static By buttonPost = By.xpath("//span[text()='Post']");

    //Wallethub Homepage
    public static By w_Signin =By.xpath("//*[@class='brgm-button brgm-signup brgm-signup-login']");
    public static By w_UserName= By.xpath("//*[@id='email']");
    public static By w_Password= By.xpath("//*[@id='password']");
    public static By w_LoginBtn=By.xpath("//button/*[text()='Login']");
    public static By w_ProfileName = By.xpath("//*[@class='profile-name']");
    public static By w_ClickReview = By.xpath("//*[@class='profile-nav-module']/div[2]//*[contains(text(),'Reviews')]");

    //Wallethub Review popup
    public static By w_RatingBox=By.xpath("//*[contains(text(),'Your Rating')]/following-sibling::review-star/*[@class='rating-box-wrapper']");
    public static By w_ProductDropDown = By.xpath("//*[@class='dropdown-placeholder' and text()='Select...']");
    public static By w_ProductOptionHI = By.xpath("//*[@class='dropdown-item' and text()='Health Insurance']");
    public static By w_ProductSelected = By.xpath("//write-review//*[@class='dropdown-selected']");
    public static By w_ProductWriteReview= By.xpath("//textarea[@placeholder='Write your review...']");
    public static By w_ProductReviewSubmit = By.xpath("//write-review//*[text()='Submit']");
    public static By w_ReviewConfirmation = By.xpath("//h4[text()='Your review has been posted.']");

    //WalletHub Homepage after login
    public static By w_UserProfile = By.xpath("(//*[@class='brgm-list-title'])[3]");
    public static By w_clickProfile =By.xpath("//*[@class='brgm-user-info']//*[text()='Profile']");
    public static By w_ReviewFeed = By.xpath("//*[@class='pr-rec-container']//a[@href][1]");
    public static By w_YourReviewHeader = By.xpath("//*[@id='reviews-section']//*[text()=' Your Review']");



    public static WebDriver driver;
    public WebDriverWait wait;
    public  Actions action;

   public PageActions(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        action = new Actions(driver);
        driver.manage().window().maximize();
         wait = new WebDriverWait(driver,20);

    }
    public void fLogInAndPost(String uName, String pWord) throws InterruptedException {
        driver.get("https://www.facebook.com/");
        driver.findElement(userName).sendKeys(uName);
        driver.findElement(passWord).sendKeys(pWord);
        driver.findElement(login).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(statusSpan)));
        driver.findElement(statusSpan).click();
        Thread.sleep(2000);
        //driver.findElement(statusDiv).click();
        driver.findElement(statusDiv).sendKeys("Hello World");
        wait.until(ExpectedConditions.elementToBeClickable(buttonPost));
        driver.findElement(buttonPost).click();

   }
    public void wLogIn(String uName, String pWord)  {
       try {
           driver.get("http://wallethub.com/profile/test_insurance_company/");
           driver.findElement(w_Signin).click();
           wait.until(ExpectedConditions.visibilityOfElementLocated(w_UserName));
           driver.findElement(w_UserName).sendKeys(uName);
           driver.findElement(w_Password).sendKeys(pWord);
           driver.findElement(w_LoginBtn).click();
           wait.until(ExpectedConditions.visibilityOfElementLocated(w_ProfileName));
           Assert.assertEquals(driver.findElement(w_ProfileName).getText(), "Test Insurance Company", "Profile name validation after successful login");
       }catch(Exception e){
           Assert.fail("Script failed to login to the application, Method name- "+new Throwable().getStackTrace()[0].getMethodName());
       }
    }

    public void wWriteReview() {
       try{
           driver.findElement(w_ClickReview).click();
           Thread.sleep(5000);
           if(driver.findElements(w_RatingBox).size()>0){
               System.out.println("Rating filed displayed");

               action.moveToElement(driver.findElement(w_RatingBox),60,1).click().build().perform();
               System.out.println("Provided 4 star rating");
               wait.until(ExpectedConditions.visibilityOfElementLocated(w_RatingBox));
               String rString = RandomStringUtils.random(200,"The world");
               submitReview(rString);
               Thread.sleep(3000);
               if(driver.findElements(w_ProductDropDown).size()>0){
                   submitReview(rString);
               }
               wait.until(ExpectedConditions.presenceOfElementLocated(w_UserProfile));
               driver.findElement(By.cssSelector("body")).sendKeys(Keys.PAGE_UP);
               Thread.sleep(2000);
               action.moveToElement(driver.findElement(w_UserProfile)).build().perform();
               //driver.findElement(w_UserProfile).click();
               driver.findElement(w_clickProfile).click();
               wait.until(ExpectedConditions.presenceOfElementLocated(w_ReviewFeed));
               if(driver.findElement(w_ReviewFeed).getText().equalsIgnoreCase("Test Insurance Company")){
                   System.out.println("Review comment posted and viewable under profile section");
               }else Assert.fail("The review is not submitted, please verify the script");
               driver.findElement(w_ReviewFeed).click();
               wait.until(ExpectedConditions.presenceOfElementLocated(w_YourReviewHeader));
               Assert.assertTrue(driver.findElement(w_YourReviewHeader).isDisplayed(),"Your review is not posted under review section");

           }else Assert.fail("The review section is not displayed, please try some other options to review");
       }catch(Exception e){
           Assert.fail("Script failed in writing review, Method name- "+new Throwable().getStackTrace()[0].getMethodName());
       }
    }

    void submitReview(String rString){
       try{
        driver.findElement(w_ProductDropDown).click();
        driver.findElement(w_ProductOptionHI).click();
        if(driver.findElement(w_ProductSelected).getText().equalsIgnoreCase("Health Insurance"))    {
            System.out.println("Selected Health insurance as product");
        } else throw new Exception();
        // driver.findElement(w_ProductWriteReview).click();
        driver.findElement(w_ProductWriteReview).sendKeys(rString);
        driver.findElement(w_ProductReviewSubmit).click();
       }catch(Exception e){
           Assert.fail("Script failed in writing review, Method name- "+new Throwable().getStackTrace()[0].getMethodName());
       }
    }
}
