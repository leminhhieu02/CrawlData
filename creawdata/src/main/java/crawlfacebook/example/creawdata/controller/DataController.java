package crawlfacebook.example.creawdata.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawlfacebook.example.creawdata.model.Data;
import crawlfacebook.example.creawdata.repository.DataRepository;
import crawlfacebook.example.creawdata.service.DataService;


@Controller
@RequestMapping("/crawl")
public class DataController {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DataService dataService;
	@Autowired
	private DataRepository dataRepository;
	@GetMapping
	public String index() {
		return "crawldata";
	}
	@PostMapping
	public String crawlData(@RequestParam("groupId") long groupId){
		
		String url = "https://graph.facebook.com/112853312710153?fields=id,name,feed{id,message,type,permalink_url,updated_time}&access_token=EAAGNO4a7r2wBALNyQSdvywJvKsvwuq5oXhBTc6ZAraoSlblYxzR8R6iAnQeS7XZCImAftZA3It5TUDZBBZAFzwNJ2kwxhquGJI9xpZAVHZCzrF8rHt2Q5cTId3qTmNZAhXe6BsdVlOHKe3PUTrmYGiXzmUZBytg7chiZCZCX6hDZA9K7n0SJIFdeg7u6";
		String cookie = "sb=V02AZK0Lx01kPHnbwZwPlGdT; datr=V02AZKyTMKMxEBzlc7PJXjjD; dpr=1.25; c_user=100024859335889; wd=1492x739; cppo=1; xs=46%3AMSVIyGiUQZrbVg%3A2%3A1686130120%3A-1%3A6321%3A%3AAcVdQSYWF7TCvhOI_qzN8W4PRXXJYkXTlq28IhT82wc; presence=C%7B%22lm3%22%3A%22u.100015929474344%22%2C%22t3%22%3A%5B%5D%2C%22utc3%22%3A1687487102940%2C%22v%22%3A1%7D; usida=eyJ2ZXIiOjEsImlkIjoiQXJ3b3BseDE0Y2VhemYiLCJ0aW1lIjoxNjg3NDg3NDEwfQ%3D%3D; fr=0Q8QL9BpkoTvvjZd5.AWUigw7dNQONHzrFm0FCt0FsBnk.BklPrl.NN.AAA.0.0.BklQO5.AWV6ttj2ueY";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", cookie);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode root = objectMapper.readTree(response);
	        String groupName = root.path("name").asText();
	        JsonNode dataNode = root.path("feed").path("data");

	        List<Data> listData = new ArrayList<>();
	        for (JsonNode node : dataNode) {
	            if (node.has("id") && node.has("message") && node.has("type") && node.has("permalink_url")) {
	                Data data = new Data();
	                data.setGroupId(groupId);
	                data.setGroupName(groupName);
	                data.setPostId(node.path("id").asLong());
	                data.setType(node.path("type").asText());
	                data.setContentText(node.path("message").asText());
	                data.setUrl(node.path("permalink_url").asText());
	                data.setTime(new Date(node.path("updated_time").asLong() * 1000L));
	                listData.add(data);
	            }
	        }
	        dataService.addData(listData);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "Error occurred: " + e.getMessage();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return "Error occurred while inserting data into database: " + ex.getMessage();
	    }
	    return "/";
	}

}

//	@PostMapping
//	public String crawlData(@RequestParam("groupId") long groupId){
//		String url="https://graph.facebook.com/me?fields=id,name&access_token=EAALhzvjLnGoBAJU0lfK3K7PCr5N3VzBMsbU2CMQoNJzFpMB7AFSQbIMme8mDxcZAAynE6wk4gPdJqsV8UrEEmJtjKJzslI96V43fezgZBoIHA6UABO67BMZAwx4TXLpjpZAWeG9Lko0G0sxgKUK5CmlJx0BB0VQzlBJxRpwYewX099KcxZBPDRwWdfdHmYYYDgVWRg5AITQZDZD";
//		String response = restTemplate.getForObject(url, String.class);
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//	        JsonNode root = objectMapper.readTree(response);
//	        List<Data> listData = new ArrayList<>();		
//			Data data = new Data();
//			data.setGroupId(groupId);
//			data.setContentText("abcd");
//			data.setPostId(root.path("id").asLong());
//			data.setType("abcd");
//			data.setUrl("facebook.comm");
//			data.setGroupName(root.path("name").asText());				
//			data.setTime(new Date());
//			listData.add(data);
//			dataService.addData(listData);
//
//        
//		} catch (IOException e) {
//	        e.printStackTrace();
//	        return "Error occurred: " + e.getMessage();
//	    } catch (Exception ex) {
//	        ex.printStackTrace();
//	        return "Error occurred while inserting data into database: " + ex.getMessage();
//	    }
//	    return "/";
//	}
//		
//}

//	@GetMapping
//	public String crawlData(@RequestParam("groupId") long groupId){
//	    String url = "https://graph.facebook.com/" + groupId + "?fields=id,name,feed{id,message,type,permalink_url}&access_token=EAAGNO4a7r2wBALNyQSdvywJvKsvwuq5oXhBTc6ZAraoSlblYxzR8R6iAnQeS7XZCImAftZA3It5TUDZBBZAFzwNJ2kwxhquGJI9xpZAVHZCzrF8rHt2Q5cTId3qTmNZAhXe6BsdVlOHKe3PUTrmYGiXzmUZBytg7chiZCZCX6hDZA9K7n0SJIFdeg7u6";
//	    String response = restTemplate.getForObject(url, String.class);
//	    try {
//	        // Chuyển đổi đối tượng JSON thành đối tượng Java
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        JsonNode root = objectMapper.readTree(response);
//
//	        // Lấy danh sách bài đăng từ đối tượng JSON
//	        JsonNode dataNode = root.path("feed").path("data");
//	        List<String> listData = new ArrayList<>();
//	        for (JsonNode node : dataNode) {
//	            String data = "";
//	            data += "GroupId: " + groupId;
//	            data += ", GroupName: " + node.path("name").asText();
//	            data += ", PostId: " + node.path("id").asLong();
//	            data += ", Message: " + node.path("message").asText();
//	            data += ", Type: " + node.path("type").asText();
//	            data += ", PermalinkUrl: " + node.path("permalink_url").asText();
//	      
//	            listData.add(data);
//	        }
//
//	        // Insert danh sách bài đăng vào database thông qua service
//	        dataService.addData(listData);
//
//	        
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
//	}
	



