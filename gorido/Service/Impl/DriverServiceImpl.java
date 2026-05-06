package com.example.gorido.Service.Impl;

import com.example.gorido.DTO.DriverRegisterRequest;
import com.example.gorido.Model.*;
import com.example.gorido.Repository.*;
import com.example.gorido.Service.DriverService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
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
    private final ActiveRepository activeRepository;
    private final DriverRepository driverRepository;
    private final TypeRepository typeRepository;
    private final DistrictRepository districtRepository;

    public DriverServiceImpl(UserRepository userRepository, StatusRepository statusRepository, ActiveRepository activeRepository,
                             DriverRepository driverRepository, TypeRepository typeRepository, DistrictRepository districtRepository){
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.statusRepository = statusRepository;
        this.activeRepository = activeRepository;
        this.typeRepository = typeRepository;
        this.districtRepository = districtRepository;
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
        /*User part
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null){
            return "User not Found";
        }*/

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

        Optional<Active> active = activeRepository.findById(1);
        if (active.isEmpty()) {
            return "error: Active status not found";
        }

        Optional<District> district = districtRepository.findById(request.getDistrict());
        if (district.isEmpty()) {
            return "error: District not found";
        }

        driver.setDistrict(district.get());
        driver.setRegistered_date(LocalDate.now());
        driver.setStatusId(status.get());
        driver.setActiveId(active.get());
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
}
