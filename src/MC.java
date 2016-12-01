import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MC extends Question {
	private ArrayList<choice> choices = new ArrayList<choice>();
	public int correctResponse;
	private String type;
	
	// Multiple choice constructor method
	public MC(String prompt, ArrayList<String> ch, String type, int correctResponse) {
		super(prompt);
		this.prompt = prompt;
		this.type = type;

		for (int i = 0; i < ch.size(); i++) {
			choice c = new choice(i + 1, ch.get(i));
			this.choices.add(c);
		}
		
		if (type.equals("class Test")) {
			this.correctResponse = correctResponse;
		}
	}
	
	public void display(abstractIO aio, String function) {
		// loop through each choice and display
		aio.outputMessage(this.prompt);
		for (int i = 0; i < this.choices.size(); i++) {
			aio.outputMessage(this.choices.get(i).choiceNum + ". " + this.choices.get(i).choicePrompt);
		}
		
		if (function.equals("display") && this.type.equals("class Test")) {
			aio.outputMessage("Correct Response: ");
			aio.outputMessage(Integer.toString(this.correctResponse));
		}
		System.out.println("\n");
	}
	
	public void serialize(Document doc) {
		Element question = doc.createElement("question");
		Element prompt = doc.createElement("prompt");
		prompt.setTextContent(this.prompt);
		Element choices = doc.createElement("choices");
		// loop through each choice in arraylist and create a choice xml element
		for (int i = 0; i < this.choices.size(); i++) {
			Element choice = doc.createElement("choice");
			choice.setTextContent(this.choices.get(i).choicePrompt);
			choice.setAttribute("num", Integer.toString(this.choices.get(i).choiceNum));
			choices.appendChild(choice);
		}
		question.appendChild(prompt);
		question.appendChild(choices);
		// Add a correct choice if this is a test
		if (this.type.equals("Test")) {
			Element answer = doc.createElement("answer");
			answer.setTextContent(Integer.toString(this.correctResponse));
			question.appendChild(answer);
		}
		question.setAttribute("type", "MC");
		doc.getElementsByTagName("questions").item(0).appendChild(question);
	}
	
	public void modify(String type)
	{
		// Does the user want to modify the prompt
		String answer = "";
		while (!answer.equals("yes") && !answer.equals("no")) {
			System.out.println("Do you wish to modify the prompt (Enter 'yes' or 'no')?");
			Scanner s = new Scanner(System.in);
			answer = s.nextLine();
		}
		
		// Modify prompt if answer is yes
		if (answer.equals("yes")) {
			System.out.println("Enter a new prompt: ");
			Scanner s2 = new Scanner(System.in);
			String newPrompt = s2.nextLine();
			this.prompt = newPrompt;
		}
		
		// Does the user want to modify the choices
		answer = "";
		while (!answer.equals("yes") && !answer.equals("no")) {
			System.out.println("Do you wish to modify the choices (Enter 'yes' or 'no')?");
			Scanner s = new Scanner(System.in);
			answer = s.nextLine();
		}
		
		// Choose choice to modify and then modify if answer is yes
		if (answer.equals("yes")) {
			int choicenum = 0;
			// Choose choice to modify
			while (choicenum == 0) {
				System.out.println("Which choice do you want to modify (Enter the number)?");
				for (int i = 0; i < this.choices.size(); i++) {
					System.out.println(this.choices.get(i).choiceNum + ". " + this.choices.get(i).choicePrompt);
				}
				Scanner s = new Scanner(System.in);
				try
				{
					choicenum = s.nextInt();
				}
				catch(InputMismatchException exception)
				{
				  System.out.println("This is not a number");
				}
			}
			
			// Modify the choice
			System.out.println("Enter a new value for this choice: ");
			Scanner s3 = new Scanner(System.in);
			String newChoicePrompt = s3.nextLine();
			for (int i = 0; i < this.choices.size(); i++) {
				if (this.choices.get(i).choiceNum == choicenum) {
					this.choices.get(i).choicePrompt = newChoicePrompt;
				}
			}
		}
		
		// Give user option to modify correct response if this is a test
		if (type.equals("Test")) {
			answer = "";
			// ask the user if they want to modify the correct answer
			while (!answer.equals("yes") && !answer.equals("no")) {
				System.out.println("Do you wish to modify the correct answer (Enter 'yes' or 'no')?");
				Scanner s = new Scanner(System.in);
				answer = s.nextLine();
			}
			
			// Modify the correct answer if answer is yes
			if (answer.equals("yes")) {
				System.out.println("Enter the new correct answer (Enter the number): ");
				Scanner s2 = new Scanner(System.in);
				try
				{
					int newCorrectAnswer = s2.nextInt();
					this.correctResponse = newCorrectAnswer;
				}
				catch(InputMismatchException exception)
				{
				  System.out.println("This is not a number");
				}
			}
		}
	}


}