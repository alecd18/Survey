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

public abstract class Question {
	public String prompt;
	
	abstract void serialize(Document doc);
	abstract void display(abstractIO aio, String function);
	abstract void modify(String type);
	
	// Abstract question class
	public Question(String prompt) {
		this.prompt = prompt;
	}
	
}