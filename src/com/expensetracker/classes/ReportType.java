package com.expensetracker.classes;


public enum ReportType {
	
	WEEKLY("Weekly"),
	MONTHLY("Monthly"),
	YEARLY("Yearly");

	public String reportType;
	
	ReportType(String reportType)
	{
		this.reportType = reportType;
	}
	@Override
	public String toString()
	{
		return reportType;
	}
	
	
	
}
