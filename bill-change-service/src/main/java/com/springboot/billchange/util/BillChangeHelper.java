package com.springboot.billchange.util;

import com.springboot.billchange.entity.BillChangeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class BillChangeHelper {
    // using greedy algorithm
    public List<BillChangeData> findMin(BillChangeData[] coins, float sum) {
        log.info("inside findMin");
        // sort in descending order of coin's denomination
        Arrays.sort(coins, (o1, o2) -> Float.compare(o2.getDenomination(), o1.getDenomination()));
        Map<Float, Integer> answer = new HashMap<>();
        for (int i = 0; i < coins.length; i++) {
            int count = coins[i].getCount();
            float val = coins[i].getDenomination();
            // skipping coin if count is 0
            if (count == 0)
                continue;
            // consuming maximum number of coins
            while (sum >= val && count > 0) {
                sum = round(sum - val);
                count -= 1;
                answer.put(val, answer.getOrDefault(val, 0) + 1);
            }
            // breaking loop if remainder is 0
            if (sum == 0)
                break;
        }

        return getBillChangeData(answer);
    }

    // using greedy algorithm
    public List<BillChangeData> findMax(BillChangeData[] coins, float sum) {
        log.info("inside findMax");
        // sort in ascending order of coin's denomination
        Arrays.sort(coins, (o1, o2) -> Float.compare(o1.getDenomination(), o2.getDenomination()));
        Map<Float, Integer> answer = new HashMap<>();
        float previousVal = 0f;
        for (int i = 0; i < coins.length; i++) {
            int count = coins[i].getCount();
            float val = coins[i].getDenomination();
            if (count == 0)
                continue;
            // adjusting remainder sum such that higher value coins can be used (if remainder is smaller than coin's value)
            if (sum - val < 0) {
                float diff = val - sum;
                sum = round(sum + diff);
                answer.put(previousVal, answer.get(previousVal) - Math.round(diff * 100));
            }
            while (sum > 0 && count > 0) {
                sum = round(sum - val);
                count -= 1;
                answer.put(val, answer.getOrDefault(val, 0) + 1);
            }
            // maintaining previous reference
            previousVal = val;
            // breaking loop if remainder is 0
            if (sum == 0)
                break;
        }

        return getBillChangeData(answer);
    }

    private List<BillChangeData> getBillChangeData(Map<Float, Integer> answer) {
        // constructing result list
        List<BillChangeData> result = new ArrayList<>();
        Iterator<Map.Entry<Float, Integer>> itr = answer.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Float, Integer> data = itr.next();
            result.add(new BillChangeData(data.getKey(), data.getValue()));
        }
        return result;
    }


    private float round(float d) {
        float roundOff = Math.round(d * 100) / 100f;
        return roundOff;
    }

    public static void main(String[] args) {
        BillChangeData[] coins = {new BillChangeData(.25f, 100), new BillChangeData(.10f, 100), new BillChangeData(.05f, 100), new BillChangeData(.01f, 100)};
        System.out.print(new BillChangeHelper().findMax(coins, 5));
    }
}
