package crawlfacebook.example.creawdata.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import crawlfacebook.example.creawdata.model.Data;


@Repository
public interface DataRepository extends JpaRepository<Data, Long>{

}