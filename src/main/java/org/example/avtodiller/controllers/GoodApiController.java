package org.example.avtodiller.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.avtodiller.dtos.CreateGoodDto;
import org.example.avtodiller.dtos.EditGoodDto;
import org.example.avtodiller.models.GoodModel;
import org.example.avtodiller.services.GoodService;
import org.example.avtodiller.utils.GoodsAndCities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
class GoodApiController {
    private final GoodService goodService;
    @GetMapping("")
    ResponseEntity<GoodsAndCities> goodList(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cityOfDeparture,
            @RequestParam(required = false) String cityOfArrival,
            @RequestParam(required = false) String dateOfDeparture,
            @RequestParam(required = false) String dateOfArrival,
            HttpServletRequest request)
    {
        try{
            if(content != null && !content.isEmpty()
            || name != null && !name.isEmpty()
            || cityOfDeparture != null || cityOfArrival != null
            || dateOfDeparture != null && !dateOfDeparture.isEmpty()
            || dateOfArrival != null && !dateOfArrival.isEmpty()) {
                return ResponseEntity.ok(
                        goodService.filtered(
                                content,
                                name,
                                cityOfDeparture,
                                dateOfDeparture,
                                cityOfArrival,
                                dateOfArrival,
                                request
                        ));
            }
            return ResponseEntity.ok(goodService.list(request));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<GoodModel> carInfo(@PathVariable Long id)
    {
        try {
            return ResponseEntity.ok(goodService.getById(id));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value="/edit", consumes = {"application/x-www-form-urlencoded", "application/json"})
    ResponseEntity<String> editCar(@RequestBody EditGoodDto good) throws IOException {
        try {
            goodService.editGood(good);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("edited");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    ResponseEntity<String> createGood(@RequestBody CreateGoodDto good, HttpServletRequest request) throws IOException
    {
        try {
            goodService.saveGood(good, request);
            return ResponseEntity.status(HttpStatus.CREATED).body("created");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/delete/{id}")
    ResponseEntity<String> deleteCar(@PathVariable Long id) throws IOException {
        try {
            goodService.deleteGood(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("deleted");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
