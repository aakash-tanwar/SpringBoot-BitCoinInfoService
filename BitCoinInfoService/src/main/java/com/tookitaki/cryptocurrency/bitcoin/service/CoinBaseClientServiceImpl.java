package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.CoinBaseBitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.CoinBasePrice;
import com.tookitaki.cryptocurrency.bitcoin.domain.Price;
import com.tookitaki.cryptocurrency.bitcoin.util.DateUtility;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.CoinBaseRequest;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.response.CoinBaseBitCoinInfoResponse;

@Service
public class CoinBaseClientServiceImpl implements CoinBaseClientService {

	private final RestTemplateService restTemplate;
	private final UrlBuilder urlBuilder;

	@Autowired
	public CoinBaseClientServiceImpl(final RestTemplateService restTemplate, final UrlBuilder urlBuilder) {
		this.restTemplate = restTemplate;
		this.urlBuilder = urlBuilder;
	}

	@Override
	public BitCoinInfo get(CoinBaseRequest coinBaseRequest) {
		ResponseEntity<CoinBaseBitCoinInfoResponse> responseEntity = null;
		String url = urlBuilder.getCoinBaseApiUrl(coinBaseRequest);
		try {
			responseEntity = restTemplate.get(url, CoinBaseBitCoinInfoResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapResponse(responseEntity, coinBaseRequest.getStartDate(), coinBaseRequest.getEndDate(),
				coinBaseRequest.isFilterData());
	}

	private BitCoinInfo mapResponse(ResponseEntity<CoinBaseBitCoinInfoResponse> responseEntity, Date startDate,
			Date endDate, boolean isFilterData) {
		if (null == responseEntity) {
			throw new WebServiceException("Error Occured", null);
		}
		HttpStatus httpStatus = responseEntity.getStatusCode();
		if (!httpStatus.equals(HttpStatus.OK)) {
			throw new WebServiceException("Error Occured", null);
		}
		CoinBaseBitCoinInfoResponse coinBaseApiResponse = responseEntity.getBody();
		BitCoinInfo bitCoinInfo = getBitCoinInfo(coinBaseApiResponse, startDate, endDate, isFilterData);
		return bitCoinInfo;
	}

	private BitCoinInfo getBitCoinInfo(CoinBaseBitCoinInfoResponse coinBaseApiResponse, Date startDate, Date endDate,
			boolean isFilterData) {
		BitCoinInfo bitCoinInfo = null;
		if (Objects.nonNull(coinBaseApiResponse)) {
			CoinBaseBitCoinInfo baseBitCoinInfo = coinBaseApiResponse.getData();
			if (Objects.nonNull(baseBitCoinInfo)) {
				List<CoinBasePrice> coinBasePriceList = baseBitCoinInfo.getPrices();
				System.out.println("Size is :: "+coinBasePriceList.size());
				if (Objects.nonNull(coinBasePriceList) && !coinBasePriceList.isEmpty()) {
					List<Price> priceList = mapToPriceList(coinBasePriceList, startDate, endDate, isFilterData);
					if (!priceList.isEmpty()) {
						bitCoinInfo = new BitCoinInfo();
						bitCoinInfo.setPrices(priceList);
					}
				}
			}
		}
		return bitCoinInfo;
	}

	private List<Price> mapToPriceList(List<CoinBasePrice> coinBasePriceList, Date startDate, Date endDate,
			boolean isFilterData) {
		List<Price> prices = new ArrayList<Price>();
		
		prices = coinBasePriceList.stream()
				.map(coinBasePrice -> mapToPrice(coinBasePrice))
				.filter(coinBasePrice -> Objects.nonNull(coinBasePrice.getTime()))
				.filter(coinBasePrice -> isFilterData || coinBasePrice.getTime().after(startDate))
				.filter(coinBasePrice -> isFilterData || coinBasePrice.getTime().before(endDate))
				.collect(Collectors.toList());
		
		return prices;
	}

	private Price mapToPrice(CoinBasePrice coinBasePrice) {
		Price price = new Price();
		Date date = DateUtility.convertStringToDate(coinBasePrice.getTime(), "yyyy-MM-dd'T'HH:mm:ss'Z'");
		price.setPrice(Double.parseDouble(coinBasePrice.getPrice()));
		price.setTime(date);
		return price;
	}

}
