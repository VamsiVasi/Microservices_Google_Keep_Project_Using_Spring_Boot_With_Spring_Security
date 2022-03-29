package com.albanero.User_Microservice_.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.albanero.User_Microservice_.Config.JwtUtil;
import com.albanero.User_Microservice_.Config.UserDetailsServiceImpl;
import com.albanero.User_Microservice_.Exceptions.Resource_NotFound_Exception_;
import com.albanero.User_Microservice_.Request.JwtRequest;
import com.albanero.User_Microservice_.Request.Notes_;
import com.albanero.User_Microservice_.Response.JwtResponse;
import com.albanero.User_Microservice_.Response.Microservice_NotesList_Response_;
import com.albanero.User_Microservice_.Response.Microservice_UserList_Response_;
import com.albanero.User_Microservice_.Service.User_Serv_;

@RestController
@RequestMapping(value = "/keep")
public class User_Contro_ {

	@Autowired
	private User_Serv_ user_Serv;

	@Autowired
	private WebClient.Builder webClient;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	// Generate for Valid User
	@PostMapping(value = "/generateToken")
	public JwtResponse generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		System.out.println(jwtRequest);
		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		} catch (Exception e) {
			throw new Exception("Invalid UserName Or Password");
		}
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
		String token = this.jwtUtil.generateToken(userDetails);
		System.out.println("JWT " + token);
		return new JwtResponse(token);
	}

	// Create User
	@PostMapping("/Create/User")
	public String createUserNotes(@RequestParam("username") String UserName, @RequestParam("password") String Password,
			@RequestParam("email") String Email) throws IOException, Resource_NotFound_Exception_ {
		user_Serv.createUserNotes(UserName, Password, Email);
		return UserName + ", Your Details are Successfully Added\nStatus : " + HttpStatus.OK.toString();
	}

	// Create Notes For a User Using User Token
	@PostMapping("/Create/Notes")
	public String createNotesByUserName(@RequestParam("file") MultipartFile File,
			@RequestParam("notesname") String NotesName, HttpServletRequest request)
			throws Resource_NotFound_Exception_, IOException {
		user_Serv.createNotesByUserName(File, NotesName, request);
		return NotesName + " notes was Successfully Created\nStatus : " + HttpStatus.OK.toString();
	}

	// Get All Users Using Admin Token
	@GetMapping("/Get/AllUsers")
	public Microservice_UserList_Response_ getAllUsers() {
		return user_Serv.getAllUsers();
	}

	// Get All Notes Of The User Using User Token
	@GetMapping("/Get/AllNotes")
	public Microservice_NotesList_Response_ getAllNotesByUserName(HttpServletRequest request)
			throws Resource_NotFound_Exception_ {
		return user_Serv.getAllNotesByUserName(request);
	}

	// Get A Particular Notes Of The User Using User Token And Notes Name
	@GetMapping("/Get/Notes/{name}")
	public Microservice_NotesList_Response_ getNotesByName(HttpServletRequest request, @PathVariable String name)
			throws Resource_NotFound_Exception_ {
		return user_Serv.getNotesByName(request, name);
	}

	// Update User Details Using User Token
	@PutMapping("/Update/User")
	public String updateUser(@RequestParam("username") String UserName, @RequestParam("password") String Password,
			HttpServletRequest request) throws Resource_NotFound_Exception_ {
		user_Serv.updateUser(UserName, Password, request);
		return UserName + ", Your Details are Successfully Updated\nStatus : " + HttpStatus.OK.toString();
	}

	// Update A Particular Notes Of The User Using User Token And Notes Name
	@PutMapping("/Update/Notes/{name}")
	public String updateNotes(@RequestParam("file") MultipartFile File, @RequestParam("notesname") String NotesName,
			HttpServletRequest request, @PathVariable String name) throws IOException, Resource_NotFound_Exception_ {
		user_Serv.updateNotes(File, NotesName, request, name);
		return name + " notes was Successfully Updated.\nStatus : " + HttpStatus.OK.toString();
	}

	// Delete A Particular Notes Of The User Using User Token And Notes Name
	@DeleteMapping("/Delete/Notes/{name}")
	public String deleteNotes(HttpServletRequest request, @PathVariable String name)
			throws Resource_NotFound_Exception_ {
		user_Serv.deleteNotes(request, name);
		return " Your " + name + " notes was successfully removed\nStatus : " + HttpStatus.OK.toString();
	}

	// Delete User Using User Token
	@DeleteMapping("/Delete/User")
	public String deleteUser(HttpServletRequest request) throws Resource_NotFound_Exception_ {
		user_Serv.deleteUser(request);
		return "Your account and all your notes were successfully removed\nStatus : " + HttpStatus.OK.toString();
	}

	// Delete User Using Admin Token And UserName
	@DeleteMapping("/Delete/User/{userName}")
	public String deleteUserByAdmin(@PathVariable String userName) throws Resource_NotFound_Exception_ {
		user_Serv.deleteUserByAdmin(userName);
		return "Admin, You have removed '" + userName + "' account and all of " + userName
				+ " notes were successfully removed\nStatus : " + HttpStatus.OK.toString();
	}

	// Download A Notes File Using User Token
	@GetMapping("/{fileName}/{id}")
	ResponseEntity<byte[]> downloadFile(@PathVariable String fileName, @PathVariable String id,
			HttpServletRequest request) {
		Notes_ notesResponse = webClient.build().get().uri("http://localhost:1999/Get/NotesById/" + id).retrieve()
				.bodyToMono(Notes_.class).block();
		String mimeType = request.getServletContext().getMimeType(fileName);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + fileName).body(notesResponse.getFile());
	}

}
