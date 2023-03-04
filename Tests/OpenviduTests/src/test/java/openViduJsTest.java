import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Test with Java.
 * Test for the java deployment of open vidu 
 * @author Andrea Acuña
 */
class OpenViduJsTest extends Module{

    String evidencesFolder = "..\\..\\evidence";

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL = "http://localhost:8080/";

    String NAMESESSION = "TestSession";

    String XpathJoinButton = "//*[@id='join-dialog']/form/p[3]/input";
    String idLeaveButton = "buttonLeaveSession";
    String xpathOtherCamera = "/html/body/div/div[2]/div[3]/video[1]";

    String idHeader = "session-header";
    String idNameSession = "sessionId";
    String idSelfCamera = "local-video-undefined";


/**
 * BeforeEach.
 *
 * @author Andrea Acuña
 * Description: Execute before every single test. Configure the camera an set de url in each browser
 */
    @BeforeEach
    void setup() {
        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
        driverChrome.get(URL); 
        driverFirefox.get(URL);
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Join the session and verificate that the two browsers are inside the session
 * @throws IOException
 */
    @Test
    void T001_JoinSession() throws IOException {
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        try{
            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                super.takePhoto(evidencesFolder + "\\J_OK_C.png", "", driverChrome, driverFirefox);
            }
            if (!driverFirefox.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                super.takePhoto("", evidencesFolder + "\\J_OK_F.png", driverChrome, driverFirefox);
            }
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\J_ErrorInicializate_C.png", evidencesFolder + "\\J_ErrorInicializate_F.png", driverChrome, driverFirefox);
            fail("The app is not correctly inicializate");
        }
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Join the session, verficate that the video is playing property and leave the session
 * @throws IOException
 */
    @Test
    void T002_LeaveSession() throws IOException{

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        // see if the video is playing properly, moreover synchronize both videos
        WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
        waitC.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));
        
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
        waitF.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));

        // see if the video is playing properly
        String currentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
        String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");

        driverChrome.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
            super.takePhoto(evidencesFolder + "\\J_VideoPlaying_C.png", evidencesFolder + "\\J_VideoPlaying_F.png", driverChrome, driverFirefox);
                //Leave the session with chrome
            try{
                WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
                if (leaveButtonC.isDisplayed()){ 
                    leaveButtonC.click();
                }
                if(joinButtonC.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 1");
                    super.takePhoto(evidencesFolder + "\\J_LeaveSession_C.png", "", driverChrome, driverFirefox);
                }

                //Leave the session with Firefox

                WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
                if (leaveButtonF.isDisplayed()){ 
                    leaveButtonF.click();
                }
                if(joinButtonF.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 2");
                    super.takePhoto("", evidencesFolder + "\\J_LeaveSession_F.png", driverChrome, driverFirefox);
                }

            }catch (NoSuchElementException n){
                super.takePhoto(evidencesFolder + "\\J_Error_C.png", evidencesFolder + "\\J_Error_F.png", driverChrome, driverFirefox);
                fail("The app is not correctly working");
            }
        }else{
            super.takePhoto(evidencesFolder + "\\J_VideoNotWorking_C.png", evidencesFolder + "\\J_VideoNotWorking_F.png", driverChrome, driverFirefox);
            fail("The video is not playing properly");
        }
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Joins the session and verifies that the session name is correct
 * @throws IOException
 */
@Test
void T003_SessionHeader() throws IOException {
    // Configurate the session in chrome
    WebElement textBox = driverChrome.findElement(By.id(idNameSession));
    textBox.clear();
    textBox.sendKeys(NAMESESSION);
    WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
    joinButtonC.submit();

    try{
        if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
            if (driverChrome.findElement(By.id(idHeader)).getText() == NAMESESSION){
                System.out.println("The title is correctly set");
                super.takePhoto(evidencesFolder + "\\J_OK_C.png", "", driverChrome, driverFirefox);
            }
        }
    }catch (NoSuchElementException n){
        super.takePhoto(evidencesFolder + "\\J_ErrorInicializate_C.png", evidencesFolder + "", driverChrome, driverFirefox);
        fail("The app is not correctly inicializate");
    }
}

/**
 * AfterEach.
 *
 * @author Andrea Acuña
 * Description: close both drivers
 */
    @AfterEach
    void quit() {
        super.quitTwoBrowsers(driverChrome, driverFirefox);
    }

}
