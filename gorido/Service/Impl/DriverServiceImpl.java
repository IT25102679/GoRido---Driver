package com.example.gorido.Service.Impl;

import com.example.gorido.DTO.DriverRegisterRequest;
import com.example.gorido.Model.*;
import com.example.gorido.Repository.*;
import com.example.gorido.Service.DriverService;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final DriverRepository driverRepository;
    private final TypeRepository typeRepository;
    private final DistrictRepository districtRepository;
    private final HireRepository hireRepository;

    public DriverServiceImpl(UserRepository userRepository, StatusRepository statusRepository, DriverRepository driverRepository,
                             TypeRepository typeRepository, DistrictRepository districtRepository,
                             HireRepository hireRepository){
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.statusRepository = statusRepository;
        this.typeRepository = typeRepository;
        this.districtRepository = districtRepository;
        this.hireRepository = hireRepository;
    }

    public String loadUser(HttpSession session){
        String email = (String) session.getAttribute("userEmail");

        if (email == null){
            return "not logged";
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null){
            return "User not found";
        }

        return user.getEmail() + "|" + user.getMobile_number();
    }

    public String getAllDistricts() {

        List<District> districts = districtRepository.findAll();

        StringBuilder gen = new StringBuilder();

        for (District district : districts) {
            gen.append(district.getId())
                    .append(":")
                    .append(district.getName())
                    .append(",");
        }

        return gen.toString();
    }

    public String registerDriver(DriverRegisterRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null){
            return "User not Found";
        }
        Optional<Driver> activeDriver = driverRepository.findByUserId(user);
        if (activeDriver.isPresent()) {
            return "You already have a driver account";
        }
        Driver driver = new Driver();
        driver.setUserId(user);
        driver.setNic_number(request.getNic_number());
        driver.setLicense_number(request.getLicense_number());
        driver.setLicense_exp_date(request.getLicense_exp_date());
        driver.setExperience(request.getExperience());

        Optional<Type> userType = typeRepository.findById(2);
        if (userType.isEmpty()) {
            return "error: User type not found";
        }

        user.setTypeId(userType.get());

        Optional<Status> status = statusRepository.findById(2);
        if (status.isEmpty()) {
            return "error: Status not found";
        }

        Optional<District> district = districtRepository.findById(request.getDistrict());
        if (district.isEmpty()) {
            return "error: District not found";
        }

        driver.setDistrict(district.get());
        driver.setRegistered_date(LocalDate.now());
        driver.setStatusId(status.get());
        driverRepository.save(driver);
        userRepository.save(user);

        int driverId = driver.getId();

        String basePath = "J:/New folder/GoRido/images/";
        String nicPath;
        String licensePath;

        try {
            nicPath = saveFile(request.getNicImage(), basePath + "driver/personal/nic/", driverId + "_nic.jpg");
            licensePath = saveFile(request.getLicenseImage(), basePath + "driver/personal/license/", driverId + "_license.jpg");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }

        driver.setNic_image(nicPath);
        driver.setLicense_image(licensePath);

        driverRepository.save(driver);

        return "success";
    }

    private String saveFile(MultipartFile file, String folderPath, String fileName) throws IOException {

        File dir = new File(folderPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File saveFile = new File(dir, fileName);
        file.transferTo(saveFile);

        return fileName;
    }

    public String driverProfile(Model model, HttpSession session) {

        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            return "redirect:/signin";
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/signin";
        }

        Driver driver = driverRepository.findByUserId(user).orElse(null);
        if (driver == null){
            return "redirect:/driverregi";
        }

        List<Hire> hires = hireRepository.findByDriverId(driver.getId());

        double totalFare = hires.stream()
                .mapToDouble(Hire::getTotal_fare)
                .sum();

        double totalDistance = hires.stream()
                .mapToDouble(Hire::getDistance)
                .sum();

        List<Vehicle> vehicleList = driver.getVehicles();
        List<Hire> latestHires = hireRepository.findTop3ByUserId(user.getId())
                .stream()
                .limit(3)
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("driver", driver);
        model.addAttribute("vehicles", vehicleList);
        model.addAttribute("hires", hires);
        model.addAttribute("totalFare", totalFare);
        model.addAttribute("totalDistance", totalDistance);
        model.addAttribute("latestHires", latestHires);
        model.addAttribute("activePage", "profile");

        return "driverprofile";
    }

    public String deleteDriver(HttpSession session) {

        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "Session expired";

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "User not found";

        Driver driver = user.getDriver();

        if (driver == null){
            return "Driver not found";
        }

        if (driver != null) {

            deleteFile(driver.getNic_image(), "driver/personal/nic");
            deleteFile(driver.getLicense_image(), "driver/personal/license");

            for (Vehicle v : driver.getVehicles()) {
                deleteFile(v.getInsurance_photo(), "driver/vehicle/isuarance");
                deleteFile(v.getVehicle_book(), "driver/vehicle/book");
                deleteFile(v.getVehicle_photo(), "driver/vehicle/vehicle_images");
            }
        }

        Optional<Type> userType = typeRepository.findById(1);
        if (userType.isEmpty()){
            return "error: User type not found";
        }

        user.setTypeId(userType.get());
        user.setDriver(null);
        userRepository.save(user);
        driverRepository.delete(driver);

        return "success";
    }

    private void deleteFile(String fileName, String folder) {

        if (fileName == null) return;

        String basePath = "J:/New folder/GoRido/images/";

        File file = new File(basePath + folder + "/" + fileName);

        if (file.exists()) {
            file.delete();
        }
    }

    public String updatelicense(LocalDate license_exp_date, MultipartFile licenseImage, HttpSession session){
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "Session expired";

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "User not found";

        Driver driver = user.getDriver();

        if (driver == null){
            return "Driver not found";
        }

        int driverId = driver.getId();
        String basePath = "J:/New folder/GoRido/images/";
        String folder = "driver/personal/license/";

        try {
            deleteFile(driver.getLicense_image(), folder);

            String licensePath = saveFile(
                    licenseImage,
                    basePath + folder,
                    driverId + "_license.jpg"
            );

            driver.setLicense_exp_date(license_exp_date);
            driver.setLicense_image(licensePath);

            driverRepository.save(driver);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
        return "success";
    }
}
