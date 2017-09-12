//package com.humanbooster.todolist;

//import org.junit.Test;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.fail;

//import java.util.concurrent.TimeUnit;

public class TaskFonctionnelleTest {

    @Test
     public void browerOpen() throws InterruptedException {
            String nameSystem = System.getProperty("os.name");
        ChromeOptions chromeOptions = new ChromeOptions();
            //comment the above 2 lines and uncomment below 2 lines to use Chrome
            if (nameSystem.contains("Windows")){
                //chromeOptions.setBinary("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
                chromeOptions.addArguments("--headless");
                //System.setProperty("webdriver.chrome.driver","Lib/chromedriver-win.exe");

            }else {
                chromeOptions.setBinary("/usr/bin/google-chrome");
                chromeOptions.addArguments("--headless");
                System.setProperty("webdriver.chrome.driver","Lib/chromedriver-linux");
            }

            WebDriver driver = new ChromeDriver(chromeOptions);
            String baseUrl = "http://46.101.232.136/hello-world";
            // launch direct it to the Base URL
            driver.get(baseUrl);
            addTask(driver);
            Thread.sleep(2000);
            removeTask(driver);
            Thread.sleep(2000);
            try{
                driver.findElement(By.id("task_0"));
                fail("Une erreur est la !!!!!");
            }catch (Exception e){

            }
            driver.close();
        }

        public void addTask( WebDriver driver ) throws InterruptedException {
            //tagName = driver.findElement(By.id("email")).getTagName();
            // Rentrée les informations dans les variables de la page
            WebElement taskTitle = driver.findElement(By.id("taskTitle"));
            taskTitle.sendKeys("Test saisie");
            WebElement dateDebut = driver.findElement(By.id("dateDebut"));
            dateDebut.sendKeys("11/09/2017");
            WebElement dateFin = driver.findElement(By.id("dateFin"));
            dateFin.sendKeys("12/08/2017");
            //driver.findElement(By.cssSelector("input[type='button']")).click();
            driver.findElement(By.cssSelector("input[type='submit']")).click();
        }

        public void removeTask(WebDriver driver ) throws InterruptedException {
            driver.findElement(By.linkText("X")).click();
        }

}
