package com.nexa.nexacheckfighter.controller;

import com.nexa.nexacheckfighter.service.CheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Check {

    private final CheckService checkService;

    public Check(final CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/check-fighter")
    public String checkFighter(@RequestParam(name = "id", required = true) int id,
                               @RequestParam(name = "verbose", defaultValue = "n") char verbose) {
        if (verbose == 'y' || verbose == 'Y') {
            return this.checkService.checkFighter(id, true);
        }
        return this.checkService.checkFighter(id, false);
    }
}
