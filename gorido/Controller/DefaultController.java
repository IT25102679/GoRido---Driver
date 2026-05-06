package com.example.gorido.Controller;
import com.example.gorido.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    private final UserRepository userRepository;

    public DefaultController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/driver/redirect")
    public String redirectDriver(HttpSession session) {

        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        int typeId = user.getTypeId().getId();

        if (typeId == 1) {
            return "redirect:/driverregi";
        }
        else if (typeId == 2) {
            return "redirect:/driver/profile";
        }

        return "redirect:/userprofile";
    }
}
