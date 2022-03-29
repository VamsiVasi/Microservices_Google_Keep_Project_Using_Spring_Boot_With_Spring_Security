package com.albanero.Notes_Microservice_.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albanero.Notes_Microservice_.Model.Notes_;
import com.albanero.Notes_Microservice_.Repository.Notes_Repo_;

@Service
public class Notes_Serv_ {

	@Autowired
	private Notes_Repo_ notes_Repo;

	public Notes_ createNotes(Notes_ notes) {
		return notes_Repo.save(notes);
	}

	public Notes_ getNotesById(String id) {
		Optional<Notes_> optionalNotes = notes_Repo.findById(id);
		return optionalNotes.get();
	}

	public Notes_ getNotesByName(String name) {
		Optional<Notes_> optionalNotes = notes_Repo.findByName(name);
		return optionalNotes.get();
	}

	public Notes_ updateNotes(String id, Notes_ notes) {
		Optional<Notes_> optionalNotes = notes_Repo.findById(id);
		Notes_ updateNK = optionalNotes.get();
		updateNK.setName(notes.getName());
		updateNK.setFile(notes.getFile());
		updateNK.setFileURL(notes.getFileURL());
		return notes_Repo.save(updateNK);
	}

	public void deleteNotes(String id) {
		notes_Repo.deleteById(id);
	}

}
