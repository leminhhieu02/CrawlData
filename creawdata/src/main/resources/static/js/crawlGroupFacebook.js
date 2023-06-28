
$(document).ready(function () {
	// Start the process when the button is clicked
	// $("#btn-crawl").click(function () {
	// 	$.get("/startProcess", function (data) {
	// 		console.log(data);
	// 	});
	// });

	// Fetch the progress periodically and update the progress bar
	setInterval(function () {
		$.get("/progress", function (data) {
			$("#progressBar").css("width", data + "%");
			$("#progressBar").text(data + "%");
			if(data === 101){
				document.querySelector("div[class='progress']").style.display="none";
				document.querySelector('#table-scroll').style.display="block";
			}else if(data >=1){
				document.querySelector("div[class='progress']").style.display="";
				document.querySelector('#table-scroll').style.display="none";
			}
		});
	}, 50);
});




function crawlData(){
	let groupId = document.getElementById("groupId").value; 
	var PostAPI = 'http://localhost:8088/crawl/groups/'+ groupId;
	var options = {
		method: 'GET',
		headers:{
			'Content-type': 'application/json'
		}
	};
	fetch(PostAPI, options)
	.then(function(response){ 
		return response.json();
	})
	.then(function(response){
		renderPosts(response);
	});
}

function insertData(){
	if(confirm("Bạn có muốn thêm kết quả vào cơ sở dữ liệu?")===true){
		var PostAPI = 'http://localhost:8088/crawl/insertDB';
		var options = {
			method: 'GET',
			headers:{
				'Content-type': 'application/json'
			}
		};
		fetch(PostAPI, options)
		.then(function(response){
			if(response.status===200){
				alert("Thêm thành công!");
			}
			return response.json();
		})
		.then(function(response){
		});
	}
}


// ----------------------------------------------- get -----------------------------------------------

function getPostToUpdate(id){
	getPost(id, renderForm);
}


function getPosts(callback){
	var PostAPI = 'http://localhost:8088/crudAPI';
	fetch(PostAPI)
	.then(function(response){
		return response.json();
	})
	.then(callback);
}

function getPost(id ,callback){
	var PostAPI = 'http://localhost:8088/Post/'+id;
	var options = {
		method: 'GET',
		headers:{
			'Content-type': 'application/json'
		}
	};
	fetch(PostAPI, options)
	.then(function(response){
		return response.json();
	})
	.then(callback);
}


// ----------------------------------------------- post -----------------------------------------------

// document.querySelector('#Add').onclick = function(){
// 	var address = document.querySelector('input[name="address"]').value;
// 	var dateOfBirth = document.querySelector('input[name="dateOfBirth"]').value;
// 	var firstName = document.querySelector('input[name="firstName"]').value;
// 	var gender = document.querySelector('input[name="gender"]:checked').value;
// 	var image = document.querySelector('input[name="image"]').value;
// 	var lastName = document.querySelector('input[name="lastName"]').value;
// 	var formData = {
// 		address: address,
// 		dateOfBirth: dateOfBirth,
// 		firstName:firstName, 
// 		gender:gender, 
// 		image: image,
// 		lastName: lastName
// 	}
// 	addPost(formData, renderPost);
	
// }

// function addPost(data, callback){
// 	var PostAPI = 'http://localhost:8088/validate';
// 	var options = {
// 		method: 'POST',
// 		headers:{
// 			'Content-type': 'application/json'
// 		},
// 		body: JSON.stringify(data)
// 		// body: data
// 	};

// 	fetch(PostAPI, options)
// 	.then(function(response){
// 		console.log(response.status);
// 		if(response.status == 200){return response.json(); }
		
// 	})
// 	.then(callback)
// 	.catch(function(error) {
//       console.log(error);
//     });
// }


// ----------------------------------------------- put -----------------------------------------------

// document.querySelector('#Update').onclick = function(){
// 	var PostID = document.querySelector('input[name="PostID"]').value;
// 	var address = document.querySelector('input[name="address"]').value;
// 	var dateOfBirth = document.querySelector('input[name="dateOfBirth"]').value;
// 	var firstName = document.querySelector('input[name="firstName"]').value;
// 	var gender = document.querySelector('input[name="gender"]:checked').value;
// 	var image = document.querySelector('input[name="image"]').value;
// 	var lastName = document.querySelector('input[name="lastName"]').value;
// 	var formData = {
// 		PostID: PostID,
// 		address: address,
// 		dateOfBirth: dateOfBirth,
// 		firstName:firstName, 
// 		gender:gender, 
// 		image: image,
// 		lastName: lastName
// 	}
// 	updatePost(formData, function(Post){
// 		if(Post !== null){
// 			var updateItem = document.querySelector('.Post-item-' +Post.PostID);
// 			if(updateItem){
// 				updateItem.innerHTML= `<td>${Post.address}</td>
// 					<td>${Post.PostID}</td>
// 					<td>${Post.dateOfBirth}</td>
// 					<td>${Post.firstName}</td>
// 					<td>${Post.gender}</td>
// 					<td>${Post.image}</td>
// 					<td>${Post.lastName}</td>
// 					<td><button onclick="deletePost(${Post.PostID})" name="delete">Delete</button></td>
// 					<td><button onclick="getPostToUpdate(${Post.PostID})" name="update">Update</button></td>`;
// 			}
// 		}	
// 	});
	
