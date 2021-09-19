package com.black.driver_service.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.black.driver_service.exception.ResourceNotFoundException;
import com.black.driver_service.model.Driver;
import com.black.driver_service.model.Trip;
import com.black.driver_service.repository.DriverRepository;
import com.black.driver_service.repository.TripRepository;
import com.black.driver_service.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:*"})
@RestController
@RequestMapping("/api/v1/")
public class DriverController {
	 
	private DriverRepository driverRepository;

    private TripRepository tripRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public final OtpService otpService;
	
	public DriverController(DriverRepository driverRepository, OtpService otpService, TripRepository tripRepository) {
        this.driverRepository = driverRepository;
        this.otpService = otpService;
        this.tripRepository = tripRepository;
    }
	
	// get all drivers
	@GetMapping("/drivers")
	public List<Driver> getAllDrivers() {
		return driverRepository.findAll();
	}

	// create driver rest API
	@PostMapping("/drivers")
	public Driver createDriver(@RequestBody Driver driver) {
		return driverRepository.save(driver);
	}
	
	// get driver by id rest API
	@GetMapping("/drivers/{id}")
	public ResponseEntity<Driver> getDriverById(@PathVariable long id) {
		Driver driver = driverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not exist with id :" + id));
		return ResponseEntity.ok(driver);
	}
	
	// update driver rest API

	@PutMapping("/drivers/{id}")
	public ResponseEntity<Driver> updateDriver(@PathVariable long id, @RequestBody Driver driverDetails) {
		Driver driver = driverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not exist with id :" + id));

		driver.setFirstName(driverDetails.getFirstName());
		driver.setLastName(driverDetails.getLastName());
		driver.setPhoneNumber(driverDetails.getPhoneNumber());
		driver.setLicenseNumber(driverDetails.getLicenseNumber());
		driver.setPlateNumber(driverDetails.getPlateNumber());
		driver.setVehicleType(driverDetails.getVehicleType());
		driver.setLibre(driverDetails.getLibre());
		driver.setServiceType(driverDetails.getServiceType());
		
		Driver updatedDriver = driverRepository.save(driver);
		return ResponseEntity.ok(updatedDriver);
	}

	// delete driver rest API
	@DeleteMapping("/drivers/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteDriver(@PathVariable long id) {
		Driver driver = driverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not exist with id :" + id));

		driverRepository.delete(driver);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/driver/phone")
    Map<String, Object> byPhone(@RequestParam("phone") String phone) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        boolean driverExist = driverRepository.findByPhoneNumber(phone).isPresent();
        String message = "";
        boolean user_exist = true;
        if (driverExist) {
            user_exist = true;
            message = "Driver exist with phone number: " + phone;
            int otp = otpService.generateOTP(phone);
            System.out.println("OPT --> " + otp);
            //SMS getaway url
            //final String uri = "http://localhost:8082/sms/sendSms.php?phone=" + phone + "&otp=" + otp + "";
            //RestTemplate restTemplate = new RestTemplate();
            //restTemplate.getForObject(uri, String.class);
        } else {
            user_exist = false;
            message = "Driver does not exist with phone number: " + phone;
        }
        response.put("user_exist", user_exist);
        response.put("message", message);

        return response;
    }

	@PostMapping(value = "/driver/validateOtp")
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
                    response.put("profile", driverRepository.findByPhoneNumber(phone));
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
    	//Trip
	@PostMapping("/drivers/trip")
	public Trip trip(@RequestBody Trip trip) {
        return tripRepository.save(trip);
	}

}
