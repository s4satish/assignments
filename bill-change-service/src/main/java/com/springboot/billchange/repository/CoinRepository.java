package com.springboot.billchange.repository;

import com.springboot.billchange.entity.BillChangeData;
import com.springboot.billchange.exception.InsufficientCoinException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CoinRepository {
    private List<BillChangeData> coinCollection = null;

    public CoinRepository() {
        coinCollection = Arrays.asList(new BillChangeData(.25f, 100), new BillChangeData(.10f, 100), new BillChangeData(.05f, 100), new BillChangeData(.01f, 100));
    }

    public List<Float> getCoins() {
        return coinCollection.stream().map(billChangeData -> billChangeData.getDenomination()).collect(Collectors.toList());
    }

    public List<BillChangeData> getCoinCollection() {
        return coinCollection;
    }

    public int getCountByCoin(float coin) {
        Optional<BillChangeData> data = coinCollection.stream().filter(billChangeData -> billChangeData.getDenomination() == coin).findFirst();
        return data.isPresent() ? data.get().getCount() : 0;
    }

    public double getTotalCoinValue() {
        double sum = 0.0;
        for (BillChangeData data : coinCollection)
            sum += data.getDenomination() * data.getCount();
        return sum;
    }

    public void updateCountToCoinInventory(BillChangeData input) {
        if (input.getCount() < 0) throw new IndexOutOfBoundsException("coin count cannot be less than 0");
        else {
            for (BillChangeData data : coinCollection) {
                if (data.getDenomination() == input.getDenomination())
                    data.setCount(input.getCount());
            }
        }
    }

    public void reduceCountToCoinInventory(BillChangeData input) throws InsufficientCoinException {
        for (BillChangeData data : coinCollection) {
            if (data.getDenomination() == input.getDenomination()) {
                if (data.getCount() >= input.getCount())
                    data.setCount(data.getCount() - input.getCount());
                else
                    throw new InsufficientCoinException("Coin " + input.getDenomination() + " has insufficient count.");

            }
        }
    }
}
