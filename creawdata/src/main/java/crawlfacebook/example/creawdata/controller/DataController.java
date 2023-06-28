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
	
	@GetMapping(value="/groups/{groupId}")
	public ResponseEntity<List<Group>> crawlData(@PathVariable("groupId") String groupId, HttpServletRequest request){
		String access_token = "";
		String url = "https://graph.facebook.com/v17.0/"+groupId+"?fields=name,id,feed.fields(full_picture,id,message,type,permalink_url,updated_time,attachments.fileds(media)).limit(100)&access_token="+access_token;
		String cookie = "datr=v-chYjX_2GoJy_bt45Y1MUk0; sb=v-chYgPMClWhSqPp49ztm7FZ; locale=vi_VN; wl_cbv=v2%3Bclient_version%3A2276%3Btimestamp%3A1687416045; dpr=1.25; wd=1492x747; c_user=100015929474344; xs=49%3A36VP6LjaNTE2QA%3A2%3A1687853582%3A-1%3A6321; fr=0ZwAtwgSoKrDP4B99.AWUHkXrQsZUr1FriVcK50wkZMpc.BkmoKl.LZ.AAA.0.0.BkmpoQ.AWUq_g8QAAw; usida=eyJ2ZXIiOjEsImlkIjoiQXJ3d2trdTFyd2FvN2EiLCJ0aW1lIjoxNjg3ODU0MTI2fQ%3D%3D; cppo=1; presence=EDvF3EtimeF1687854136EuserFA21B15929474344A2EstateFDutF0CEchF_7bCC";
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
//        dataService.addData(myList);
        request.getSession().removeAttribute("listData");
        return new ResponseEntity<List<Group>>(myList, HttpStatus.OK);
    }
}


	



