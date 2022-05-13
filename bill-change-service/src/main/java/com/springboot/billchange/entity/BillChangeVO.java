package com.springboot.billchange.entity;

import com.springboot.billchange.util.OneOf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillChangeVO {
    @NotBlank(message = "denomination cannot be null or empty")
    @OneOf(value = {"0.01", "0.05", "0.10", "0.25"}, message = "Allowed values for denomination are 0.01, 0.05, 0.10, 0.25")
    private String denomination;

    @Min(value = 1, message = "count cannot be less than 1")
    private int count;
}
