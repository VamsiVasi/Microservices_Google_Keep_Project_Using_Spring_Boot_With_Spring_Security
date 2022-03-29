package com.albanero.Notes_Microservice_.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.albanero.Notes_Microservice_.Model.Notes_;
import com.albanero.Notes_Microservice_.Service.Notes_Serv_;

@RestController

public class Notes_Contro_ {

	@Autowired
	private Notes_Serv_ notes_Serv;

	@PostMapping("/Create/Notes")
	public Notes_ createNotes(@RequestBody Notes_ notes) {
		return notes_Serv.createNotes(notes);
	}

	@GetMapping("/Get/NotesById/{id}")
	public ResponseEntity<Notes_> getNotesById(@PathVariable String id) {
		return ResponseEntity.ok(notes_Serv.getNotesById(id));
	}

	@GetMapping("/Get/NotesByName/{name}")
	public ResponseEntity<Notes_> getNotesByName(@PathVariable String name) {
		return ResponseEntity.ok(notes_Serv.getNotesByName(name));
	}

	@PutMapping("/Put/NotesById/{id}")
	public ResponseEntity<Notes_> updateNotes(@PathVariable String id, @RequestBody Notes_ notes) {
		return ResponseEntity.ok(notes_Serv.updateNotes(id, notes));
	}

	@DeleteMapping("/Delete/NotesById/{id}")
	public String deleteNotes(@PathVariable String id) {
		notes_Serv.deleteNotes(id);
		return "Notes with Id : " + id + " was Successfully deleted";
	}
}