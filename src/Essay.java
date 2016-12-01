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

public class Essay extends Question {
	
	// Constructor method for Essay
	public Essay(String prompt) {
		super(prompt);
		this.prompt = prompt;
	}
	
	// Display prompt of an essay question
	public void display(abstractIO aio, String function) {
		aio.outputMessage(this.prompt);
		System.out.println("\n");
	}
	
	// Save Essay question to xml doc
	public void serialize(Document doc) {
		Element question = doc.createElement("question");
		Element prompt = doc.createElement("prompt");
		prompt.setTextContent(this.prompt);
		question.appendChild(prompt);
		// Set question type for attribute
		question.setAttribute("type", "E");
		doc.getElementsByTagName("questions").item(0).appendChild(question);
	}
	
	public void modify(String type) {
		System.out.println("Enter the new essay prompt: ");
		Scanner s = new Scanner(System.in);
		String newEssayPrompt = s.nextLine();
		this.prompt = newEssayPrompt;
	}

}