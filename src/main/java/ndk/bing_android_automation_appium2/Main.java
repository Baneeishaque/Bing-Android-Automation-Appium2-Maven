package ndk.bing_android_automation_appium2;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setCapability("appium:automationName", "UiAutomator2");
        desiredCapabilities.setCapability("appium:udid", "127.0.0.1:21503");
        desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, "android");
        desiredCapabilities.setCapability("appium:platformVersion", "7.1.2");
        desiredCapabilities.setCapability("appium:appPackage", "com.microsoft.bing");
        desiredCapabilities.setCapability("appium:appActivity", "com.microsoft.sapphire.app.main.SapphireHomeV3Activity");
        desiredCapabilities.setCapability("appium:noReset", "true");

        try {

            AndroidDriver remoteWebDriver = new AndroidDriver(new URI("http://127.0.0.1:4723/").toURL(), desiredCapabilities);

            remoteWebDriver.findElement(By.id("com.microsoft.bing:id/sa_hp_header_search_box")).click();
            remoteWebDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            remoteWebDriver.findElement(By.id("com.microsoft.bing:id/sapphire_search_header_input")).sendKeys("Automation of android apps");
            remoteWebDriver.pressKey(new KeyEvent(AndroidKey.ENTER));
            remoteWebDriver.quit();

        } catch (MalformedURLException | URISyntaxException e) {

            System.out.println(e.getLocalizedMessage());
        }
    }
}
