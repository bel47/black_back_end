package com.black.sales_service.controller;

import com.black.sales_service.exception.SalesNotFoundException;
import com.black.sales_service.model.Sales;
import com.black.sales_service.repository.SalesRepository;
import com.black.sales_service.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class SalesContoller {
    private final SalesRepository salesRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final OtpService otpService;

    public SalesContoller(SalesRepository salesRepository, OtpService otpService) {
        this.salesRepository = salesRepository;
        this.otpService = otpService;
    }

    @GetMapping("/sales")
    List<Sales> all() {
        return salesRepository.findAll();
    }

    @PostMapping("/sales")
    Sales newEmployee(@RequestBody Sales newSales) {
        return salesRepository.save(newSales);
    }

    @GetMapping("/sales/{id}")
    Sales one(@PathVariable Long id) {
        return salesRepository.findById(id)
                .orElseThrow(() -> new SalesNotFoundException("Could not find Sales " + id));
    }

    @GetMapping("/sales/pin/{PIN}")
    Sales getPin(@PathVariable String PIN) {
        Sales result = salesRepository.findByPIN(PIN);
        if (result == null) {
            return null;
        }
        return result;
    }

    @PostMapping("/sales/phone")
    Map<String, Object> byPhone(@RequestParam("phone") String phone) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        boolean sales = salesRepository.findByPhoneNumber(phone).isPresent();
        String message = "";
        boolean user_exist = true;
        RestTemplate restTemplate = new RestTemplate();
        if (sales) {
            user_exist = true;
            message = "sales exist with phone number: " + phone;
            int otp = otpService.generateOTP(phone);
            final String uri = "https://sms.vestige-e.com/sendSms.php?phone=" + phone + "&otp=" + otp + "&type=" + "sales";
            restTemplate.getForObject(uri, String.class);

            System.out.println("OPT:: " + otp);
        } else {
            user_exist = false;
            message = "sales does not exist with phone number: " + phone;
        }
        response.put("user_exist", user_exist);
        response.put("message", message);

        return response;
    }

    @PutMapping("/sales/{id}")
    Sales replaceSales(@RequestBody Sales newSales, @PathVariable Long id) {

        return salesRepository.findById(id)
                .map(sales -> {
                    sales.setfName(newSales.getfName());
                    sales.setlName(newSales.getlName());
                    sales.setPhoneNumber(newSales.getPhoneNumber());
                    sales.setPIN(newSales.getPIN());
                    return salesRepository.save(sales);
                })
                .orElseGet(() -> {
                    newSales.setId(id);
                    return salesRepository.save(newSales);
                });
    }

    @DeleteMapping("/sales/{id}")
    void deleteEmployee(@PathVariable Long id) {
        salesRepository.deleteById(id);
        return;
    }

    @PostMapping(value = "/sales/validateOtp")
    public Map<String, Object> validateOtp(@RequestParam("otpnum") int otpnum, @RequestParam("phone") String phone) {

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        final String SUCCESS = "Entered Otp is valid";
        logger.info(" Otp Number : " + otpnum + "  phone: " + phone);

        //Validate the Otp
        if (otpnum >= 0) {
            int serverOtp = otpService.getOtp(phone);
            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    otpService.clearOTP(phone);
                    response.put("isVerified", true);
                    response.put("message", SUCCESS);
                    response.put("profile", salesRepository.findByPhoneNumber(phone));
                    return response;
                } else {
                    response.put("isVerified", false);
                    response.put("message", "OTP ERROR UOTP: " + otpnum + "SOTP: " + serverOtp);
                    return response;
                }
            } else {
                response.put("isVerified", false);
                response.put("message", "FAIL OTP: " + otpnum + " Key: " + serverOtp + " Phone: " + phone);
                return response;
            }
        } else {
            response.put("isVerified", false);
            response.put("message", "FAIL" + otpnum);
            return response;
        }
    }

    @GetMapping("/generateOtp")
    public String generateOtp() {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "913886321";

        int otp = otpService.generateOTP(username);
        logger.info("OTP : " + otp);

        return "otppage";
    }
}
