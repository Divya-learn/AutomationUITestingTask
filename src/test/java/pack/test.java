package pack;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class test {

    WebDriver driver;
    String browsername;

    @BeforeTest
    @Parameters({"browser"})
    public void setUp(String browser) {
    	
    	browsername = browser;
    	if(browser.equalsIgnoreCase("firefox")) {
    		
    		driver = new FirefoxDriver();
    	
    	
    	}
    	else if(browser.equalsIgnoreCase("chrome")) {
    		
    	driver = new ChromeDriver();
    
    	}
        
       }
    
    @Test(dataProvider="resolutions")
    public void resolution(int width, int height) {
    	capturescreenshot(width,height);
    	
    }
    
   
    public void capturescreenshot(int width,int height) {
    
    	driver.get(" https://www.getcalley.com/page-sitemap.xml");
    	driver.manage().window().maximize();
    	driver.manage().window().setSize(new Dimension(width,height));
    	
    	
    	String[] Alllinks= {"https://www.getcalley.com/",
    			"https://www.getcalley.com/calley-call-from-browser/",
    			"https://www.getcalley.com/calley-pro-features/",
    			"https://www.getcalley.com/best-auto-dialer-app/",
    			"https://www.getcalley.com/how-calley-auto-dialer-app-works/"};
    	
    	for(int i=0;i<Alllinks.length;i++) {
    		
    		WebElement separatelink=driver.findElement(By.linkText(Alllinks[i]));
    		separatelink.click();
    		
    		Screenshot screenshot = new AShot()
    	    		.shootingStrategy(ShootingStrategies.viewportPasting(500))
    	    		.takeScreenshot(driver);
    	    
    			try {
    				String foldername = getDeviceName()+ "_"+ width +"_"+height+getCurrentDateTime();
    				File folder = new File(foldername);
    				folder.mkdirs();
    				ImageIO.write(screenshot.getImage(),"PNG", new File(foldername+"/screenshot.png"));
    			} catch (IOException e) {
    				System.out.println("Screenshot not taken successfully");    	
    				}
    		
    			
    		driver.navigate().back();	
    	}
    }
    public String getDeviceName() {
        Dimension size = driver.manage().window().getSize();
        int width = size.getWidth();
        int height = size.getHeight();
        
        if ((width <= 480 && height <= 800) || (width <= 800 && height <= 480)) {
            return "Mobile";
        } else {
            return "Desktop";
        }
    }
    
    public String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
  
    @DataProvider(name ="resolutions")
    public Object[][] getresolution(){
    	
    	Object[][] resolutionsdata = {{1920,1080}, {1366, 768}, {1536, 864},{360, 640},{414, 896},{375, 667}};
    	return resolutionsdata;
    }
    
    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

   
    }
