package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.AccommodationFilterDTO;
import com.tecnologo.grupo3.goandrent.dtos.push.Push;
import com.tecnologo.grupo3.goandrent.services.AccommodationService;
import com.tecnologo.grupo3.goandrent.services.DeviceService;
import com.tecnologo.grupo3.goandrent.services.GeneralFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class DataController {

    @Autowired
    private GeneralFeatureService generalFeatureService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/features")
    public ResponseEntity<?> getGeneralFeatures(){
        return new ResponseEntity<>(generalFeatureService.getGeneralFeaturesList(), HttpStatus.OK);
    }

    @GetMapping("/accommodation/info/{id}")
    public ResponseEntity<?> getAccommodationInfo(@PathVariable(value = "id") int id,
                                                  @RequestParam(value = "alias", required = false) String alias){
        return new ResponseEntity<>(accommodationService.getAccommodationDetails(id, alias), HttpStatus.OK);
    }

    @GetMapping("/accommodation/search")
    public ResponseEntity<?> getAccommodationsByFilter(@RequestParam(value = "services", required = false) List<String> services,
                                                            @RequestParam(value = "features", required = false) List<String> features,
                                                            @RequestParam String country, @RequestParam String province, @RequestParam String city,
                                                            @RequestParam(value = "dateFrom", required = false) String dateFrom,
                                                            @RequestParam(value = "dateTo", required = false) String dateTo,
                                                            @RequestParam(value = "priceFrom", required = false) String priceFrom,
                                                            @RequestParam(value = "priceTo", required = false) String priceTo,
                                                            @RequestParam(value = "nroPag", defaultValue = "0", required = false) int nroPag,
                                                            @RequestParam(value = "cantReg", defaultValue = "10", required = false) int cantReg) throws ParseException {


        return new ResponseEntity<>(accommodationService.getAccommodationsBySearch(services, features, country, province, city, dateFrom, dateTo, priceFrom, priceTo, nroPag, cantReg), HttpStatus.OK);
    }

    /*@GetMapping("/push")
    public ResponseEntity<?> sendPushTest(){
        // -- Envío notificación push
        RestTemplate restTemplate = new RestTemplate();
        List<String> tokens = deviceService.getDevicesTokens("ezEnrique22");
        String to = ""; String title = ""; String body = "";
        ResponseEntity<Push> push2 = null;
        for (String t: tokens){
            System.out.println("Token");
            System.out.println(t);
            to = t;
            title = "Reserva de Alojamiento!";
            body = "Confirmación de Reserva #"+12345678;
            Push push = new Push(to, title, body);
            push2 = restTemplate.postForEntity("https://exp.host/--/api/v2/push/send", push, Push.class);
        }
        return new ResponseEntity<>(push2, HttpStatus.OK);
    }*/
}
