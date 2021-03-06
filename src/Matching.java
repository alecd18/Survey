import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Matching extends Question {
	private Map<String, String> ranks = new HashMap<String, String>();
	private String type;
	
	// Constructor method for matching question
	public Matching(String prompt, Map<String, String> r, String type) {
		super(prompt);
		this.prompt = prompt;
		this.type = type;
		
		this.ranks = r;
	}
	
	public void display(abstractIO aio, String function) {
		aio.outputMessage(this.prompt);
		// iterate through hashmap and display key and values
		Iterator it = this.ranks.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        aio.outputMessage(pair.getKey() + "  " + pair.getValue());
	    }
	    
	    if (function.equals("display") && this.type.equals("class Test")) {
	    	aio.outputMessage("Correct Response: ");
	    	Iterator it2 = this.ranks.entrySet().iterator();
		    while (it2.hasNext()) {
		        Map.Entry pair = (Map.Entry)it2.next();
		        aio.outputMessage(pair.getKey() + "  " + pair.getValue());
		    }
	    }
	    System.out.println("\n");
	}

	public void serialize(Document doc) {
		Element question = doc.createElement("question");
		Element prompt = doc.createElement("prompt");
		prompt.setTextContent(this.prompt);
		Element choices = doc.createElement("choices");
		
		// iterate through hashmap and save each to and xml document
		Iterator it = this.ranks.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Element choice = doc.createElement("choice");
	        choice.setTextContent(pair.getValue().toString());
	        Element l = doc.createElement("letter");
		    l.setTextContent(pair.getKey().toString());
		    choice.appendChild(l);	
	        choices.appendChild(choice);
	    }
	    // add question xml element to xml doc
		question.appendChild(prompt);
		question.appendChild(choices);
		question.setAttribute("type", "M");
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
		
		// Ask if user if they wish to modify a matching choice
		answer = "";
		while (!answer.equals("yes") && !answer.equals("no")) {
			System.out.println("Do you wish to modify a choice (Enter 'yes' or 'no')?");
			Scanner s = new Scanner(System.in);
			answer = s.nextLine();
		}
			
		// modify a matching choice if user answers yes
		if (answer.equals("yes")) {
			System.out.println("Enter the choice that you want to modify (Enter string value): ");
			Iterator it2 = this.ranks.entrySet().iterator();
		    while (it2.hasNext()) {
		        Map.Entry pair = (Map.Entry)it2.next();
		        System.out.println(pair.getKey());
		    }
		    Scanner s = new Scanner(System.in);
		    String toModify = s.nextLine();
		    
		    // Get new value for left side
		    System.out.println("Enter a new value for the left side: ");
		    Scanner s2 = new Scanner(System.in);
		    String newLeft = s2.nextLine();
		    
		    // Get new value for right side
		    System.out.println("Enter a new value for the right side: ");
		    Scanner s3 = new Scanner(System.in);
		    String newRight = s3.nextLine();
		    
		    // put pair into hashmap and remove old pair
		    this.ranks.put(newLeft, newRight);
		    this.ranks.remove(toModify);
		}
	}
	
}