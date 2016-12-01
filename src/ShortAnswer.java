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

public class ShortAnswer extends Question {
	private String correctResponse;
	private String type;
	
	// Consructor method for short answer question
	public ShortAnswer(String prompt, String type, String correctResponse) {
		super(prompt);
		this.prompt = prompt;
		this.type = type;
		
		if (this.type.equals("class Test")) {
			this.correctResponse = correctResponse;
		}
	}
	
	// Display prompt for short answer question
	public void display(abstractIO aio, String function) {
		aio.outputMessage(this.prompt);
		if (function.equals("display") && this.type.equals("class Test")) {
			aio.outputMessage("Correct Response: ");
			aio.outputMessage(this.correctResponse);
		}
		System.out.println("\n");
	}

	public void serialize(Document doc) {
		// Create and save prompt to xml element
		Element question = doc.createElement("question");
		Element prompt = doc.createElement("prompt");
		prompt.setTextContent(this.prompt);
		question.appendChild(prompt);
		if (this.type.equals("Test")) { // Create and save asnwer to xml element if this is a test
			Element answer = doc.createElement("answer");
			answer.setTextContent(this.correctResponse);
			question.appendChild(answer);
		}
		// Add question to xml document
		question.setAttribute("type", "SA");
		doc.getElementsByTagName("questions").item(0).appendChild(question);
	}
	
	public void modify(String type) {
		String answer = "";
		while (!answer.equals("yes") && !answer.equals("no")) {
			System.out.println("Do you wish to modify the prompt (Enter 'yes' or 'no')?");
			Scanner s = new Scanner(System.in);
			answer = s.nextLine();
		}
		
		if (answer.equals("yes")) {
			System.out.println("Enter a new prompt: ");
			Scanner s = new Scanner(System.in);
			String newPrompt = s.nextLine();
			this.prompt = newPrompt;
		}
		
		if (type.equals("Test")) {
			answer = "";
			while (!answer.equals("yes") && !answer.equals("no")) {
				System.out.println("Do you wish to modify the correct response (Enter 'yes' or 'no')?");
				Scanner s = new Scanner(System.in);
				answer = s.nextLine();
			}
			
			if (answer.equals("yes")) {
				System.out.println("Enter a new correct response: ");
				Scanner s = new Scanner(System.in);
				String newCorrectResponse = s.nextLine();
				this.correctResponse = newCorrectResponse;
			}
		}
	}
	
}