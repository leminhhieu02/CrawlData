package crawlfacebook.example.creawdata.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawlfacebook.example.creawdata.ProgressService;
import crawlfacebook.example.creawdata.model.Group;
import crawlfacebook.example.creawdata.repository.DataRepository;
import crawlfacebook.example.creawdata.service.DataService;


@RestController
@RequestMapping("/crawl")
public class DataController {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DataService dataService;
	
	@Autowired
	private DataRepository dataRepository;
	
	@Autowired
    private ProgressService progressService;
	
	@GetMapping(value="/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
	@GetMapping(value="/success")
	public ModelAndView indexx() {
		return new ModelAndView("success");
	}
	
	@GetMapping(value="/groups/{groupId}")
	public ResponseEntity<List<Group>> crawlData(@PathVariable("groupId") String groupId, HttpServletRequest request){
		String access_token = "EAAGNO4a7r2wBAKgaIuqEgrQbnCSezGgGmthcAjBwwk1Pl4k0YlSgZCotFukZA0bdeHjhv9RrkwJ4XCZAJgkDFIJZBi2tesayUIDKoNB8v1gCfp1G9b1qdwSEZBswCxlBYcW7RiLr0JhYMMjJST9drnDd7qsvEZCK7U36tZCqZAVzSpILHaIJee7ZAgBvXYXR5vfXoXqjLCJaBMgZDZD";
		String url = "https://graph.facebook.com/v17.0/"+groupId+"?fields=name,id,feed.fields(full_picture,id,message,type,permalink_url,updated_time,attachments.fileds(media)).limit(100)&access_token="+access_token;
		String cookie = "sb=V02AZK0Lx01kPHnbwZwPlGdT; datr=V02AZKyTMKMxEBzlc7PJXjjD; dpr=1.25; locale=vi_VN; wd=1492x739; c_user=100024859335889; xs=49%3ApZf58UL_zoIZ6g%3A2%3A1687845891%3A-1%3A6321; usida=eyJ2ZXIiOjEsImlkIjoiQXJ3d2Z6ZDF2ZXBmbDUiLCJ0aW1lIjoxNjg3ODQ4MTY5fQ%3D%3D; cppo=1; fr=0KZz8kvD1NYyHaSt5.AWXxII01ReWyhfqWUujuLDSXPnI.BkmmDw.NN.AAA.0.0.BkmoT0.AWX8LFHshqo; presence=C%7B%22t3%22%3A%5B%5D%2C%22utc3%22%3A1687848318528%2C%22v%22%3A1%7D";
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", cookie);
		System.out.println(url);
		CompletableFuture.runAsync(() -> progressService.executeLongRunningProcess());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		List<Group> listData = new ArrayList<Group>();
		String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode root = objectMapper.readTree(response);
	        String groupName = root.path("name").asText();
	        JsonNode dataNode = root.path("feed").path("data");

	        
	        for (JsonNode node : dataNode) {
	            if (node.has("id") && node.has("message") && node.has("type") && node.has("permalink_url")) {
	                Group data = new Group();
	                data.setGroupId(groupId);    
	                data.setGroupName(groupName);
	                data.setPostId(node.path("id").asText());
	                data.setType(node.path("type").asText());
	                data.setContentText(node.path("message").asText());  
	                
	                JsonNode attachmentsData = node.path("attachments").path("data");
                    for (JsonNode attachmentData : attachmentsData) {
                    	String src="";
						if(attachmentData.has("subattachments")) {
							JsonNode subAttachmentsDataNode = attachmentData.path("subattachments").path("data");
	                        for (JsonNode subAttachment : subAttachmentsDataNode) {
	                        	if(subAttachment.path("media").has("source"))  src += subAttachment.path("media").path("source").asText() +";";
	                        	else src += subAttachment.path("media").path("image").path("src").asText() +";"; 
	                        }
		                    data.setUrl(src.substring(0, src.length()-1));
		                    break;
						}else if(attachmentData.has("media")){
							if(attachmentData.path("media").has("source")) src =attachmentData.path("media").path("source").asText();
		                	else src = attachmentData.path("media").path("image").path("src").asText();
//			                if(node.path("type").asText().equals("photo")) data.setUrl(node.path("full_picture").asText());   
//			                else if(node.path("type").asText().equals("video")) data.setUrl(node.path("permalink_url").asText()); 
		                	data.setUrl(src);
						}
					}
	                
	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	                Date date = dateFormat.parse(node.path("updated_time").asText());
	                data.setTime(date); 		System.out.println(data.getPostId());
	                
	                listData.add(data);
	            }
	        }
	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return "Error occurred: " + e.getMessage();
	    } catch (Exception ex) {
//	        ex.printStackTrace();
//	        return "Error occurred while inserting data into database: " + ex.getMessage();
	    }
	    if(listData.size() >0) {
	    	HttpSession session = request.getSession();
	    	session.setAttribute("listData", listData);

	    } System.out.println(listData.size());
	    return new ResponseEntity<List<Group>>(listData,HttpStatus.OK );
	}

	
	@GetMapping("/{groupId}/posts")
    public ResponseEntity<List<Group>> getGroupPosts(@PathVariable String groupId) {
        return new ResponseEntity<List<Group>>(dataService.getGroupPosts(groupId), HttpStatus.FOUND);
    }
	
	@GetMapping("/insertDB")
    public ResponseEntity<List<Group>> insertDB(HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<Group> myList = (List<Group>) session.getAttribute("listData");
        dataService.addData(myList);
        request.getSession().removeAttribute("listData");
        return new ResponseEntity<List<Group>>(myList, HttpStatus.OK);
    }
}


	



