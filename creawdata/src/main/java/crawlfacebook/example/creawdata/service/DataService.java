package crawlfacebook.example.creawdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crawlfacebook.example.creawdata.model.Data;
import crawlfacebook.example.creawdata.repository.DataRepository;

@Service
public class DataService{
	@Autowired
	private DataRepository dataRepository;
	
	public List<Data> addData(List<Data> dataList) {
	    if (dataList != null && !dataList.isEmpty()) {
	        return dataRepository.saveAll(dataList);
	    } else {
	        return null;
	    }
	}
}
