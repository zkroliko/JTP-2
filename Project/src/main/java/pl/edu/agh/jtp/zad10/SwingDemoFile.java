package pl.edu.agh.jtp.zad10;

import pl.edu.agh.jtp.zad10.controller.AppController;
import pl.edu.agh.jtp.zad10.controller.GraphController;
import pl.edu.agh.jtp.zad10.model.Log;
import pl.edu.agh.jtp.zad10.model.Particle;
import pl.edu.agh.jtp.zad10.view.AppView;

import java.io.File;

public final class SwingDemoFile {

	/** This launches the demo
	 * @param args
	 */
	public static void main(String[] args) {	
		Particle particle = new Particle(0,0,0);
		
		Log log = new Log();
		AppView view = new AppView();
		AppController controller = new AppController(view, particle);
		controller.setLog(log);
		GraphController graphController = new GraphController(view, particle);
		graphController.setLog(log);
		
		@SuppressWarnings("unused")
		PropertiesRunner runner = new PropertiesRunner(controller, graphController, new File("config.properties"));
	}

}
