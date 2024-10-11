package net.alephdev.lab1.repository;

import net.alephdev.lab1.models.AdminRequest;
import net.alephdev.lab1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {
    Optional<AdminRequest> findByUser(User user);
    List<AdminRequest> findAllByStatus(AdminRequest.Status status);
}