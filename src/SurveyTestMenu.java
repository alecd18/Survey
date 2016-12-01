import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SurveyTestMenu extends menu {
	
	public SurveyTestMenu(String type, ArrayList<Question> questions, ArrayList<response> responses) {
		// Create all choices for survey or test menu
		choice create = new choice(1, "Create a " + type);
		choice display = new choice(2, "Display a " + type);
		choice load = new choice(3, "Load a " + type);
		choice save = new choice(4, "Save a " + type);
		choice modify = new choice (5, "Modify an Existing " + type);
		choice take = new choice (6, "Take a " + type);
		choice tabulate = new choice(7, "Tabulate a " + type);
		choice grade = new choice (8, "Grade a Test");
		choice quit = new choice(9, "Quit");
		
		this.addChoices(create);
		this.addChoices(display);
		this.addChoices(load);
		this.addChoices(save);
		this.addChoices(modify);
		this.addChoices(take);
		this.addChoices(tabulate);
		if (type == "Test")
			this.addChoices(grade);
		this.addChoices(quit);
		
		this.displayChoices();
		
		choice resp;
		
		resp = this.getResponse();
		
		nextStep(resp, type, questions, responses);
	}
	
	// Decide next step based on user input
	public void nextStep(choice resp, String type, ArrayList<Question> questions, ArrayList<response> responses)
	{
		if (resp == null) {
			System.out.println("The option you have chosen is not valid");
			SurveyTestMenu stm = new SurveyTestMenu(type, null, null);
		}	
		else
		{
			SurveyTestMenu tm;
			if (type.equals("Survey")) {
				Survey newSurvey = new Survey();
				
				switch (resp.choiceNum) {
					case 1: // Create new survey
						questions = newSurvey.Create();
						tm = new SurveyTestMenu("Survey", questions, null);
						break;
					case 2: // Display survey
						if (questions == null) {
							System.out.println("You must load a Survey before you can display one!");
							tm = new SurveyTestMenu("Survey", null, null);
						}
						else {
							newSurvey.Display(questions);
							tm = new SurveyTestMenu("Survey", null, null);
						}
						break;
					case 3: // Load Survey
						questions = newSurvey.Load("Survey");
						tm = new SurveyTestMenu("Survey", questions, null);
						break;
					case 4: // Save Survey
						if (questions == null) {
							System.out.println("You must create a Survey before you can save one!");
							tm = new SurveyTestMenu("Survey", null, null);
						}
						else {
							newSurvey.Save(questions, "Survey");
						}
						tm = new SurveyTestMenu("Survey", null, null);
						break;
					case 5: // Modify Survey
						questions = newSurvey.Load("Survey");
						if (questions != null) {
							newSurvey.Display(questions);
							try {
								ArrayList<Question> modified = newSurvey.Modify(questions);
								newSurvey.Save(modified, "Survey");	
							}
							catch (InputMismatchException e) {
								System.out.println("Something went wrong! Try again.");
							}
						}
						tm = new SurveyTestMenu("Survey", null, null);
						break;
					case 6: // Take Survey
						questions = newSurvey.Load("Survey");
						responses = newSurvey.Take(questions);
						newSurvey.SaveResponses(responses, newSurvey.current);
						tm = new SurveyTestMenu("Survey", questions, responses);
						break;
					case 7: // Tabulate Survey
						newSurvey.Tabulate("Survey");
						tm = new SurveyTestMenu("Survey", null, null);
				}
			}
			else {
				Test newTest = new Test();
				
				switch (resp.choiceNum) {
					case 1: // Create New Test
						questions = newTest.Create();
						tm = new SurveyTestMenu("Test", questions, null);
						break;
					case 2: // Display Test
						if (questions == null) {
							System.out.println("You must load a Test before you can display one!");
							tm = new SurveyTestMenu("Test", null, null);
						}
						else {
							newTest.Display(questions);
							tm = new SurveyTestMenu("Test", null, null);
						}
						break;
					case 3: // Load Test
						questions = newTest.Load("Test");
						tm = new SurveyTestMenu("Test", questions, null);
						break;
					case 4: // Save Test
						if (questions == null) {
							System.out.println("You must create a test before you can save one!");
							tm = new SurveyTestMenu("Test", null, null);
						}
						else {
							newTest.Save(questions, "Test");
						}
						tm = new SurveyTestMenu("Test", null, null);
						break;
					case 5: // Modify Test
						questions = newTest.Load("Test");
						if (questions != null) {
							newTest.Display(questions);
							try {
								ArrayList<Question> modified = newTest.Modify(questions);
								newTest.Save(modified, "Test");	
							}
								catch (InputMismatchException e) {
								System.out.println("Something went wrong! Try again.");
							}
						}
						tm = new SurveyTestMenu("Test", null, null);
						break;
					case 6: // Take Test
						questions = newTest.Load("Test");
						responses = newTest.Take(questions);
						newTest.SaveResponses(responses, newTest.current);
						tm = new SurveyTestMenu("Test", questions, responses);
						break;
					case 7: // Tabulate Test
						newTest.Tabulate("Test");
						tm = new SurveyTestMenu("Test", null, null);
						break;
					case 8: // Grade Test
						if (responses == null) {
							System.out.println("You must take a test before you can grade one!");
							tm = new SurveyTestMenu("Test", null, null);
						}
						else {
							newTest.Grade(responses);
							tm = new SurveyTestMenu("Test", null, null);
						}
				}
			}

		}
	}
}