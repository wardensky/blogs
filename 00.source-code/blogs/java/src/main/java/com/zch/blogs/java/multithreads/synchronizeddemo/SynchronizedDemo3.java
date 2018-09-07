package com.zch.blogs.java.multithreads.synchronizeddemo;

import com.zch.blogs.java.base.TimeUtil;

/**
 * @Description 除了锁在this，也可以锁一个对象。
 * @author zch
 * @time 2018年9月6日 上午9:42:01
 * 
 */
public class SynchronizedDemo3 {
	public static void main(String[] args) {
		Account account = new Account("可可", 10000.0f);

		final int THREAD_NUM = 5;
		Thread threads[] = new Thread[THREAD_NUM];
		for (int i = 0; i < THREAD_NUM; i++) {
			threads[i] = new Thread(new AccountOperator(account), "Thread" + i);
			threads[i].start();
		}
	}
}

class Account {
	String name;
	float amount;

	public Account(String name, float amount) {
		this.name = name;
		this.amount = amount;
	}

	public void deposit(float amt) {
		amount += amt;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void withdraw(float amt) {
		amount -= amt;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public float getBalance() {
		return amount;
	}
}

/**
 * 账户操作类
 */
class AccountOperator implements Runnable {
	private Account account;

	public AccountOperator(Account account) {
		this.account = account;
	}

	public void run() {
		synchronized (account) {
			account.deposit(500);
			account.withdraw(500);
			System.out.println(TimeUtil.getShortTime() + Thread.currentThread().getName() + ":" + account.getBalance());
		}
	}
}