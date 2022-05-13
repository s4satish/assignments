package com.springboot.billchange.controller;

import com.springboot.billchange.entity.BillChangeData;
import com.springboot.billchange.entity.BillChangeVO;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BillChangeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void givenNothing_GetEndPoint_ShouldReturnCoinInventory() {
        final int NUM_COIN_TYPES = 4;
        String baseUrl = "http://localhost:" + port + "/bill-change";
        ResponseEntity<BillChangeData[]> response = testRestTemplate.getForEntity(baseUrl, BillChangeData[].class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(response.getBody().length).isEqualTo(NUM_COIN_TYPES);
     }

    @Test
    void givenBill_GetEndPoint_ShouldReturnMinCoinChangeData() {
        final int INPUT_BILL = 1;
        final float EXPECTED_COIN_DENOMINATION = 0.25f;
        final int EXPECTED_COIN_COUNT = 4;
        String baseUrl = "http://localhost:" + port + "/bill-change/" + INPUT_BILL;
        ResponseEntity<BillChangeData[]> response = testRestTemplate.getForEntity(baseUrl, BillChangeData[].class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(response.getBody().length).isEqualTo(1);
        AssertionsForClassTypes.assertThat(response.getBody()[0].getDenomination()).isEqualTo(EXPECTED_COIN_DENOMINATION);
        AssertionsForClassTypes.assertThat(response.getBody()[0].getCount()).isEqualTo(EXPECTED_COIN_COUNT);
    }

    @Test
    void givenBillWithMaximizeCoinsArg_GetEndPoint_ShouldReturnMaxCoinChangeData() {
        final int INPUT_BILL = 1;
        final float EXPECTED_COIN_DENOMINATION = 0.01f;
        final int EXPECTED_COIN_COUNT = 100;
        String baseUrl = "http://localhost:" + port + "/bill-change/" + INPUT_BILL + "?maximize_coins=true";
        ResponseEntity<BillChangeData[]> response = testRestTemplate.getForEntity(baseUrl, BillChangeData[].class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(response.getBody().length).isEqualTo(1);
        AssertionsForClassTypes.assertThat(response.getBody()[0].getDenomination()).isEqualTo(EXPECTED_COIN_DENOMINATION);
        AssertionsForClassTypes.assertThat(response.getBody()[0].getCount()).isEqualTo(EXPECTED_COIN_COUNT);
    }

    @Test
    void givenInvalidBill_GetEndPoint_ShouldReturnBadRequestException() {
        final int INPUT_BILL = 100;
        String baseUrl = "http://localhost:" + port + "/bill-change/" + INPUT_BILL;
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl, String.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void givenCoinData_PostEndPoint_ShouldUpdateCoinInventory() {
        List<BillChangeVO> coinInventory = Arrays.asList(new BillChangeVO("0.25", 200), new BillChangeVO("0.01", 300));
        String baseUrl = "http://localhost:" + port + "/bill-change";
        ResponseEntity<String> response = testRestTemplate.postForEntity(baseUrl, coinInventory, String.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<BillChangeData[]> response2 = testRestTemplate.getForEntity(baseUrl, BillChangeData[].class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(Arrays.stream(response2.getBody()).filter(billChangeData -> billChangeData.getDenomination() == Float.parseFloat("0.25")).findFirst().get().getCount()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Arrays.stream(response2.getBody()).filter(billChangeData -> billChangeData.getDenomination() == Float.parseFloat("0.01")).findFirst().get().getCount()).isEqualTo(300);
    }

    @Test
    void givenInvalidCoinData_PostEndPoint_ShouldReturnException() {
        List<BillChangeVO> coinInventory = Arrays.asList(new BillChangeVO("0.5", 100));
        String baseUrl = "http://localhost:" + port + "/bill-change";
        ResponseEntity<String> response = testRestTemplate.postForEntity(baseUrl, coinInventory, String.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
