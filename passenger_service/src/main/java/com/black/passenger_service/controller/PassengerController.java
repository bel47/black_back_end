package com.black.passenger_service.controller;

import com.black.passenger_service.exception.PassengerNotFoundException;
import com.black.passenger_service.model.Passenger;
import com.black.passenger_service.repository.PassengerRepository;
import com.black.passenger_service.service.OtpService;
import com.black.sales_service.model.Sales;
//import com.sun.istack.Nullable;
//import net.bytebuddy.implementation.bind.annotation.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:*"})
@RestController
public class PassengerController {
    private final PassengerRepository passengerRepository;
    public final OtpService otpService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PassengerController(PassengerRepository passengerRepository, OtpService otpService) {
        this.passengerRepository = passengerRepository;
        this.otpService = otpService;
    }

    @GetMapping("/passengers")
    List<Passenger> all() {
        return passengerRepository.findAll();
    }

    @PostMapping("/passenger/register")
//    Passenger newPassenger(@RequestBody Passenger newPassenger) {
    Map<String, Object> newPassenger(@RequestParam("phone") String phone, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "name") String name, @RequestParam(value = "sales_number", required = false, defaultValue = "0") String sales_number) {
        //RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        //final String uri = "http://localhost:8003/sales/pin/" + sales_number + "";
        //Sales sales = restTemplate.getForObject(uri, Sales.class);
        String sales_pin = "0"; //(sales != null) ? sales.getPIN() : "0";

        Passenger newPassenger = new Passenger();
        newPassenger.setFullName(name);
        newPassenger.setEmail(email);
        newPassenger.setPhoneNumber(phone);
        // newPassenger.setContactNumber(emergency);
        newPassenger.setSalesId(sales_pin);
        System.out.println(passengerRepository.existsByPhoneNumber(phone));
        if (passengerRepository.existsByPhoneNumber(phone)) {
            response.put("isRegisterSuccess", false);
            response.put("profile", null);
            return response;
        } else {
            response.put("isRegisterSuccess", true);
            response.put("profile", passengerRepository.save(newPassenger));
            return response;
        }

    }

    // create passenger rest API
	@PostMapping("/passengers/add")
	public Passenger createPassenger(@RequestBody Passenger passenger) {
		return passengerRepository.save(passenger);
	}

    @GetMapping("/passenger/{id}")
    Passenger one(@PathVariable Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException("Could not find Sales " + id));
    }

/*    @PostMapping("/passenger/phone")
    Map<String, Object> phone(@PathParam("phone") String phone){
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        boolean passenger = passengerRepository.existsByPhoneNumber(phone).isPresent();
        boolean exist =  passengerRepository.existsByPhoneNumber(phone).isPresent();
                ?? passengerRepository.save(passenger);
        response.put("message", "Hello" );

        return response;
    }*/

    @PostMapping("/passenger/phone")
    Map<String, Object> byPhone(@PathParam("phone") String phone) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String message = "";
        boolean user_exist = true;
        RestTemplate restTemplate = new RestTemplate();
        int otp = otpService.generateOTP(phone);
        final String uri = "http://localhost/sms/sendSms.php?phone=" + phone + "&otp=" + otp + "&type=" + "passenger";

        if (passengerRepository.existsByPhoneNumber(phone)) {
            user_exist = true;
            message = "Passenger exist for phone number: " + phone;
            System.out.println("Exist_OPT:: " + otp);
        } else {
            user_exist = false;
            message = "Passenger does not exist for phone number: " + phone;
            System.out.println("Not_Exist_OPT:: " + otp);
        }

        String result = restTemplate.getForObject(uri, String.class);
        response.put("user_exist", user_exist);
        response.put("message", message);
        return response;
    }

    @PutMapping("/passenger/update")
    Map<String, Object> replacePassenger(@RequestParam(value = "id") String id, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "contact_number", required = false, defaultValue = "0") String contact_number) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        return passengerRepository.findById(Long.parseLong(id))
                .map(passenger -> {
                    if (name != null && !name.isEmpty()) passenger.setFullName(name);
                    if (email != null) passenger.setEmail(email);
                    if (contact_number != null) passenger.setContactNumber(contact_number);
                    response.put("isUpdateSuccess", true);
                    response.put("profile", passengerRepository.save(passenger));
                    return response;
                })
                .orElseGet(() -> {
                    //newPassenger.setId(id);
                    //return passengerRepository.save(newPassenger);
                    response.put("isUpdateSuccess", false);
                    return response;
                });
    }

    @DeleteMapping("/passenger/{id}")
    void deletePassenger(@PathVariable Long id) {
        passengerRepository.deleteById(id);
        return;
    }

    @PostMapping(value = "/passenger/validateOtp")
    public Map<String, Object> validateOtp(@RequestParam("otpnum") int otpnum, @RequestParam("phone") String phone) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";
        // String phonenum = passengerRepository.findByPhoneNumber(phone).get().getPhoneNumber();;
        logger.info(" Otp Number : " + otpnum);

        //Validate the Otp
        if (otpnum >= 0) {
            int serverOtp = otpService.getOtp(phone);
            System.out.println("GOTP::" + serverOtp);
            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    otpService.clearOTP(phone);
                    response.put("isVerified", true);
                    response.put("message", SUCCESS);
                    response.put("profile", passengerRepository.findByPhoneNumber(phone));
                    return response;
                } else {
                    response.put("isVerified", false);
                    response.put("message", FAIL);
                    return response;
                }
            } else {
                response.put("isVerified", false);
                response.put("message", FAIL);
                return response;
            }
        } else {
            response.put("isVerified", false);
            response.put("message", FAIL);
            return response;
        }
    }

    @GetMapping("/generateOtp")
    public String generateOtp() {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "991430291";
        int otp = otpService.generateOTP(username);
        //logger.info("OTP : "+otp);

        return "otppage";
    }
}