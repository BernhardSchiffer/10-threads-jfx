package de.thro.inf.prg3.a10.kitchen.workers;

import de.thro.inf.prg3.a10.internals.displaying.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

public class Waiter implements Runnable {

	String name;
	ProgressReporter progressReporter;
	KitchenHatch kitchenHatch;

	public Waiter(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch) {
		this.name = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch = kitchenHatch;
	}

	@Override
	public void run() {
		while (kitchenHatch.getDishesCount() > 0 || kitchenHatch.getOrderCount() > 0) {

			try {
				Dish currentDish = kitchenHatch.dequeueDish();
				System.out.println("Waiter " + this.name + " is serving " + currentDish.getMealName());
				Thread.sleep((int)(Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			progressReporter.updateProgress();

		}
		progressReporter.notifyWaiterLeaving();
	}
}
