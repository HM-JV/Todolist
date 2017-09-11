package com.humanbooster.todolist;
//import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TaskFonctionnelle {
     public static void main(String[] args) {
            //comment the above 2 lines and uncomment below 2 lines to use Chrome
            System.setProperty("webdriver.chrome.driver","Lib/chromedriver-win.exe");
            WebDriver driver = new ChromeDriver();
            String baseUrl = "http://46.101.232.136/hello-world";
            // launch direct it to the Base URL
            driver.get(baseUrl);
            test(driver);
            driver.close();
        }

        public static void test( WebDriver driver ){
            //tagName = driver.findElement(By.id("email")).getTagName();
            // Rentrée les informations dans les variables de la page
            WebElement TaskName = driver.findElement(By.id("taskTitle"));
            TaskName.sendKeys("Test saisie");
        }
}
