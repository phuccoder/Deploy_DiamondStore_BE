package com.example.diamondstore.controller.DiamondPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.DiamondPrice;
import com.example.diamondstore.service.DiamondPriceService;

@RestController
@RequestMapping("/api/guest/diamond-prices")
public class DiamondPriceControllerGuest {

    @Autowired
    private final DiamondPriceService diamondPriceService;

    public DiamondPriceControllerGuest(DiamondPriceService diamondPriceService) {
        this.diamondPriceService = diamondPriceService;
    }

    @GetMapping(value = "/get-all", produces = "application/json;charset=UTF-8")
    public List<DiamondPrice> getAll_Guest() {
        return diamondPriceService.getAll();
    }

    @GetMapping(value = "/{diamondPriceID}", produces = "application/json;charset=UTF-8")
    public DiamondPrice getDiamondPriceById_Guest(@PathVariable Integer diamondPriceID) {
        return diamondPriceService.getDiamondPriceById(diamondPriceID);
    }

    @GetMapping(value = "/carat/{caratSize}", produces = "application/json;charset=UTF-8")
    public List<DiamondPrice> getDiamondPriceByCaratSize(@PathVariable BigDecimal caratSize) {
        return diamondPriceService.getDiamondPricesByCaratSize(caratSize);
    }

    @GetMapping(value = "/diamon-prices", produces = "application/json;charset=UTF-8")
    public List<DiamondPrice> getDiamondPrices(@RequestParam(required = false) String clarity,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) BigDecimal caratSize) {
        return diamondPriceService.findByCriteria(clarity, color, caratSize);
    }

    @GetMapping(value = "/clarity", produces = "application/json;charset=UTF-8")
    public List<String> getClarity() {
        return diamondPriceService.getClarity();
    }

    @GetMapping(value = "/caratsize", produces = "application/json;charset=UTF-8")
    public List<BigDecimal> getCaratSize() {
        return diamondPriceService.getCaratSize();
    }

    @GetMapping(value = "/color", produces = "application/json;charset=UTF-8")
    public List<String> getColor() {
        return diamondPriceService.getColor();
    }

    @GetMapping(value = "/prices/{caratSize}", produces = "application/json;charset=UTF-8")
    public Map<String, Map<String, BigDecimal>> getDiamondPrices(@PathVariable BigDecimal caratSize) {
        List<DiamondPrice> diamondPrices = diamondPriceService.getDiamondPricesByCaratSize(caratSize);

        // Transform the data to match the required format
        return diamondPrices.stream()
                .collect(Collectors.groupingBy(
                        DiamondPrice::getColor,
                        Collectors.groupingBy(
                                DiamondPrice::getClarity,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.get(0).getDiamondEntryPrice()
                                )
                        )
                ));
    }
}
