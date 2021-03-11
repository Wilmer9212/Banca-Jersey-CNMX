/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.ResponseDTO;

import com.fenoreste.rest.entidades.Amortizaciones;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elliot
 */
public class LoanDTO {
    
private String AccountBankIdentifier;	   
private Double CurrentBalance;        
private Double CurrentRate;      	       
private int FeesDue;         	       
private List<Amortizaciones>FeesDueData;	
private int LoanStatusId;	
private Amortizaciones NextFee;	
private Double OriginalAmount;
private int OverdueDays;
private int PaidFees;
private Double PayoffBalance;   
private Double PrepaymentAmount;	        
private String ProducttBankIdentifier;
private int term;	                    
private boolean showPrincipalInformation;	
/*private LoanFeesResultDTO GetLoanFeesResult;
GetLoanRatesResult	  
GetLoanPaymentsResult	*/

    public LoanDTO() {
    }

    public LoanDTO(String AccountBankIdentifier, Double CurrentBalance, Double CurrentRate, int FeesDue, List<Amortizaciones> FeesDueData, int LoanStatusId, Amortizaciones NextFee, Double OriginalAmount, int OverdueDays, int PaidFees, Double PayoffBalance, Double PrepaymentAmount, String ProducttBankIdentifier, int term, boolean showPrincipalInformation) {
        this.AccountBankIdentifier = AccountBankIdentifier;
        this.CurrentBalance = CurrentBalance;
        this.CurrentRate = CurrentRate;
        this.FeesDue = FeesDue;
        this.FeesDueData = FeesDueData;
        this.LoanStatusId = LoanStatusId;
        this.NextFee = NextFee;
        this.OriginalAmount = OriginalAmount;
        this.OverdueDays = OverdueDays;
        this.PaidFees = PaidFees;
        this.PayoffBalance = PayoffBalance;
        this.PrepaymentAmount = PrepaymentAmount;
        this.ProducttBankIdentifier = ProducttBankIdentifier;
        this.term = term;
        this.showPrincipalInformation = showPrincipalInformation;
    }

    public String getAccountBankIdentifier() {
        return AccountBankIdentifier;
    }

    public void setAccountBankIdentifier(String AccountBankIdentifier) {
        this.AccountBankIdentifier = AccountBankIdentifier;
    }

    public Double getCurrentBalance() {
        return CurrentBalance;
    }

    public void setCurrentBalance(Double CurrentBalance) {
        this.CurrentBalance = CurrentBalance;
    }

    public Double getCurrentRate() {
        return CurrentRate;
    }

    public void setCurrentRate(Double CurrentRate) {
        this.CurrentRate = CurrentRate;
    }

    public int getFeesDue() {
        return FeesDue;
    }

    public void setFeesDue(int FeesDue) {
        this.FeesDue = FeesDue;
    }

    public List<Amortizaciones> getFeesDueData() {
        return FeesDueData;
    }

    public void setFeesDueData(List<Amortizaciones> FeesDueData) {
        this.FeesDueData = FeesDueData;
    }

    public int getLoanStatusId() {
        return LoanStatusId;
    }

    public void setLoanStatusId(int LoanStatusId) {
        this.LoanStatusId = LoanStatusId;
    }

    public Amortizaciones getNextFee() {
        return NextFee;
    }

    public void setNextFee(Amortizaciones NextFee) {
        this.NextFee = NextFee;
    }

    public Double getOriginalAmount() {
        return OriginalAmount;
    }

    public void setOriginalAmount(Double OriginalAmount) {
        this.OriginalAmount = OriginalAmount;
    }

    public int getOverdueDays() {
        return OverdueDays;
    }

    public void setOverdueDays(int OverdueDays) {
        this.OverdueDays = OverdueDays;
    }

    public int getPaidFees() {
        return PaidFees;
    }

    public void setPaidFees(int PaidFees) {
        this.PaidFees = PaidFees;
    }

    public Double getPayoffBalance() {
        return PayoffBalance;
    }

    public void setPayoffBalance(Double PayoffBalance) {
        this.PayoffBalance = PayoffBalance;
    }

    public Double getPrepaymentAmount() {
        return PrepaymentAmount;
    }

    public void setPrepaymentAmount(Double PrepaymentAmount) {
        this.PrepaymentAmount = PrepaymentAmount;
    }

    public String getProducttBankIdentifier() {
        return ProducttBankIdentifier;
    }

    public void setProducttBankIdentifier(String ProducttBankIdentifier) {
        this.ProducttBankIdentifier = ProducttBankIdentifier;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public boolean isShowPrincipalInformation() {
        return showPrincipalInformation;
    }

    public void setShowPrincipalInformation(boolean showPrincipalInformation) {
        this.showPrincipalInformation = showPrincipalInformation;
    }

    @Override
    public String toString() {
        return "LoanDTO{" + "AccountBankIdentifier=" + AccountBankIdentifier + ", CurrentBalance=" + CurrentBalance + ", CurrentRate=" + CurrentRate + ", FeesDue=" + FeesDue + ", FeesDueData=" + FeesDueData + ", LoanStatusId=" + LoanStatusId + ", NextFee=" + NextFee + ", OriginalAmount=" + OriginalAmount + ", OverdueDays=" + OverdueDays + ", PaidFees=" + PaidFees + ", PayoffBalance=" + PayoffBalance + ", PrepaymentAmount=" + PrepaymentAmount + ", ProducttBankIdentifier=" + ProducttBankIdentifier + ", term=" + term + ", showPrincipalInformation=" + showPrincipalInformation + '}';
    }


}