// }


// function updatePost(data, callback){
// 	var PostAPI = 'http://localhost:8088/Post';
// 	var options = {
// 		method: 'PUT',
// 		headers:{
// 			'Content-type': 'application/json'
// 		},
// 		body: JSON.stringify(data)
// 		// body: data
// 	};

// 	fetch(PostAPI, options)
// 	.then(function(response){
// 		console.log(response.status);
// 		return response.json(); 
// 	})
// 	.then(callback).catch(function(error) {
//       console.log(error);
//     });
// }


// ----------------------------------------------- delete -----------------------------------------------


// function deletePost(id){
// 	var PostAPI = 'http://localhost:8088/Post';
// 	var options = {
// 		method: 'DELETE',
// 		headers:{
// 			'Content-type': 'application/json'
// 		}
// 	};

// 	fetch(PostAPI + '/'+id, options)
// 	.then(function(response){
// 		response.json();
// 	})
// 	.then(function(){
// 		var removeItem = document.querySelector('.Post-item-' +id);
// 		if(removeItem){
// 			removeItem.remove();
// 		}
		
// 	});
// }


// ----------------------------------------------- render -----------------------------------------------
function renderPosts(Posts){
	var table = document.querySelector('#tbody');
	let stt=1;
	var htmls = Posts.map(function(Post){
		let ulli = "";
		if(Post.url !== null ){
			const urlArray = Post.url.split(";");
			let count =1;
			urlArray.forEach(element => {
				ulli +=`<li><a href="${element}" target="_blank">link ${count}</a></li>`;
				 count++;
			});
		}
		
		return `<tr onclick="showMore(this)">
					<td class="textwrap">${stt++}</td>
					<td class="textwrap">${Post.groupId}</td>
					<td class="textwrap">${Post.groupName}</td>
					<td class="textwrap"><a style="text-decoration:none" href="https://www.facebook.com/${Post.postId.replace('_', '/posts/')}" target="_blank">${Post.postId.split("_")[1]}</a></td>
					<td class="textwrap">${Post.contentText}</td>
					<td class="textwrap">${Post.type}</td>
					<td class="textoverflow">
						<ul>${ulli}</ul>
					</td>
					<td class="textwrap">${Post.time}</td>
				</tr>`;
	});
	
	
	table.innerHTML =htmls.join('');
	
}
function showMore(tr){
	var tdElements = tr.getElementsByTagName('td');
	// Loop through each <td> element
	for (var i = 0; i < tdElements.length; i++) {
		var tdElement = tdElements[i];
		var computedStyle = window.getComputedStyle(tdElement);
		if(computedStyle.whiteSpace === "nowrap"){
			tdElement.style.whiteSpace  ='normal';
			tdElement.style.textOverflow  ='';
		}else{
			tdElement.style.whiteSpace  ='nowrap';
			tdElement.style.textOverflow  ='ellipsis';
		}
	}
}
// function renderPost(Post){
// 	if(Post !== null){
// 		var table = document.querySelector('#Post');
// 		table.innerHTML +=`
// 				<tr class="Post-item-${Post.PostID}">
// 					<td>${Post.address}</td>
// 					<td>${Post.PostID}</td>
// 					<td>${Post.dateOfBirth}</td>
// 					<td>${Post.firstName}</td>
// 					<td>${Post.gender}</td>
// 					<td>${Post.image}</td>
// 					<td>${Post.lastName}</td>
// 					<td><button onclick="deletePost(${Post.PostID})" name="delete">Delete</button></td>
// 					<td><button onclick="getPostToUpdate(${Post.PostID})" name="update">Update</button></td>
// 				</tr>
// 			`;	
// 	}
// }

// function renderForm(Post){
// 	document.querySelector('input[name="PostID"]').value = Post.PostID;
// 	document.querySelector('input[name="address"]').value = Post.address;
// 	document.querySelector('input[name="dateOfBirth"]').value = Post.dateOfBirth;
// 	document.querySelector('input[name="firstName"]').value = Post.firstName;
// 	document.querySelector('input[name="image"]').value = Post.image;
// 	document.querySelector('input[name="lastName"]').value = Post.lastName;
// 	if(Post.gender ===true) 	document.querySelector('input[id="male"]').checked = true;
// 	else document.querySelector('input[id="female"]').checked = true;
// }