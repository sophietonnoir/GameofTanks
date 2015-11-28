import java.applet.*;
import java.io.*;
import java.net.*;

public class Audio {

	public static void play(String filename) {
		//
		URL url = null;
		try {
			// charge le fichier sonore
			File file = new File(filename);
			// change le fichier en URL
			if (file.canRead())
				url = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// lance une lecture du fichier à partir de l'URL
		AudioClip clip = Applet.newAudioClip(url);
		clip.play();
	}
}
