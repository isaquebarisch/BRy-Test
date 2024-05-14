package com.test.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.test.demo.model.Photo;


@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
