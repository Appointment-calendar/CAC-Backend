package com.careandcure.cac.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
public class RescheduleDTO {
    private  String newDate;
    private  String newTime;
}
