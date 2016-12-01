import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;

public class QuestionMenu extends menu {
	private String type;
	
	public QuestionMenu(ArrayList<Question> questions, String type) {
		this.type = type;
		System.out.println("How many quetions would you like to create?");
		Scanner s = new Scanner(System.in);
		int numQuestions = s.nextInt();
		
		// Create and add all choices for question menu
		choice tf = new choice(1, "Add a New T/F Question");
		choice mc = new choice(2, "Add a New Multiple Choice Question");
		choice sa = new choice(3, "Add a New Short Answer Question");
		choice essay = new choice(4, "Add a New Essay Question");
		choice rank = new choice(5, "Add a New Ranking Question");
		choice match = new choice(6, "Add a New Matching Question");
		
		this.addChoices(tf);
		this.addChoices(mc);
		this.addChoices(sa);
		this.addChoices(essay);
		this.addChoices(rank);
		this.addChoices(match);
		
		// Display all choices
		for (int i = 0; i < numQuestions; i++)
		{
			this.displayChoices();
			choice resp;
			
			resp = this.getResponse();
			
			questions.add(createQuestion(questions, resp, type));
		}
	}
	
	// Create new question depedning on user choice
	public Question createQuestion(ArrayList<Question> questions, choice resp, String type)
	{
		if (resp == null) {
			System.out.println("The option you have chosen is not valid");
			QuestionMenu qm = new QuestionMenu(questions, type);
		}
		else
		{
			switch (resp.choiceNum) {
				case 1: // new true false
					TrueFalse tf = createTF();
					return tf;
				case 2: // new multiple choice
					MC multchoice = createMC();
					return multchoice;
				case 3: // new short answer
					ShortAnswer sa = createSA();
					return sa;
				case 4: // new essay
					Essay e = createEssay();
					return e;
				case 5: // new rank
					Rank r = createRank();
					return r;
				case 6: // new matching
					Matching m = createMatch();
					return m;
			}
		}
			
		return null;
		
	}
	
	public TrueFalse createTF()
	{
		// get and store prompt
		System.out.println("Enter a True/False Question: ");
		Scanner s = new Scanner(System.in);
		String q = s.nextLine();
		String correct = "";
		if (this.type.equals("Test")) { // get correct choice if this is a test
			System.out.println("Enter the correct choice (True/False): ");
			Scanner s2 = new Scanner(System.in);
			correct = s2.nextLine();
		}
		TrueFalse tf = new TrueFalse(q, this.type, correct);
		return tf;
	}
	
	public MC createMC()
	{
		// get and store prompt
		ArrayList<String> choices = new ArrayList<String>();
		System.out.println("Enter a Multiple Choice Question: ");
		Scanner s = new Scanner(System.in);
		String q = s.nextLine();
		System.out.println("How many choices do you want?");
		int numChoices = s.nextInt();
		// add create and add all choices
		for (int i = 0; i < numChoices; i++) {
			int num = i + 1;
			System.out.println("Enter Choice #" + num);
			Scanner s2 = new Scanner(System.in);
			String choice = s2.nextLine();
			choices.add(choice);
		}
		int correct = 0;
		if (this.type.equals("Test")) { // store correct choice if this is a test
			System.out.println("Enter the correct choice (number): ");
			Scanner s2 = new Scanner(System.in);
			correct = s2.nextInt();
		}
		MC multchoice = new MC(q, choices, this.type, correct);
		return multchoice;
	}
	
	public ShortAnswer createSA()
	{
		// Create and store prompt for short answer
		System.out.println("Enter a Short Answer Question: ");
		Scanner s = new Scanner(System.in);
		String q = s.nextLine();
		String correct = "";
		if (this.type.equals("Test")) { // Create and store correct response if this is a test
			System.out.println("Enter the correct answer: ");
			Scanner s2 = new Scanner(System.in);
			correct = s2.nextLine();
		}
		ShortAnswer sa = new ShortAnswer(q, this.type, correct);
		return sa;
	}
	
	public Essay createEssay()
	{
		// Create and store prompt for essay
		System.out.println("Enter a prompt for an essay question: ");
		Scanner s = new Scanner(System.in);
		String prompt = s.nextLine();
		Essay e = new Essay(prompt);
		return e;
	}
	
	public Rank createRank()
	{
		// Create and store prompt for ranking question
		Map ranks = new HashMap<Integer, String>();
		System.out.println("Enter a prompt for a ranking question: ");
		Scanner s = new Scanner(System.in);
		String prompt = s.nextLine();
		System.out.println("How many ranking options do you want?");
		int numChoices = s.nextInt();
		// Get all choices and ranks and store in hashmap
		for (int i = 0; i < numChoices; i++) {
			System.out.println("Enter choice: ");
			Scanner s2 = new Scanner(System.in);
			String choice = s2.nextLine();
			int rank;
			if (this.type.equals("Test")) {
				System.out.println("Enter rank: ");
				rank = s2.nextInt();
			}
			else {
				rank = 0;
			}
			ranks.put(rank, choice);
		}
		
		Rank r = new Rank(prompt, ranks, this.type);
		return r;
	}
	
	public Matching createMatch()
	{
		// Create and store prompt for matching question
		Map matches = new HashMap<String, String>();
		System.out.println("Enter a prompt for a matching question: ");
		Scanner s = new Scanner(System.in);
		String prompt = s.nextLine();
		System.out.println("How many matching options do you want?");
		int numChoices = s.nextInt();
		// Create and store all choices and matches in hashmap
		for (int i = 0; i < numChoices; i++) {
			System.out.println("Enter choice: ");
			Scanner s2 = new Scanner(System.in);
			String choice = s2.nextLine();
			String letter = "";
			System.out.println("Enter letter: ");
			Scanner s3 = new Scanner(System.in);
			letter = s3.nextLine();	
			matches.put(letter, choice);
		}
		
		Matching m = new Matching(prompt, matches, this.type);
		return m;
	}
}