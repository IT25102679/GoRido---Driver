package com.example.gorido.Service;
import com.example.gorido.DTO.DriverRegisterRequest;
import jakarta.servlet.http.HttpSession;

public interface DriverService {
    String loadUser(HttpSession session);
    String getAllDistricts();
    String registerDriver(DriverRegisterRequest request);
}
