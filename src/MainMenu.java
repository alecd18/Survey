import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Date;

public class MainMenu extends menu {
	
	public MainMenu() {
		// Create test and survey choice and display
		choice survey = new choice(1, "Survey");
		choice test = new choice(2, "Test");
		this.addChoices(survey);
		this.addChoices(test);
		this.displayChoices();
		
		choice resp;
		
		resp = this.getResponse();
		
		// get user response and decide next choice
		nextStep(resp);
	}
	
	public void nextStep(choice resp)
	{
		// Decide next step depending on user choice
		if (resp == null) {
			System.out.println("The option you have chosen is not valid");
			MainMenu mm = new MainMenu();
		}
		else
		{
			switch (resp.choiceNum) {
			case 1: // create new Survey menu
				SurveyTestMenu sm = new SurveyTestMenu("Survey", null, null);
				break;
			case 2: // create new Test menu
				SurveyTestMenu tm = new SurveyTestMenu("Test", null, null);
				break;
				
			}	
		}
	}
}