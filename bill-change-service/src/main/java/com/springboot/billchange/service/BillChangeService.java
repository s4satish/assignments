package com.springboot.billchange.service;

import com.springboot.billchange.entity.BillChangeData;
import com.springboot.billchange.entity.BillChangeVO;
import com.springboot.billchange.exception.InsufficientCoinException;
import com.springboot.billchange.repository.CoinRepository;
import com.springboot.billchange.util.BillChangeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BillChangeService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private BillChangeHelper helper;

    public List<BillChangeData> findBillExchange(Integer bill, boolean maximizeCoins) throws InsufficientCoinException {
        // check if you have enough coin in your inventory to get a change
        double availableFund = coinRepository.getTotalCoinValue();
        log.info("available fund: " + availableFund);
        if (availableFund < Double.parseDouble(bill.toString()))
            throw new InsufficientCoinException("Insufficient coins");
        return getBillToCoins(bill.floatValue(), maximizeCoins);
    }

    private List<BillChangeData> getBillToCoins(float sum, boolean maximizeCoins) throws InsufficientCoinException {
        log.info("inside getBillToCoins");
        List<BillChangeData> result = null;
        if(maximizeCoins)
            result = helper.findMax(coinRepository.getCoinCollection().toArray(new BillChangeData[0]), sum);
        else
            result = helper.findMin(coinRepository.getCoinCollection().toArray(new BillChangeData[0]), sum);
        // reduce count in the coin inventory
        for (BillChangeData i : result) {
            coinRepository.reduceCountToCoinInventory(i);
        }

        return result;
    }

    public List<BillChangeData> getCoinInventory() {
        return coinRepository.getCoinCollection();
    }

    public void updateCoinInventory(List<BillChangeVO> coinInventory) {
        for (BillChangeVO data : coinInventory)
            coinRepository.updateCountToCoinInventory(new BillChangeData(Float.parseFloat(data.getDenomination()), data.getCount()));
    }
}
