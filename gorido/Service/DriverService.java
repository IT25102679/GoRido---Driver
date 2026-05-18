package com.example.gorido.Service;
import com.example.gorido.DTO.DriverRegisterRequest;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface DriverService {
    String loadUser(HttpSession session);
    String getAllDistricts();
    String registerDriver(DriverRegisterRequest request);
    String driverProfile(Model model, HttpSession session);
    String deleteDriver(HttpSession session);
    String updatelicense(LocalDate license_exp_date, MultipartFile licenseImage, HttpSession session);
}
