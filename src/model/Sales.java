package model;

public class Sales {

	private int month;
	private int sales;
	
	public Sales(int month, int sales) {
		this.month = month;
		this.sales = sales;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}
	
}
