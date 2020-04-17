package de.thro.inf.prg3.a10.kitchen;

import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch {

	private int capacity;

	private Deque<Order> orders;
	private Deque<Dish> dishes = new LinkedList<>();

	public KitchenHatchImpl(int maxDishes, Deque<Order> orders) {
		this.capacity = maxDishes;
		this.orders = orders;
	}

	@Override
	public int getMaxDishes() {
		return this.capacity;
	}

	@Override
	public Order dequeueOrder(long timeout) throws InterruptedException {
		Order order;
		synchronized (orders) {
			while (orders.isEmpty()) {
				orders.wait(timeout);
			}
			order = orders.removeFirst();
		}
		return order;
	}

	@Override
	public int getOrderCount() {
		synchronized (orders) {
			return orders.size();
		}
	}

	@Override
	public Dish dequeueDish(long timeout) throws InterruptedException {
		Dish dish;
		synchronized (dishes) {
			while (dishes.isEmpty()) {
				System.out.println("Kitchenhatch is empty, waiting for dishes...");
				dishes.wait(timeout);
			}
			dish = dishes.removeFirst();
			System.out.println("Putting " + dish.getMealName() + " out of the kitchenhatch");
			dishes.notifyAll();
			return dish;
		}
	}

	@Override
	public void enqueueDish(Dish m) {

		synchronized (dishes) {

			while (this.getDishesCount() >= this.getMaxDishes()) {
				try {
					System.out.println("Kitchenhatch is full...");
					dishes.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Putting " + m.getMealName() + " into the kitchenhatch");
			dishes.add(m);
			dishes.notifyAll();
		}
	}

	@Override
	public int getDishesCount() {
		synchronized (dishes) {
			return dishes.size();
		}
	}
}
