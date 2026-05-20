package com.example.gorido.Controller;
import com.example.gorido.DTO.DriverRegisterRequest;
import com.example.gorido.Service.DriverService;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/driverregi")
    public String driverRegiPage(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null){
            return "signin";
        }
        return "driverregi";
    }

    @GetMapping("/driverprofile")
    public String profile(Model model, HttpSession session) {
        return driverService.driverProfile(model, session);
    }
        @GetMapping("/driver/loadUser")
        @ResponseBody
        public String loadUser(HttpSession session){
            return driverService.loadUser(session);
        }

        @GetMapping("/driver/districts")
        @ResponseBody
        public String getAllDistricts() {
            return driverService.getAllDistricts();
        }

        @PostMapping("/driver/register")
        @ResponseBody
        public String driverRegister(@ModelAttribute DriverRegisterRequest request){
            return driverService.registerDriver(request);
        }

        @GetMapping("/delete/driver")
        @ResponseBody
        public String deleteDriver(HttpSession session) {
            return driverService.deleteDriver(session);
        }

    @ResponseBody
    @PostMapping("/updatelicense")
    public String updatelicense(@RequestParam LocalDate license_exp_date, @RequestParam MultipartFile licenseImage, HttpSession session){
        return driverService.updatelicense(license_exp_date, licenseImage, session);
    }
}
