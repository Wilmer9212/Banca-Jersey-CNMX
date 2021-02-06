/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.ws.rest.Bankingly.dto;

/**
 *
 * @author wilmer
 */
public class GetProductsDTO {
    
      private String clientBankIdentifier;
      private String productBankIdentifier;
      private String productNumber;
      private String productStatusId;
      private String productTypeId;
      private String productAlias;
      private String canTransact;
      private String currencyId;

    public GetProductsDTO() {
    }

    public GetProductsDTO(String clientBankIdentifier, String productBankIdentifier, String productNumber, String productStatusId, String productTypeId, String productAlias, String canTransact, String currencyId) {
        this.clientBankIdentifier = clientBankIdentifier;
        this.productBankIdentifier = productBankIdentifier;
        this.productNumber = productNumber;
        this.productStatusId = productStatusId;
        this.productTypeId = productTypeId;
        this.productAlias = productAlias;
        this.canTransact = canTransact;
        this.currencyId = currencyId;
    }

    public String getClientBankIdentifier() {
        return clientBankIdentifier;
    }

    public void setClientBankIdentifier(String clientBankIdentifier) {
        this.clientBankIdentifier = clientBankIdentifier;
    }

    public String getProductBankIdentifier() {
        return productBankIdentifier;
    }

    public void setProductBankIdentifier(String productBankIdentifier) {
        this.productBankIdentifier = productBankIdentifier;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(String productStatusId) {
        this.productStatusId = productStatusId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductAlias() {
        return productAlias;
    }

    public void setProductAlias(String productAlias) {
        this.productAlias = productAlias;
    }

    public String getCanTransact() {
        return canTransact;
    }

    public void setCanTransact(String canTransact) {
        this.canTransact = canTransact;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "GetProducts{" + "clientBankIdentifier=" + clientBankIdentifier + ", productBankIdentifier=" + productBankIdentifier + ", productNumber=" + productNumber + ", productStatusId=" + productStatusId + ", productTypeId=" + productTypeId + ", productAlias=" + productAlias + ", canTransact=" + canTransact + ", currencyId=" + currencyId + '}';
    }
      
      

     
}
