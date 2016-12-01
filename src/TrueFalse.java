import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TrueFalse extends Question {
	private ArrayList<choice> choices = new ArrayList<choice>();
	private String correctResponse;
	private String type;
	
	// Constructor method for true false question
	public TrueFalse(String prompt, String type, String correctResponse) {
		super(prompt);
		this.prompt = prompt;
		this.type = type;
		
		choice t = new choice(1, "True");
		choice f = new choice(2, "False");
		
		this.choices.add(t);
		this.choices.add(f);
		
		if (type.equals("class Test")) {
			this.correctResponse = correctResponse;
		}
	}
	
	// Display true false prompt and true false choices
	public void display(abstractIO aio, String function) {
		aio.outputMessage(this.prompt);
		aio.outputMessage("A. " + this.choices.get(0).choicePrompt);
		aio.outputMessage("B. " + this.choices.get(1).choicePrompt);
		
		if (function.equals("display") && this.type.equals("class Test")) {
			aio.outputMessage("Correct Response: ");
			aio.outputMessage(this.correctResponse);
		}
		System.out.println("\n");
	}
	
	public void serialize(Document doc) {
		Element question = doc.createElement("question");
		Element prompt = doc.createElement("prompt");
		prompt.setTextContent(this.prompt);
		question.appendChild(prompt);
		if (this.type.equals("Test")) { // Save correct choice if this is a test
			Element answer = doc.createElement("answer");
			answer.setTextContent(this.correctResponse);
			question.appendChild(answer);
		}
		// Create and save xml element to xml document
		question.setAttribute("type", "TF");
		doc.getElementsByTagName("questions").item(0).appendChild(question);
	}
	
	public void modify(String type)
	{
		// Modify answer, prompt, or both?
		String toModify = "";
		if (type.equals("Test")) {
			System.out.println("Do you want to modify the prompt or the answer (enter 'prompt' or 'answer' or 'both' for both)?");
			Scanner s = new Scanner(System.in);
			toModify = s.nextLine();
		}
		else
			toModify = "prompt";
		
		// Get the new prompt or answer or both
		String prompt = "";
		String answer = "";
		if (toModify.equals("both")) {
			System.out.println("Enter the new prompt: ");
			Scanner s = new Scanner(System.in);
			prompt = s.nextLine();
			System.out.println("Enter the new answer: ");
			Scanner s2 = new Scanner(System.in);
			answer = s2.nextLine();
		}
		else if (toModify.equals("answer")) {
			System.out.println("Enter the new answer: ");
			Scanner s2 = new Scanner(System.in);
			answer = s2.nextLine();
		}
		else {
			System.out.println("Enter the new prompt: ");
			Scanner s = new Scanner(System.in);
			prompt = s.nextLine();
		}
		
		// store modifications in true false question
		if (toModify.equals("both")) {
			this.prompt = prompt;
			this.correctResponse = answer;
		}
		else if (toModify.equals("answer")) {
			this.correctResponse = answer;
		}
		else {
			this.prompt = prompt;
		}
	}

}