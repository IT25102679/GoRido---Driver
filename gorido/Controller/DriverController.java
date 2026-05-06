package com.example.gorido.Controller;
import com.example.gorido.DTO.DriverRegisterRequest;
import com.example.gorido.Service.DriverService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/driverregi")
    public String driverRegiPage() {
        return "driverregi";
    }

    @RestController
    public class driverRegistrationController{
        @GetMapping("/driver/loadUser")
        public String loadUser(HttpSession session){
            return driverService.loadUser(session);
        }

        @GetMapping("/driver/districts")
        @ResponseBody
        public String getAllDistricts() {
            return driverService.getAllDistricts();
        }

        @PostMapping("/driver/register")
        public String driverRegister(@ModelAttribute DriverRegisterRequest request){
            return driverService.registerDriver(request);
        }

    }
}
