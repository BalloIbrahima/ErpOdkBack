package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.PostulantTire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostulantTrieRepository extends JpaRepository<PostulantTire, Long> {
}
