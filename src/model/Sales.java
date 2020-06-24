package model;

import java.time.*;

public class Sales {

	private LocalDate sDate;
	private int todaySales;
	private int monthSales;
	
	public Sales(LocalDate sDate, int todaySales, int monthSales) {
		this.sDate = sDate;
		this.todaySales = todaySales;
		this.monthSales = monthSales;
	}
	
	
	public LocalDate getsDate() {
		return sDate;
	}
	public void setsDate(LocalDate sDate) {
		this.sDate = sDate;
	}
	public int getTodaySales() {
		return todaySales;
	}
	public void setTodaySales(int todaySales) {
		this.todaySales = todaySales;
	}
	public int getMonthSales() {
		return monthSales;
	}
	public void setMonthSales(int monthSales) {
		this.monthSales = monthSales;
	}
	
	
}
