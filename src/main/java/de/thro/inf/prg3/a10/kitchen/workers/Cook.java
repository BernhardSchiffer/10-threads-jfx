package de.thro.inf.prg3.a10.kitchen.workers;

import de.thro.inf.prg3.a10.internals.displaying.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

public class Cook implements Runnable {

	String name;
	ProgressReporter progressReporter;
	KitchenHatch kitchenHatch;

	public Cook(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		while (kitchenHatch.getOrderCount() > 0) {
			Order currentOrder = null;
			try {
				currentOrder = kitchenHatch.dequeueOrder();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Dish currentDish = new Dish(currentOrder.getMealName());
			try {
				System.out.println("Cook " + this.name + " is preparing " + currentDish.getMealName());
				Thread.sleep(currentDish.getCookingTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			kitchenHatch.enqueueDish(currentDish);

			progressReporter.updateProgress();

		}
		progressReporter.notifyCookLeaving();
	}
}
