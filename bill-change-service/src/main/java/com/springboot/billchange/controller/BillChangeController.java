package com.springboot.billchange.controller;

import com.springboot.billchange.entity.BillChangeData;
import com.springboot.billchange.entity.BillChangeVO;
import com.springboot.billchange.exception.InsufficientCoinException;
import com.springboot.billchange.service.BillChangeService;
import com.springboot.billchange.util.OneOf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/bill-change")
@Slf4j
public class BillChangeController {
    @Autowired
    private BillChangeService billChangeService;

    @GetMapping
    public List<BillChangeData> getCoinInventory() {
        log.debug("Inside getCoinInventory");
        return billChangeService.getCoinInventory();
    }

    @PostMapping
    public void updateCoinInventory(@Valid @RequestBody List<BillChangeVO> coinInventory) {
        log.debug("Inside updateCoinInventory");
        billChangeService.updateCoinInventory(coinInventory);
    }

    @GetMapping("/{bill}")
    public List<BillChangeData> findBillChange(@PathVariable
                                               @OneOf(value = {"1", "5", "10", "20", "50", "100"}, message = "Valid bill value is 1, 5, 10, 20, 50 or 100") String bill,
                                               @RequestParam(value = "maximize_coins", required = false) @Valid boolean maximizeCoins)
            throws InsufficientCoinException {
        log.debug("Inside findBillExchange");
        List<BillChangeData> result = billChangeService.findBillExchange(Integer.valueOf(bill), maximizeCoins);
        return result;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Bad Request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientCoinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleInsufficientCoinException(InsufficientCoinException e) {
        return new ResponseEntity<>("Insufficient Resource: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
