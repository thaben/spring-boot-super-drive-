package com.udacity.jwdnd.course1.cloudstorage;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	private static final String FIRST_NAME = "JOHN";
	private static final String LAST_NAME = "DOE";
	private static final String USER_NAME = "JOHN_DOE";
	private static final String PASSWORD = "password";
	private static final String NOTE_TITLE = "MARKET LIST";
	private static final String NOTE_DESCRIPTION = "PRODUCTS I NEED TO BUY FROM THE SUPER MARKET";
	private static final String URL = "lipsum.com";



	private String createURL(String path){
		final String LOCALHOST = "http://localhost:"+port;
		return LOCALHOST+path;
	}

	@Test
	@Order(1)
	void getLoginPageTest() {
		driver.get("http://localhost:"+port+"/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	void getSignupPageTest() {
		driver.get("http://localhost:"+port+"/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(3)
	void getUnauthorizedHomePageTest() {
		driver.get("http://localhost:"+port+"/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(4)
	void getUnauthorizedResultPageTest() {
		driver.get("http://localhost:"+port+"/result");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(5)
	void registerUserTest() {
		driver.get("http://localhost:"+port+"/signup");
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.sendKeys(FIRST_NAME);
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.sendKeys(LAST_NAME);
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement signUpButton = driver.findElement(By.id("signupbutton"));
		signUpButton.click();

		driver.get("http://localhost:"+port+"/login");
		inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.findElement(By.id("logoutbutton")).submit();
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(6)
	void createNoteTest() {
		WebDriverWait wait = new WebDriverWait (driver, 15);
		JavascriptExecutor javascriptExecutor =(JavascriptExecutor) driver;
		driver.get("http://localhost:"+port+"/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		javascriptExecutor.executeScript("arguments[0].click()", notesTab);
		wait.withTimeout(Duration.ofSeconds(15));
		WebElement newNote = driver.findElement(By.id("newnotebutton"));
		wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
		WebElement notedescription = driver.findElement(By.id("note-description-modal"));
		WebElement notetitle = driver.findElement(By.id("note-title-modal"));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		notetitle.sendKeys(NOTE_TITLE);
		notedescription.sendKeys(NOTE_DESCRIPTION);
		WebElement noteSubmit = driver.findElement(By.id("noteSubmit2"));
		noteSubmit.click();

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		Assertions.assertEquals("Result", driver.getTitle());

		driver.get("http://localhost:"+port+"/home");
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		javascriptExecutor.executeScript("arguments[0].click()", notesTab);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
		Boolean noteCreated = false;
		for (int i=0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals(NOTE_TITLE)) {
				noteCreated = true;
				break;
			}
		}
		Assertions.assertTrue(noteCreated);
	}

	@Test
	@Order(7)
	public void updateNoteTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor javascriptExecutor =(JavascriptExecutor) driver;
		String noteTitle = "Silly Note Title";
		driver.get("http://localhost:"+port+"/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		javascriptExecutor.executeScript("arguments[0].click()", notesTab);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement editElement = null;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			editElement = element.findElement(By.id("editnote"));
			if (editElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
		WebElement updatedNoteTitle = driver.findElement(By.id("note-title-modal"));
		wait.until(ExpectedConditions.elementToBeClickable(updatedNoteTitle));
		updatedNoteTitle.clear();
		updatedNoteTitle.sendKeys(noteTitle);
		WebElement savechanges = driver.findElement(By.id("noteSubmit2"));
		savechanges.click();
		Assertions.assertEquals("Result", driver.getTitle());
		driver.get("http://localhost:"+port+"/home");
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		javascriptExecutor.executeScript("arguments[0].click()", notesTab);
		notesTable = driver.findElement(By.id("userTable"));
		notesList = notesTable.findElements(By.tagName("th"));
		Boolean edited = false;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals(noteTitle)) {
				edited = true;
				break;
			}
		}
		Assertions.assertTrue(edited);
	}

	@Test
	@Order(8)
	public void deleteNoteTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		//login
		driver.get("http://localhost:"+port+"/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			deleteElement = element.findElement(By.id("deletenote"));
			if (deleteElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	@Test
	@Order(9)
	public void createCredentialTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor javascriptExecutor =(JavascriptExecutor) driver;
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
		javascriptExecutor.executeScript("arguments[0].click()", credTab);
		wait.withTimeout(Duration.ofSeconds(15));
		WebElement newCred = driver.findElement(By.id("newcredential"));
		wait.until(ExpectedConditions.elementToBeClickable(newCred)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(URL);
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		credentialUsername.sendKeys(USER_NAME);
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys(PASSWORD);
		WebElement submit = driver.findElement(By.id("savecredential"));
		submit.click();

		Assertions.assertEquals("Result", driver.getTitle());


		driver.get("http://localhost:" + this.port + "/home");
		credTab = driver.findElement(By.id("nav-credentials-tab"));
		javascriptExecutor.executeScript("arguments[0].click()", credTab);
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialTable.findElements(By.tagName("td"));
		Boolean created = false;
		for (int i=0; i < credentialsList.size(); i++) {
			WebElement element = credentialsList.get(i);
			if (element.getAttribute("innerHTML").equals(USER_NAME)) {
				created = true;
				break;
			}
		}
		Assertions.assertTrue(created);
	}

	@Test
	@Order(11)
	public void updateCredentialTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		String newCredentialUsername = "User 12";
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialTable.findElements(By.tagName("td"));
		WebElement editCredentialElement = null;
		for (int i = 0; i < credentialsList.size(); i++) {
			WebElement element = credentialsList.get(i);
			editCredentialElement = element.findElement(By.id("editcredential"));
			if (editCredentialElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(editCredentialElement)).click();
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		wait.until(ExpectedConditions.elementToBeClickable(credentialUsername));
		credentialUsername.clear();
		credentialUsername.sendKeys(newCredentialUsername);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement savechanges = driver.findElement(By.id("savecredential"));
		savechanges.click();
		Assertions.assertEquals("Result", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		credTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credTab);
		credentialTable = driver.findElement(By.id("credentialTable"));
		credentialsList = credentialTable.findElements(By.tagName("td"));
		Boolean edited = false;
		for (int i = 0; i < credentialsList.size(); i++) {
			WebElement element = credentialsList.get(i);
			if (element.getAttribute("innerHTML").equals(newCredentialUsername)) {
				edited = true;
				break;
			}
		}
		Assertions.assertTrue(edited);
	}

	@Test
	@Order(12)
	public void deleteCredentialTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor javascriptExecutor =(JavascriptExecutor) driver;
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(USER_NAME);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(PASSWORD);
		WebElement loginButton = driver.findElement(By.id("loginbutton"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
		javascriptExecutor.executeScript("arguments[0].click()", credTab);
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialList = credentialTable.findElements(By.tagName("td"));
		WebElement deleteCredentialElement = null;
		for (int i = 0; i < credentialList.size(); i++) {
			WebElement element = credentialList.get(i);
			deleteCredentialElement = element.findElement(By.id("deletecredential"));
			if (deleteCredentialElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteCredentialElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());
	}
}
