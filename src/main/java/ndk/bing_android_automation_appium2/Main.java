package ndk.bing_android_automation_appium2;

import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import com.jcabi.http.response.JsonResponse;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

public class Main {

    private static boolean isNotFirstSearch = false;
    private static AndroidDriver remoteWebDriver;

    public static void main(String[] args) {

        getGitHubRepositories();
    }

    private static void getGitHubRepositories() {

        final Github github = new RtGithub();
        final JsonResponse resp;
        try {
            resp = github.entry()
                    .uri().path("/search/repositories")
                    .queryParam("q", "java").back()
                    .fetch()
                    .as(JsonResponse.class);

            try (JsonReader jsonReader = resp.json()) {

                final List<JsonObject> items = jsonReader.readObject()
                        .getJsonArray("items")
                        .getValuesAs(JsonObject.class);
                for (final JsonObject item : items) {

                    automateBingAndroidApp(item.get("full_name").toString().replaceAll("\"", ""));
                }
                remoteWebDriver.quit();
            }
        } catch (IOException e) {

            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void automateBingAndroidApp(String searchTerm) {

        if (isNotFirstSearch) {

            performSearchThenWait(searchTerm, remoteWebDriver);

        } else {

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

            desiredCapabilities.setCapability("appium:automationName", "UiAutomator2");
            desiredCapabilities.setCapability("appium:udid", "127.0.0.1:21503");
            desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, "android");
            desiredCapabilities.setCapability("appium:platformVersion", "7.1.2");
            desiredCapabilities.setCapability("appium:appPackage", "com.microsoft.bing");
            desiredCapabilities.setCapability("appium:appActivity", "com.microsoft.sapphire.app.main.SapphireHomeV3Activity");
            desiredCapabilities.setCapability("appium:noReset", "true");

            try {

                remoteWebDriver = new AndroidDriver(new URI("http://127.0.0.1:4723/").toURL(), desiredCapabilities);
                remoteWebDriver.findElement(By.id("com.microsoft.bing:id/sa_hp_header_search_box")).click();
                remoteWebDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                performSearchThenWait(searchTerm, remoteWebDriver);

            } catch (MalformedURLException | URISyntaxException e) {

                System.out.println(e.getLocalizedMessage());
            }
            isNotFirstSearch = true;
        }
    }

    private static void performSearchThenWait(String searchTerm, AndroidDriver remoteWebDriver) {

        if (isNotFirstSearch) {

            remoteWebDriver.findElement(By.id("com.microsoft.bing:id/iab_address_bar_text_view")).click();
        }
        remoteWebDriver.findElement(By.id("com.microsoft.bing:id/sapphire_search_header_input")).sendKeys(searchTerm);
        remoteWebDriver.pressKey(new KeyEvent(AndroidKey.ENTER));
        remoteWebDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(120));
    }
}
