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

public class stringresponse extends response {
	public String value;
	
	public stringresponse(String value) {
		this.value = value;
				
	}
}
