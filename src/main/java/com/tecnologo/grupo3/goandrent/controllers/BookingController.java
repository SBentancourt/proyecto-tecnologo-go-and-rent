package com.tecnologo.grupo3.goandrent.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.tecnologo.grupo3.goandrent.dtos.BookingDataDTO;
import com.tecnologo.grupo3.goandrent.dtos.BookingIdDTO;
import com.tecnologo.grupo3.goandrent.dtos.BookingPaymentDTO;
import com.tecnologo.grupo3.goandrent.dtos.push.Push;
import com.tecnologo.grupo3.goandrent.dtos.responses.BookingResponse;
import com.tecnologo.grupo3.goandrent.services.BookingService;
import com.tecnologo.grupo3.goandrent.services.DeviceService;
import com.tecnologo.grupo3.goandrent.services.PaypalPaymentService;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import com.tecnologo.grupo3.goandrent.services.paypal.PaypalServiceImpl;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.PaymentStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/booking")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class BookingController {
    
    public static final String SUCCESS_URL = "booking/pay/success";
    public static final String SUCCESS_URL_WEB = "booking/pay/success-web";
    public static final String CANCEL_URL = "booking/pay/cancel";
    private static final String usernameGoAndRent = "AYnQJNi-O__v7GYcLWR4W4FVkaMY_JqKRt7ujIgs43Csz_peoJkbVOW90OJFDkla3hkAGU8woj5Zne-_";
    private static final String passwordGoAndRent = "ED_3SsNKOSl_v9ZLTHf7yh8eg6D3eCv9Kr0Ql3ugUGnzEvsv58yIU43GSCiHjZor1Eue0wrhX_6GGfRX";

    @Autowired
    private PaypalServiceImpl paypalService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaypalPaymentService paypalPaymentService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private EMailService eMailService;

    protected static final Logger logger = LogManager.getLogger(BookingController.class);

    // -- Endpoints => Huéspedes

    @PostMapping("/guest/confirm")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<?> confirmAccommodationBooking(@RequestBody BookingDataDTO bookingDTO) throws ParseException {
        Integer idBooking = bookingService.generateBooking(bookingDTO);
        Double total = bookingService.getFinalPriceBooking(bookingDTO.getIdAccommodation(), bookingDTO.getStart_date(), bookingDTO.getEnd_date());
        Payment payment = null;
        try {
            payment = paypalService.createPayment(total, "USD", "paypal",
                    "authorize", "reserva número:" + idBooking.toString(), "https://api.go-and-rent.com/prod-1/" + CANCEL_URL,
                    "https://api.go-and-rent.com/prod-1/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    System.out.println(link.getHref());
                    return new ResponseEntity<>("redirect:"+link.getHref(), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("redirect:/", HttpStatus.OK);
    }

    @PostMapping("/guest/confirm/log")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<?> confirmAccommodationBookingLog(@RequestBody BookingDataDTO bookingDTO) throws ParseException {
        String idRequest = UUID.randomUUID().toString();
        Date date = new Date();
        System.setProperty("idRequest", "1-"+idRequest);  System.setProperty("message", "Comienzo reserva de alojamiento");
        System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
        logger.error("logger");

        Integer idBooking = bookingService.generateBooking(bookingDTO);

        idRequest = UUID.randomUUID().toString();
        date = new Date();
        System.setProperty("idRequest", "2-"+idRequest);  System.setProperty("message", "Creo reserva con id: " + idBooking.toString());
        System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
        logger.error("logger");

        Double total = bookingService.getFinalPriceBooking(bookingDTO.getIdAccommodation(), bookingDTO.getStart_date(), bookingDTO.getEnd_date());
        Payment payment = null;

        idRequest = UUID.randomUUID().toString();
        date = new Date();
        System.setProperty("idRequest", "3-"+idRequest);  System.setProperty("message", "Antes del TRY del payment");
        System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
        logger.error("logger");

        try {
            
            idRequest = UUID.randomUUID().toString();
            date = new Date();
            System.setProperty("idRequest", "4-"+idRequest);  System.setProperty("message", "DENTRO DEL TRY ");
            System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
            logger.error("logger");
            
            payment = paypalService.createPayment(total, "USD", "paypal",
                    "authorize", "reserva número:" + idBooking.toString(), "https://api.go-and-rent.com/prod-1/" + CANCEL_URL,
                    "https://api.go-and-rent.com/prod-1/" + SUCCESS_URL);

            idRequest = UUID.randomUUID().toString();
            date = new Date();
            System.setProperty("idRequest", "5-"+idRequest);  System.setProperty("message", "Genero el payment: " + payment.toString());
            System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
            logger.error("logger");

            idRequest = UUID.randomUUID().toString();
            date = new Date();
            System.setProperty("idRequest", "6-"+idRequest);  System.setProperty("message", "Links del payment: " + payment.getLinks().toString());
            System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
            logger.error("logger");

            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    System.out.println(link.getHref());
                    return new ResponseEntity<>("redirect:"+link.getHref(), HttpStatus.OK);
                }
            }
            idRequest = UUID.randomUUID().toString();
            date = new Date();
            System.setProperty("idRequest", "7-"+idRequest);  System.setProperty("message", "Despues del FOR de links");
            System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
            logger.error("logger");

            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (PayPalRESTException e) {
            idRequest = UUID.randomUUID().toString();
            date = new Date();
            System.setProperty("idRequest", "8-"+idRequest);  System.setProperty("message", "CATCH: Hay error!!!! => " + "Detalles: " + e.getDetails().toString() + 
                                                                                " Mensaje: " + e.getMessage() + " Causa: " + e.getCause().toString() + " MensajeCausa: "
                                                                                + e.getCause().getMessage());
            System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
            logger.error("logger");
            e.printStackTrace();
        }
        idRequest = UUID.randomUUID().toString();
        date = new Date();
        System.setProperty("idRequest", "9-"+idRequest);  System.setProperty("message", "Finaliza el endpoint de reserva");
        System.setProperty("payload", "/booking/prueba");   System.setProperty("date", date.toString());
        logger.error("logger");

        return new ResponseEntity<>("redirect:/", HttpStatus.OK);
    }

    @PostMapping("/guest-web/confirm")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<?> confirmAccommodationBookingWEB(@RequestBody BookingDataDTO bookingDTO) throws ParseException {
        Integer idBooking = bookingService.generateBooking(bookingDTO);
        Double total = bookingService.getFinalPriceBooking(bookingDTO.getIdAccommodation(), bookingDTO.getStart_date(), bookingDTO.getEnd_date());
        Payment payment = null;
        try {
            payment = paypalService.createPayment(total, "USD", "paypal",
                    "authorize", "reserva número:" + idBooking.toString(), "https://api.go-and-rent.com/prod-1/" + CANCEL_URL,
                    "https://api.go-and-rent.com/prod-1/" + SUCCESS_URL_WEB);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    System.out.println(link.getHref());
                    return new ResponseEntity<>("redirect:"+link.getHref(), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("redirect:/", HttpStatus.OK);
    }

    // -- Endpoints ==> Anfitriones
    @PostMapping("/host/confirm")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<Object> confirmPendingBooking(@RequestBody BookingIdDTO bookingIdDTO){
        // -- Se obtiene los datos necesarios para pasarle al endpoint de confirmación.
        String authorization = bookingService.getAuthorizationIdPayment(bookingIdDTO.getBooking_id());
        Double total = Double.valueOf(bookingService.getTotalPriceBooking(bookingIdDTO.getBooking_id()));
        String currency = "USD";
        // -- Se setean los headers de la petición
        HttpHeaders headers_capture = getHeadersPaypalEndpointsV2();
        // -- Se setea el body de la petición
        HashMap<String, Object> requestBody_capture = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        map.put("value", total);
        map.put("currency_code", currency);

        Integer lastInvoiceId = paypalPaymentService.getLastInvoiceId();
        Integer nextInvoiceId = lastInvoiceId+1;
        String nextInvoiceIdString = getInvoiceIdString(nextInvoiceId);
        requestBody_capture.put("amount", map);
        requestBody_capture.put("is_final_capture", true);
        requestBody_capture.put("invoice_id", "GONRENT-"+nextInvoiceIdString);
        requestBody_capture.put("note_to_payer", "Thanks");
        requestBody_capture.put("soft_descriptor", "Booking");

        HttpEntity formEntity_capture = new HttpEntity<HashMap<String,Object>>(requestBody_capture, headers_capture);
        String url_capture_v2 = "https://api-m.sandbox.paypal.com/v2/payments/authorizations/"+authorization+"/capture";
        // -- Se ejecuta el endpoint de paypal (url_capture_v2)
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response_capture = restTemplate.exchange(url_capture_v2, HttpMethod.POST, formEntity_capture, Object.class);
        // -- Si se ejecuta correctamente y realiza la confirmación se actualizan los datos de la base de datos y se envía el mail correspondiente.
        if ((response_capture.getStatusCode() == HttpStatus.OK) || (response_capture.getStatusCode() == HttpStatus.CREATED)){
            // -- Lo siguiente es para obtener el confirmationId.
            String responseString = response_capture.getBody().toString();
            String[] split_response = responseString.split(",",3);
            String confirmationId = split_response[0].trim();
            String[] split_confirmationId = confirmationId.split("=",2);
            String confirmation_id = split_confirmationId[1].trim();

            String orderId = bookingService.getOrderId(bookingIdDTO.getBooking_id());
            paypalPaymentService.updateConfirmationIdInvoiceId(orderId, confirmation_id, nextInvoiceId);
            bookingService.updatePaymentStatus(bookingIdDTO.getBooking_id(), PaymentStatus.COMPLETADO);
            bookingService.updateBookingStatus(bookingIdDTO.getBooking_id(), BookingStatus.ACEPTADA);
            // -- Envío notificación push
            List<String> tokens = deviceService.getDevicesTokens(bookingService.getAliasGuestByBookingId(bookingIdDTO.getBooking_id()));
            String to = ""; String title = ""; String body = "";
            for (String t: tokens){
                to = t;
                title = "Reserva de Alojamiento!";
                body = "Confirmación de Reserva #"+bookingIdDTO.getBooking_id();
                Push push = new Push(to, title, body);
                ResponseEntity<Push> push2 = restTemplate.postForEntity("https://exp.host/--/api/v2/push/send", push, Push.class);
            }
            eMailService.sendEmailConfirmBooking(bookingIdDTO.getBooking_id());
            return new ResponseEntity<>(new BookingResponse(true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BookingResponse(false), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/host/reject")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<Object> rejectPendingBooking(@RequestBody BookingIdDTO bookingIdDTO){
        // -- Se obtiene el token para pasárselo al endpoint
        String token = generateTokenGoAndRent();
        // -- Se setean los headers de la petición
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        // -- se obtiene el código de autorización.
        String authorization = bookingService.getAuthorizationIdPayment(bookingIdDTO.getBooking_id());
        HashMap<String, String> requestBody = new HashMap<String, String>();
        HttpEntity formEntity = new HttpEntity<HashMap<String,String>>(requestBody, headers);

        String url_reject = "https://api-m.sandbox.paypal.com/v1/payments/authorization/"+authorization+"/void";
        // -- Se ejecuta el endpoint de paypal (url_reject)
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(url_reject, HttpMethod.POST, formEntity, Object.class);
        // -- Si se ejecuta correctamente y realiza el rechazo se actualizan los datos de la base de datos y se envía el mail correspondiente.
        if (response.getStatusCode() == HttpStatus.OK){
            bookingService.updatePaymentStatus(bookingIdDTO.getBooking_id(), PaymentStatus.RECHAZADO);
            bookingService.updateBookingStatus(bookingIdDTO.getBooking_id(), BookingStatus.RECHAZADA);
            // -- Envío notificación push
            List<String> tokens = deviceService.getDevicesTokens(bookingService.getAliasGuestByBookingId(bookingIdDTO.getBooking_id()));
            String to = ""; String title = ""; String body = "";
            for (String t: tokens){
                to = t;
                title = "Reserva de Alojamiento!";
                body = "Confirmación de Reserva #"+bookingIdDTO.getBooking_id();
                Push push = new Push(to, title, body);
                ResponseEntity<Push> push2 = restTemplate.postForEntity("https://exp.host/--/api/v2/push/send", push, Push.class);
            }
            eMailService.sendEmailRejectBooking(bookingIdDTO.getBooking_id());
            return new ResponseEntity<>(new BookingResponse(true), HttpStatus.OK);
        }
        return  new ResponseEntity<>(new BookingResponse(false), HttpStatus.BAD_REQUEST);
    }

    // -- Endpoints ==> Anfitrión y Huésped
    @PostMapping("/refund")
    @PreAuthorize("hasRole('HOST') || hasRole('GUEST')")
    public ResponseEntity<Object> refundBooking(@RequestBody BookingPaymentDTO bookingPaymentDTO){
        // -- Se obtiene los datos necesarios para pasarle al endpoint de cancelación (reembolso).
        String orderId = bookingService.getOrderId(bookingPaymentDTO.getBooking_id());
        UUID paypalRequestId = UUID.randomUUID();
        String confirmationId = paypalPaymentService.getConfirmationId(orderId);
        Double total = Double.valueOf(bookingService.getTotalPriceBooking(bookingPaymentDTO.getBooking_id()));
        // -- En el caso que el endpoint lo ejecute un anfitrión, el valor de bookingPaymentDTO.getReimbursedBy() va a ser "HOST"
        // -- En el caso que el endpoint lo ejecute un huésped, el valor de bookingPaymentDTO.getReimbursedBy() va a ser "GUEST"
        if (bookingPaymentDTO.getReimbursedBy().equals("GUEST")){
            // -- Si la cancelación la hace un huésped, hay que reembolsar la mitad.
            total = total / 2;
        }
        String currency = "USD";
        Integer invoiceId = paypalPaymentService.getInvoiceId(orderId);
        String invoiceIdString = getInvoiceIdString(invoiceId);
        // -- Se setean los headers de la petición
        HttpHeaders headers_refund = getHeadersPaypalEndpointsV2();
        // -- Se setea el body de la petición
        HashMap<String, Object> requestBody_refund = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        map.put("value", total);
        map.put("currency_code", currency);
        requestBody_refund.put("amount", map);
        requestBody_refund.put("invoice_id", "GONRENT-"+invoiceIdString);
        requestBody_refund.put("note_to_payer", "No se puede alquilar en esa fecha!");

        HttpEntity formEntity_refund = new HttpEntity<HashMap<String,Object>>(requestBody_refund, headers_refund);
        String url_refund = "https://api-m.sandbox.paypal.com/v2/payments/captures/"+ confirmationId +"/refund";
        // -- Se ejecuta el endpoint de paypal (url_refund)
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response_capture = restTemplate.exchange(url_refund, HttpMethod.POST, formEntity_refund, Object.class);

        // -- Si se ejecuta correctamente y realiza el reembolso correspondiente se actualizan los datos de la base de datos y se envía el mail correspondiente.
        if ( (response_capture.getStatusCode() == HttpStatus.OK) || (response_capture.getStatusCode() == HttpStatus.CREATED) ){
            bookingService.updatePaymentStatus(bookingPaymentDTO.getBooking_id(), PaymentStatus.REEMBOLSADO);
            bookingService.updateBookingStatus(bookingPaymentDTO.getBooking_id(), BookingStatus.CANCELADA);
            if (bookingPaymentDTO.getReimbursedBy().equals("GUEST")) {
                eMailService.sendEmailRefundBookingGuest(bookingPaymentDTO.getBooking_id());
            } else {
                // -- Envío notificación push
                List<String> tokens = deviceService.getDevicesTokens(bookingService.getAliasGuestByBookingId(bookingPaymentDTO.getBooking_id()));
                String to = ""; String title = ""; String body = "";
                for (String t: tokens){
                    to = t;
                    title = "Reserva de Alojamiento!";
                    body = "Se canceló la Reserva #"+bookingPaymentDTO.getBooking_id();
                    Push push = new Push(to, title, body);
                    ResponseEntity<Push> push2 = restTemplate.postForEntity("https://exp.host/--/api/v2/push/send", push, Push.class);
                }
                eMailService.sendEmailRefundBookingHost(bookingPaymentDTO.getBooking_id());
            }
            return new ResponseEntity<>(new BookingResponse(true), HttpStatus.OK);
        }
        return  new ResponseEntity<>(new BookingResponse(false), HttpStatus.BAD_REQUEST);
    }


    // -- ************************************************** -- //
    // -- Endpoints de reserva que se ejecutan internamente.
    // -- ************************************************** -- //
    @GetMapping(value = "/pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("token") String token,
                             @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String description = payment.getTransactions().get(0).getDescription();
            String authorization = payment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization().getId();
            String[] split = description.split(":",2);
            Integer idBooking = Integer.parseInt(split[1].trim());
            if (payment.getState().equals("approved")) {
                paypalPaymentService.savePaypalPayment(paymentId, authorization, "", idBooking, 0);
                eMailService.sendEmailNewBooking(idBooking);
                return "successPaypal";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }
    
    @GetMapping(value = "/pay/success-web")
    public String successPayWeb(@RequestParam("paymentId") String paymentId, @RequestParam("token") String token,
                             @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String description = payment.getTransactions().get(0).getDescription();
            String authorization = payment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization().getId();
            String[] split = description.split(":",2);
            Integer idBooking = Integer.parseInt(split[1].trim());
            if (payment.getState().equals("approved")) {
                paypalPaymentService.savePaypalPayment(paymentId, authorization, "", idBooking, 0);
                eMailService.sendEmailNewBooking(idBooking);
                return "redirect:https://go-and-rent.com/";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(value = "/pay/cancel")
    public String cancelPay() {
        return "cancel";
    }

    // -- ********* -- //
    // -- Funciones
    // -- ********* -- //
    private String generateTokenGoAndRent(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(usernameGoAndRent, passwordGoAndRent);
        headers.set("Accept-Language", "en_US");
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type", "client_credentials");

        HttpEntity formEntity = new HttpEntity<MultiValueMap<String,String>>(requestBody, headers);

        String uri = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, formEntity, Object.class);

        // -- Lo siguiente es para obtener el token que devuelve el response.
        String responseString = response.getBody().toString();
        String[] split_response = responseString.split(",",6);
        String access_token = split_response[1].trim();
        String[] split_access_token = access_token.split("=",2);
        return split_access_token[1].trim();
    }

    private HttpHeaders getHeadersPaypalEndpointsV2(){
        String token = generateTokenGoAndRent();
        UUID paypalRequestId = UUID.randomUUID();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("PayPal-Request-Id", paypalRequestId.toString());
        return headers;
    }

    private String getInvoiceIdString(Integer invoiceId){
        String invoiceIdString = invoiceId.toString();;
        if (invoiceId < 10){
            invoiceIdString = "000"+ invoiceIdString;
        } else if (invoiceId < 100) {
            invoiceIdString = "00"+ invoiceIdString;
        } else if (invoiceId < 1000){
            invoiceIdString = "0" + invoiceIdString;
        }
        return invoiceIdString;
    }


}
