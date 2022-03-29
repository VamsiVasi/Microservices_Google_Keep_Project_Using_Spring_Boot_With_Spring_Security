package com.albanero.User_Microservice_.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.albanero.User_Microservice_.Config.JwtUtil;
import com.albanero.User_Microservice_.Exceptions.Resource_NotFound_Exception_;
import com.albanero.User_Microservice_.Model.Users_;
import com.albanero.User_Microservice_.Repository.User_Repo_;
import com.albanero.User_Microservice_.Request.Notes_;
import com.albanero.User_Microservice_.Response.Microservice_NotesList_Response_;
import com.albanero.User_Microservice_.Response.Microservice_UserList_Response_;

import reactor.core.publisher.Mono;

@Service
public class User_Serv_ {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private User_Repo_ user_Repo;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private WebClient.Builder webClient;

	Logger log = LoggerFactory.getLogger(User_Serv_.class);

	// Create User
	public void createUserNotes(String userName, String password, String email)
			throws IOException, Resource_NotFound_Exception_ {
		ArrayList<String> ua = new ArrayList<String>();
		Users_ user = new Users_();
		user.setUserName(userName);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole("ROLE_USER");
		user.setNotesIds(ua);
		user_Repo.save(user);
		log.info("\nUser with id : " + user.getId() + " was Successfully Created");
	}

