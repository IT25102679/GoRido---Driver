package com.example.gorido.Repository;
import com.example.gorido.Model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query("SELECT d.name FROM District d")
    List<String> findAllNames();
}
