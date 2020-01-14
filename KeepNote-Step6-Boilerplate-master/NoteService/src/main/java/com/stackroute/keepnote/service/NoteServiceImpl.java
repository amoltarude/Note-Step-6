package com.stackroute.keepnote.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService{

	/*
	 * Autowiring should be implemented for the NoteRepository and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	public NoteServiceImpl(NoteRepository noteRepository) {
		super();
		this.noteRepository = noteRepository;
	}
	/*
	 * This method should be used to save a new note.
	 */
	public boolean createNote(Note note) {
		NoteUser noteUser = new NoteUser();
		List<Note> notes = new ArrayList<Note>();
		notes.add(note);
		noteUser.setUserId(note.getNoteCreatedBy());
		noteUser.setNotes(notes);
		NoteUser insert = noteRepository.insert(noteUser);
		if (insert!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	/* This method should be used to delete an existing note. */

	
	public boolean deleteNote(String userId, int noteId) {
		
		NoteUser noteUser = noteRepository.findById(userId).get();
		List<Note> collect = noteUser.getNotes().stream().filter(note -> note.getNoteId()!= noteId).collect(Collectors.toList());
		noteUser.setNotes(collect);
		noteRepository.save(noteUser);
		return true;
	}
	
	/* This method should be used to delete all notes with specific userId. */

	
	public boolean deleteAllNotes(String userId) {
		NoteUser noteUser = noteRepository.findById(userId).get();
		noteUser.setNotes(new ArrayList<Note>());
		noteRepository.save(noteUser);
		return true;
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		try {
			NoteUser noteUser = noteRepository.findById(userId).get();
			Note note2 = noteUser.getNotes().stream().filter(item -> item.getNoteId() == id).findFirst().get();
			int indexOf = noteUser.getNotes().indexOf(note2);
			noteUser.getNotes().set(indexOf, note);
			noteRepository.save(noteUser);
		} catch (NoSuchElementException e) {
			throw new NoteNotFoundExeption("NoteNotFoundExeption");
		}
		return note;
	}

	/*
	 * This method should be used to get a note by noteId created by specific user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		Note note2;
		try {
			NoteUser noteUser = noteRepository.findById(userId).get();
			note2 = noteUser.getNotes().stream().filter(item -> item.getNoteId() == noteId).findFirst().get();
			
		} catch (NoSuchElementException e) {
			throw new NoteNotFoundExeption("NoteNotFoundExeption");
		}
		return note2;
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {
		return noteRepository.findById(userId).get().getNotes();
	}

}
