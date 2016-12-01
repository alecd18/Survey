import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class choice {
	int choiceNum;
	String choicePrompt;
	
	// Constructor method for choice
	public choice(int num, String prompt) {
		this.choiceNum = num;
		this.choicePrompt = prompt;
	}
}