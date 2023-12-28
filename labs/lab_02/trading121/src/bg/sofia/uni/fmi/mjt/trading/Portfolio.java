package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import java.time.LocalDateTime;
import java.util.Objects;

public class Portfolio implements PortfolioAPI {
    private String owner;
    private PriceChartAPI priceChart;
    private double budget;
    private int maxSize;
    private int size;
    public StockPurchase[] stockPurchases;
    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.size = 0;
        this.stockPurchases = new StockPurchase[maxSize];
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.size = stockPurchases.length;
        this.stockPurchases = new StockPurchase[maxSize];
        System.arraycopy(stockPurchases, 0, this.stockPurchases, 0, size);
    }
    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
       if(stockTicker==null)
           return null;
        if(quantity<0)
            return null;
        if(size==maxSize)
            return null;
        if(!Objects.equals(stockTicker, "MSFT") && !Objects.equals(stockTicker, "GOOG") && !Objects.equals(stockTicker, "AMZ"))
            return null;
        if(priceChart.getCurrentPrice(stockTicker)*quantity>budget)
            return null;
        if(size==maxSize)
            return null;
        double payment=priceChart.getCurrentPrice(stockTicker)*quantity;
        LocalDateTime currentTime=LocalDateTime.now();
        StockPurchase purchase=null;
        switch (stockTicker){
            case "MSFT" -> {
                purchase=new  MicrosoftStockPurchase(quantity, currentTime, priceChart.getCurrentPrice(stockTicker));
            }
            case "AMZ" ->{
                purchase=new AmazonStockPurchase(quantity,currentTime,priceChart.getCurrentPrice(stockTicker));
            }
            case "GOOG" ->{
                purchase=new GoogleStockPurchase(quantity,currentTime,priceChart.getCurrentPrice(stockTicker));
            }
        }
        budget -= payment;
        stockPurchases[size++] = purchase;
        priceChart.changeStockPrice(stockTicker,5);
        return purchase;
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return stockPurchases;
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        int count=0;
        for(int i=0;i<size;i++){
            if(!stockPurchases[i].getPurchaseTimestamp().isBefore(startTimestamp)
                    && !stockPurchases[i].getPurchaseTimestamp().isAfter(endTimestamp)){
                count++;
            }
        }
        StockPurchase[] result=new StockPurchase[count];
        int index=0;
        for(int i=0;i<size;i++){
            if(!stockPurchases[i].getPurchaseTimestamp().isBefore(startTimestamp)
                    && !stockPurchases[i].getPurchaseTimestamp().isAfter(endTimestamp)){
                result[index++]=stockPurchases[i];
            }
        }
        return result;
    }

    @Override
    public double getNetWorth() {
        if(size==0)
            return 0.0;
        double netWorth=0.0;
        for(int i=0;i<size;i++){
            netWorth += stockPurchases[i].getQuantity()*priceChart.getCurrentPrice(stockPurchases[i].getStockTicker());
        }
        return Math.round(netWorth * 100.0) / 100.0;
    }

    @Override
    public double getRemainingBudget() {
        return Math.round(budget * 100.0) / 100.0;
    }

    @Override
    public String getOwner() {
        return owner;
    }


}





