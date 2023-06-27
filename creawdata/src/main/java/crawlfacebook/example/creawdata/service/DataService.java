package crawlfacebook.example.creawdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawlfacebook.example.creawdata.model.Group;
import crawlfacebook.example.creawdata.repository.DataRepository;

@Service
public class DataService{
	@Autowired
	private DataRepository dataRepository;
	
	public List<Group> addData(List<Group> dataList) {
	    if (dataList != null && !dataList.isEmpty()) {
	        return dataRepository.saveAll(dataList);
	    } else {
	        return null;
	    }
	}
	
	public List<Group> getGroupPosts(String groupId) {
	    return dataRepository.findByGroupId(groupId);
	}
}