	// Create Notes For a User Using User Token
	public void createNotesByUserName(MultipartFile file, String notesName,
			HttpServletRequest request) throws IOException, Resource_NotFound_Exception_ {
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null, jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwtToken);
		}
		Optional<Users_> optionalUser = user_Repo.findByUserName(username);
		Users_ user = optionalUser.get();
		ArrayList<String> ua = user.getNotesIds();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.isEmpty()) {
			log.warn("\nNo File was Selected");
			throw new Resource_NotFound_Exception_("No File was Selected");
		}
		String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName).toUriString();
		Notes_ newNotes = new Notes_(notesName, file.getBytes(), url);
		Notes_ notesResponse = webClient.build().post().uri("http://localhost:1999/Create/Notes")
				.body(Mono.just(newNotes), Notes_.class).retrieve().bodyToMono(Notes_.class).block();
		String url1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName + "/")
				.path(notesResponse.getId()).toUriString();
		newNotes.setFileURL(url1);
		Notes_ notesResponse2 = webClient.build().put()
				.uri("http://localhost:1999/Put/NotesById/" + notesResponse.getId())
				.body(Mono.just(newNotes), Notes_.class).retrieve().bodyToMono(Notes_.class).block();
		ua.add(notesResponse.getId());
		user.setNotesIds(ua);
		user_Repo.save(user);
		log.info("\n" + user.getUserName() + ", created a New Notes with id : " + notesResponse.getId());
	}
	
	// Get All Users Using Admin Token
	public Microservice_UserList_Response_ getAllUsers() {
		List<Users_> allUsers = user_Repo.findByRole("ROLE_USER");
		log.info("\n Total Users returned are {}", allUsers.size());
		ArrayList<String> names=new ArrayList<String>();
		for(int i=0;i<allUsers.size();i++)
		{
			names.add(allUsers.get(i).getUserName());
		}
		return new Microservice_UserList_Response_(names, HttpStatus.OK.toString());
	}

    // Get All Notes Of The User Using User Token
	public Microservice_NotesList_Response_ getAllNotesByUserName(HttpServletRequest request)
			throws Resource_NotFound_Exception_ {
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null, jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwtToken);
		}
		Optional<Users_> optionalUser = user_Repo.findByUserName(username);
		Users_ user = optionalUser.get();
		ArrayList<String> ua = user.getNotesIds();
		ArrayList<Notes_> allNotesOfUser = new ArrayList<Notes_>();
		for (int i = 0; i < ua.size(); i++) {
			Notes_ notesResponse = webClient.build().get().uri("http://localhost:1999/Get/NotesById/" + ua.get(i))
					.retrieve().bodyToMono(Notes_.class).block();
			allNotesOfUser.add(notesResponse);
		}
		log.info("\n Total Notes of the " + username + " are {}", allNotesOfUser.size());
		HashMap<String,String> allNotes=new HashMap<String,String>();
		for(int i=0;i<allNotesOfUser.size();i++)
		{
			allNotes.put(allNotesOfUser.get(i).getName(),allNotesOfUser.get(i).getFileURL());
		}
		return new Microservice_NotesList_Response_(allNotes, HttpStatus.OK.toString());
	}

    // Get A Particular Notes Of The User Using User Token And Notes Name
	public Microservice_NotesList_Response_ getNotesByName(HttpServletRequest request, String name)
			throws Resource_NotFound_Exception_ {
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null, jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwtToken);
		}
		Optional<Users_> optionalUser = user_Repo.findByUserName(username);
		try {
			Users_ user = optionalUser.get();
			ArrayList<String> ua = user.getNotesIds();
			ArrayList<Notes_> allNotesOfUser = new ArrayList<Notes_>();
			for (int i = 0; i < ua.size(); i++) {
				Notes_ notesResponse = webClient.build().get().uri("http://localhost:1999/Get/NotesById/" + ua.get(i))
						.retrieve().bodyToMono(Notes_.class).block();
				allNotesOfUser.add(notesResponse);
			}
			Notes_ notesResponse1 = webClient.build().get().uri("http://localhost:1999/Get/NotesByName/" + name)
					.retrieve().bodyToMono(Notes_.class).block();
			log.info("\n" + username + "'s Notes With Name : " + name + " Was Successfully Retrieved");
			HashMap<String,String> oneNotes=new HashMap<String,String>();
			oneNotes.put(notesResponse1.getName(), notesResponse1.getFileURL());
			return new Microservice_NotesList_Response_(oneNotes, HttpStatus.OK.toString());
		} catch (Exception e) {
			throw new Resource_NotFound_Exception_("No Notes was found with the name :- '" + name + "'.");
		}
	}

    // Update User Details Using User Token
	public void updateUser(String UserName, String password, HttpServletRequest request)
			throws Resource_NotFound_Exception_ {
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null, jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwtToken);
		}
		Optional<Users_> optionalUser = user_Repo.findByUserName(username);
		Users_ updateUK = optionalUser.get();
		updateUK.setUserName(UserName);
		updateUK.setPassword(passwordEncoder.encode(password));
		user_Repo.save(updateUK);
		log.info("\n" + username + " Updated Details : \nUser Name was updated to '" + UserName
				+ "'\nPassword was updated to '" + password + "'");
	}

    // Update A Particular Notes Of The User Using User Token And Notes Name
	public void updateNotes(MultipartFile file, String notesName,
			HttpServletRequest request, String name) throws IOException, Resource_NotFound_Exception_ {
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null, jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwtToken);
		}
		Optional<Users_> optionalUser = user_Repo.findByUserName(username);
		try {
			Users_ user = optionalUser.get();
			ArrayList<String> ua = user.getNotesIds();
			ArrayList<Notes_> allNotesOfUser = new ArrayList<Notes_>();
			for (int i = 0; i < ua.size(); i++) {
				Notes_ notesResponse = webClient.build().get().uri("http://localhost:1999/Get/NotesById/" + ua.get(i))
						.retrieve().bodyToMono(Notes_.class).block();
				allNotesOfUser.add(notesResponse);
			}
			Notes_ notesResponse1 = webClient.build().get().uri("http://localhost:1999/Get/NotesByName/" + name)
					.retrieve().bodyToMono(Notes_.class).block();
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			if (fileName.isEmpty()) {
				log.warn("\nNo File was Selected");
				throw new Resource_NotFound_Exception_("No File was Selected");
			}
			String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName + "/")
					.path(notesResponse1.getId()).toUriString();
			Notes_ updateNK = new Notes_();
			updateNK.setName(notesName);
			updateNK.setFile(file.getBytes());
			updateNK.setFileURL(url);
			Notes_ notesResponse2 = webClient.build().put()
					.uri("http://localhost:1999/Put/NotesById/" + notesResponse1.getId())
					.body(Mono.just(updateNK), Notes_.class).retrieve().bodyToMono(Notes_.class).block();
			log.info("\n" + username + "'s, " + name + " Notes Updated Details : \nNotes Name was updated to '"
					+ notesName + "'\nFile URl was updated to '" + url + "'");
		} catch (Exception e) {
			throw new Resource_NotFound_Exception_(
					"No Notes was found with the name :- '" + name + "' / No File was Selected");
		}
	}

    // Delete A Particular Notes Of The User Using User Token And Notes Name
	public void deleteNotes(HttpServletRequest request, String name) throws Resource_NotFound_Exception_ {
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null, jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwtToken);
		}
		Optional<Users_> optionalUser = user_Repo.findByUserName(username);
		try {
			Users_ user = optionalUser.get();
			ArrayList<String> ua = user.getNotesIds();
			ArrayList<Notes_> allNotesOfUser = new ArrayList<Notes_>();
			for (int i = 0; i < ua.size(); i++) {
				Notes_ notesResponse = webClient.build().get().uri("http://localhost:1999/Get/NotesById/" + ua.get(i))
						.retrieve().bodyToMono(Notes_.class).block();
				allNotesOfUser.add(notesResponse);
			}
			Notes_ notesResponse1 = webClient.build().get().uri("http://localhost:1999/Get/NotesByName/" + name)
					.retrieve().bodyToMono(Notes_.class).block();
			for (int i = 0; i < ua.size(); i++) {
				String nid = notesResponse1.getId();
				if (ua.get(i).equals(nid)) {
					ua.remove(i);
				}
			}
			user.setNotesIds(ua);
			user_Repo.save(user);
			webClient.build().delete().uri("http://localhost:1999/Delete/NotesById/" + notesResponse1.getId())
					.retrieve().bodyToMono(Void.class).block();
			log.info("\n" + username + ", " + name + " Notes Was Successfully Removed");
		} catch (Exception e) {
			throw new Resource_NotFound_Exception_("No Notes was found with the name :- '" + name + "'.");
		}
	}

    // Delete User Using User Token
	public void deleteUser(HttpServletRequest request) throws Resource_NotFound_Exception_ {
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null, jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwtToken);
		}
		Optional<Users_> optionalUser = user_Repo.findByUserName(username);
		Users_ user = optionalUser.get();
		ArrayList<String> ua = user.getNotesIds();
		for (int i = 0; i < ua.size(); i++) {
			webClient.build().delete().uri("http://localhost:1999/Delete/NotesById/" + ua.get(i)).retrieve()
					.bodyToMono(Void.class).block();
		}
		user_Repo.delete(user);
		log.info("\n" + username + ", your account and all your notes were successfully removed");
	}
	
	// Delete User Using Admin Token And UserName
	public void deleteUserByAdmin(String userName) throws Resource_NotFound_Exception_ {
		Optional<Users_> optionalUser = Optional.of(user_Repo.findByUserName(userName).orElseThrow(
				() -> new Resource_NotFound_Exception_("No user with username :- '" + userName + "' was found.")));
		Users_ user = optionalUser.get();
		ArrayList<String> ua = user.getNotesIds();
		for (int i = 0; i < ua.size(); i++) {
			webClient.build().delete().uri("http://localhost:1999/Delete/NotesById/" + ua.get(i)).retrieve()
					.bodyToMono(Void.class).block();
		}
		user_Repo.delete(user);
		log.info("\nAdmin,You have removed '" + userName + "' account and all of "+userName+" notes were successfully removed");
	}

}
