# SpringBoot-BitCoinInfoService
Following API's are exposed :

1. To fetch price trend :
  a.) Yearly 
     url : {localhost}:{8080}/bit-coin-info-ws/bitcoin/price?period=year
  b.) monthly : 
     url : {localhost}:{8080}/bit-coin-info-ws/bitcoin/price?period=month
  c.) day : 
     url : {localhost}:{8080}/bit-coin-info-ws/bitcoin/price?period=day&startDate=2018-06-01&endDate=2018-07-01
     
2. To fetch rolling avegare price trend :
    url : http://{localhost}:{8080}/bit-coin-info-ws/bitcoin/rolling-price?startDate=2018-06-01&endDate=2018-07-30&window=10
    
3. To calculate the predicted price for n days
    url : http://{localhost}:{8080}/bit-coin-info-ws/bitcoin/pridicted-price?days=10

4. To fetch the trading decision for today :
    url : http://{localhost}:{8080}/bit-coin-info-ws/bitcoin/trading-decision
    
    
    
Note : Trading decision API some times gives 204 HTTP status because of inavailability of coinbase api data. (as it computes decision for today and sometimes today data is not returned by coinbase api.)


