import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class consoleIO extends abstractIO {

	@Override
	public void outputMessage(String message) {
		System.out.println(message);
	}

}
