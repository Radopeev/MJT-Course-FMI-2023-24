package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public abstract class BaseStockPurchase implements StockPurchase {
    private  int quantity;
    private  LocalDateTime purchaseTimestamp;
    private  double purchasePricePerUnit;
    public BaseStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        this.quantity = quantity;
        this.purchaseTimestamp = purchaseTimestamp;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }
    @Override
    public int getQuantity(){
        return quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp(){
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit(){
        return purchasePricePerUnit;
    }

    @Override
    public double getTotalPurchasePrice(){
        double totalPurchase=quantity*purchasePricePerUnit;
        return Math.round(totalPurchase*100.0)/100.0;
    }
}
