package crawlfacebook.example.creawdata.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import crawlfacebook.example.creawdata.model.Group;


@Repository
public interface DataRepository extends JpaRepository<Group, String>{
	List<Group> findByGroupId(String groupId);
}