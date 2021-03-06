package pl.edu.agh.jtp.zad9;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * This will be represent the particle in a application.
 * It's a model in MVC Pattern
 * @author Zbigniew Krolikowski
 *
 */
public class Particle {	
	
	private List<AppView> views = new LinkedList<AppView>();
	
	private List<Controller> controllers = new LinkedList<Controller>();

	private List<ComponentType> components;
	private Double mass;
	private Double velocity;
	private Double kinEnergy;
	
	/** Constructor only with list containing components */ 
	public Particle (List<ComponentType> components) {
		this.components = components;
		this.mass = calculateMass();
		this.velocity = 0.0;
		this.kinEnergy = 0.0;
	}
	
	/** Constructor with a number of ELECTRONS, PROTONS, NEUTRONS */  
	public Particle (int eCount, int pCount, int nCount) {
		List<ComponentType> list = new ArrayList<ComponentType>();
		for (;eCount > 0; eCount--) {
			list.add(ComponentType.ELECTRON);
		}
		for (;pCount > 0; pCount--) {
			list.add(ComponentType.PROTON);
		}
		for (;nCount > 0; nCount--) {
			list.add(ComponentType.NEUTRON);
		}
		this.components = list;
		this.mass = calculateMass();
		this.velocity = 0.0;
		this.kinEnergy = 0.0;
	}
	
	/**
	 * Effectively changes the particle to a new one
	 * @param newComponents
	 */
	public void changeComponents(List <ComponentType> newComponents) {
		this.components = newComponents;
		this.mass = calculateMass();
		this.velocity = 0.0;
		this.kinEnergy = 0.0;
		notifyMVC();
	}
	
	/**
	 * Notifies the rest of the system that parameters have changed
	 */
	public void notifyMVC() {
		for (AppView view : views) {
			view.updateValues();
		}
	}
	
	/**
	 * Adds a view to views list
	 * @param view View which is going to be using this particle
	 */
	public void addView(AppView view) {
		(this.views).add(view);
	}
	
	/**
	 * Removes the view from the list
	 * @param view View which is no longer going to using this particle
	 */
	public void removeView(AppView view) {
		(this.views).remove(view);
	}
	
	/**
	 * Adds a controller to controller list
	 */
	public void addController(Controller controller) {
		(this.controllers).add(controller);
	}
	
	/**
	 * Removes the given controller from the list
	 */
	public void removeController(Controller controller) {
		(this.controllers).remove(controller);
	}

	/** For calculating a mass of a particle */
	@SuppressWarnings("static-access")
	private double calculateMass () {
		try {
			FileInputStream fileIn = new FileInputStream(new File("mass.properties"));
			Properties properties = new Properties();
			properties.load(fileIn);
			Double mass = 0.0;
			for (ComponentType component : this.components) {
				if (component == component.ELECTRON) {
					mass += Double.parseDouble(properties.getProperty("electronMass"));
					continue;
				}
				if (component == component.PROTON) {
					mass += Double.parseDouble(properties.getProperty("protonMass"));
					continue;
				}
				if (component == component.NEUTRON) {
					mass += Double.parseDouble(properties.getProperty("neutronMass"));
					continue;
				}
			}
			return mass;
		} catch (IOException e) {
			System.out.println("Failed to load properties file");	
			return 0;
		}		
	}
	
	/**
	 * Getter for final field components
	 * @return List of components
	 */
	public List<ComponentType> getComponents() {
		return this.components;
	}
	
	public Double getMass() {
		return this.mass;
	}
	
	public Double getVelocity() {
		return this.velocity;
	}
	
	public Double getKinEnergy() {
		return this.kinEnergy;
	}
	
	/**
	 * Counts the given components in a particle and 
	 * returns the amount
	 */
	public int getComponentCount(ComponentType desiredComponent) {
		int count = 0;
		for (ComponentType component : components) {
			if (component == desiredComponent) {
				count++;
			}
		}
		return count;
	}
	
	public void changeVelocity(Double velocity) {
		Math.abs(velocity);
		this.velocity = velocity % 1.0; 
		updateKineticEnergy();
		notifyMVC();
	}
	
	public void changeKinEnergy(Double energy) {
		Math.abs(energy);
		this.kinEnergy = energy; 
		updateVelocity();
		notifyMVC();
	}
	
	/**
	 * This is for situation in which somebody inputs 
	 * kinetic energy and wants the velocity.
	 * The mass in in MeV/c^2 and speed is c
	 */
	private void updateVelocity() {
		if (kinEnergy == 0.0) {
			this.velocity = 0.0;
			return;
		}
		this.velocity = Math.sqrt(1 - (mass*mass* 1 )/Math.pow(kinEnergy+mass * 1, 2.0));
		if (mass != 0.0) {
			this.velocity %= 1.0;
		}
	}
	
	/**
	 * This is for situation in which somebody inputs 
	 * velocity and wants kinetic energy.
	 */
	private void updateKineticEnergy() {
		if (velocity == 0.0) {
			this.kinEnergy = 0.0;
			return;
		}
		this.kinEnergy = (mass*1)/(Math.sqrt(1-Math.pow(velocity, 2))) - mass;
	}
}
